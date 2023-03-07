package io.github.mineria_mc.mineria.common.effects.util;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

// TODO: Replace with codecs
public interface IMobEffectInstanceSerializer<T extends MobEffectInstance> {
    Logger LOGGER = LogUtils.getLogger();

    void encodePacket(T effect, FriendlyByteBuf buf);

    T decodePacket(FriendlyByteBuf buf);

    T deserialize(MobEffect effect, CompoundTag nbt);

    static <T extends MobEffectInstance> T readCurativeItems(T effect, CompoundTag nbt) {
        if (nbt.contains("CurativeItems", Tag.TAG_LIST)) {
            List<ItemStack> items = new ArrayList<>();
            ListTag list = nbt.getList("CurativeItems", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                items.add(ItemStack.of(list.getCompound(i)));
            }
            effect.setCurativeItems(items);
        }

        return effect;
    }
}
