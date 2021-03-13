package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

public class ArmorBuilder
{
    private final IArmorMaterial material;
    private final EquipmentSlotType slot;
    private ItemGroup group = Mineria.MINERIA_GROUP;
    private boolean hasEffect = false;
    private TriConsumer<ItemStack, World, PlayerEntity> function = (world, player, stack) -> {};

    public ArmorBuilder(IArmorMaterial material, EquipmentSlotType slot)
    {
        this.material = material;
        this.slot = slot;
    }

    public ArmorBuilder setCreativeTab(ItemGroup group)
    {
        this.group = group;
        return this;
    }

    public ArmorBuilder addEffect()
    {
        this.hasEffect = true;
        return this;
    }

    public ArmorBuilder onArmorTick(TriConsumer<ItemStack, World, PlayerEntity> function)
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
            super(builder.material, builder.slot, new Item.Properties().group(builder.group));
            this.builder = builder;
        }

        @Override
        public boolean hasEffect(ItemStack stack)
        {
            return builder.hasEffect;
        }

        @Override
        public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
        {
            builder.function.accept(stack, world, player);
        }
    }
}
