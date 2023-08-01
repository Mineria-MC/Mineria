package io.github.mineria_mc.mineria.common.capabilities.ticking_data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.capabilities.ticking_data.ITickingDataCapability.DataType;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.enchantments.ElementType;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public final class TickingDataTypes {
    public static final DataType<PoisonSource> POISON_EXPOSURE = new DataType<>(new ResourceLocation(Mineria.MODID, "poison_exposure"), id -> {
        ResourceLocation name = ResourceLocation.tryParse(id);
        PoisonSource source = PoisonSource.byName(name);
        // PoisonSource#byName never yields null and instead returns PoisonSource.UNKNOWN, a default value.
        if(source == PoisonSource.UNKNOWN && !PoisonSource.UNKNOWN.getId().equals(name)) {
            return null;
        }
        return source;
    });
    public static final DataType<ElementType> ELEMENT_EXPOSURE = new DataType<>(new ResourceLocation(Mineria.MODID, "element_exposure"), id -> {
        try {
            return ElementType.valueOf(id.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    });
}