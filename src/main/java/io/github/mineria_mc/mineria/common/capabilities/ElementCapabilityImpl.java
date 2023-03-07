package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.common.enchantments.ElementType;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class ElementCapabilityImpl implements IElementCapability {
    private static final int APPLICATION_TIME = 400; // 20 seconds

    private final Map<ElementType, TickInfo> exposureMap = new HashMap<>();

    @Override
    public void applyElement(@Nonnull ElementType element) {
        if (ElementType.NONE.equals(element)) return;
        exposureMap.compute(element, (e, existing) -> existing == null ? new TickInfo(0) : existing.incrementCount());
    }

    private boolean isAffected(ElementType element) {
        return !ElementType.NONE.equals(element) && exposureMap.containsKey(element) && exposureMap.get(element) != null;
    }

    @Override
    public int applicationCount(ElementType element) {
        return isAffected(element) ? exposureMap.get(element).getCount() : 0;
    }

    @Override
    public long getTickCount(ElementType element) {
        return isAffected(element) ? exposureMap.get(element).getAppliedTick() : 0;
    }

    @Override
    public void tick() {
        Set<ElementType> toRemove = new LinkedHashSet<>();
        exposureMap.forEach((type, info) -> {
            if (info.getAppliedTick() >= APPLICATION_TIME || ElementType.NONE.equals(type))
                toRemove.add(type);
            else
                info.atTime(info.getAppliedTick() + 1);
        });
        toRemove.forEach(this::removeElement);
    }

    @Override
    public void removeElement(ElementType element) {
        exposureMap.remove(element);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        CompoundTag map = new CompoundTag();

        exposureMap.forEach((key, value) -> map.put(Integer.toString(key.getId()), value.toNbt()));
        nbt.put("ExposureMap", map);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag map = nbt.getCompound("ExposureMap");
        map.getAllKeys().forEach(key -> {
            try {
                ElementType element = ElementType.byId(Integer.parseInt(key));
                if (element != ElementType.NONE) exposureMap.put(element, TickInfo.fromNbt(map.getCompound(key)));
            } catch (NumberFormatException ignored) {
            }
        });
    }
}
