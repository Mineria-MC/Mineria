package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

public class ArmorBuilder
{
    private final ItemArmor.ArmorMaterial material;
    private final EntityEquipmentSlot slot;
    private final int renderIndex;
    private CreativeTabs tab = Mineria.mineriaTab;
    private boolean hasEffect = false;
    private TriConsumer<World, EntityPlayer, ItemStack> function = (world, player, stack) -> {};

    public ArmorBuilder(ItemArmor.ArmorMaterial material, EntityEquipmentSlot slot)
    {
        this.material = material;
        this.slot = slot;
        this.renderIndex = slot == EntityEquipmentSlot.LEGS ? 2 : 1;
    }

    public ArmorBuilder setCreativeTab(CreativeTabs tab)
    {
        this.tab = tab;
        return this;
    }

    public ArmorBuilder addEffect()
    {
        this.hasEffect = true;
        return this;
    }

    public ArmorBuilder onArmorTick(TriConsumer<World, EntityPlayer, ItemStack> function)
    {
        this.function = function;
        return this;
    }

    public ItemArmor build(String name)
    {
        return new BuiltArmor(this, name);
    }

    private static class BuiltArmor extends ItemArmor
    {
        private final ArmorBuilder builder;

        public BuiltArmor(ArmorBuilder builder, String name)
        {
            super(builder.material, builder.renderIndex, builder.slot);
            setRegistryName(name);
            setUnlocalizedName(name);
            setCreativeTab(builder.tab);
            this.builder = builder;
        }

        @Override
        public boolean hasEffect(ItemStack stack)
        {
            return builder.hasEffect;
        }

        @Override
        public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
        {
            builder.function.accept(world, player, itemStack);
        }
    }
}
