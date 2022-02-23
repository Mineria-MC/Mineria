package com.mineria.mod.common.items;

import com.mineria.mod.common.enchantments.FourElementsEnchantment;
import com.mineria.mod.common.entity.ElementalOrbEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpecialItem extends Item
{
    public SpecialItem()
    {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        if(!world.isClientSide())
        {
            ElementalOrbEntity elementalOrb = new ElementalOrbEntity(world, player, FourElementsEnchantment.ElementType.FIRE, 8);
            elementalOrb.setPos(player.getX(), player.getY(), player.getZ());
            world.addFreshEntity(elementalOrb);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), false);
        }
        return super.use(world, player, hand);
    }
}
