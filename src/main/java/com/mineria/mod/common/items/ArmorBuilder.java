package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.util.TriConsumer;

public class ArmorBuilder
{
    private final ArmorMaterial material;
    private final EquipmentSlot slot;
    private CreativeModeTab group = Mineria.MINERIA_GROUP;
    private boolean hasEffect = false;
    private TriConsumer<ItemStack, Level, Player> function = (world, player, stack) -> {};

    public ArmorBuilder(ArmorMaterial material, EquipmentSlot slot)
    {
        this.material = material;
        this.slot = slot;
    }

    public ArmorBuilder setCreativeTab(CreativeModeTab group)
    {
        this.group = group;
        return this;
    }

    public ArmorBuilder addEffect()
    {
        this.hasEffect = true;
        return this;
    }

    public ArmorBuilder onArmorTick(TriConsumer<ItemStack, Level, Player> function)
    {
        this.function = function;
        return this;
    }

    public ArmorItem build()
    {
        return new BuiltArmor(this);
    }

    private static class BuiltArmor extends ArmorItem
    {
        private final ArmorBuilder builder;

        public BuiltArmor(ArmorBuilder builder)
        {
            super(builder.material, builder.slot, new Item.Properties().tab(builder.group));
            this.builder = builder;
        }

        @Override
        public boolean isFoil(ItemStack stack)
        {
            return builder.hasEffect || super.isFoil(stack);
        }

        @Override
        public void onArmorTick(ItemStack stack, Level world, Player player)
        {
            builder.function.accept(stack, world, player);
        }
    }
}
