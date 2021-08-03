package com.mineria.mod.effects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface IEffectInstanceSerializer<T extends EffectInstance>
{
    List<Supplier<IEffectInstanceSerializer<?>>> SERIALIZERS = Util.make(new ArrayList<>(), list -> {
        list.add(CustomEffectInstance.Serializer::new);
        list.add(PoisonEffectInstance.Serializer::new);
        list.add(BowelSoundEffectInstance.Serializer::new);
    });

    ResourceLocation getName();

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
                items.add(ItemStack.read(list.getCompound(i)));
            }
            effect.setCurativeItems(items);
        }

        return effect;
    }

    @SuppressWarnings("unchecked")
    static <T extends EffectInstance> IEffectInstanceSerializer<T> getSerializer(ResourceLocation name)
    {
        IEffectInstanceSerializer<?> serializer = SERIALIZERS.stream().map(Supplier::get).filter(s -> s.getName().equals(name)).findFirst().orElse(null);
        return (IEffectInstanceSerializer<T>) (serializer == null ? new EffectInstanceSerializer() : serializer);
    }
}
