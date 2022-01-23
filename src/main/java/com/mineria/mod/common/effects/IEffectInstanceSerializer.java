package com.mineria.mod.common.effects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public interface IEffectInstanceSerializer<T extends EffectInstance> extends IForgeRegistryEntry<IEffectInstanceSerializer<?>>
{
    void encodePacket(T effect, PacketBuffer buf);

    T decodePacket(PacketBuffer buf);

    T deserialize(Effect effect, CompoundNBT nbt);

    static <T extends EffectInstance> T readCurativeItems(T effect, CompoundNBT nbt)
    {
        if (nbt.contains("CurativeItems", Constants.NBT.TAG_LIST))
        {
            List<ItemStack> items = new ArrayList<>();
            ListNBT list = nbt.getList("CurativeItems", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++)
            {
                items.add(ItemStack.of(list.getCompound(i)));
            }
            effect.setCurativeItems(items);
        }

        return effect;
    }
}
