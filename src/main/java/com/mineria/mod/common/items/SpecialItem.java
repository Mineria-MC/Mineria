package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.enchantments.FourElementsEnchantment;
import com.mineria.mod.common.entity.ElementalOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SpecialItem extends Item
{
    public SpecialItem()
    {
        super(new Item.Properties());
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        if(!world.isClientSide())
        {
            ElementalOrbEntity elementalOrb = new ElementalOrbEntity(world, player, FourElementsEnchantment.ElementType.FIRE, 8);
            elementalOrb.setPos(player.getX(), player.getY(), player.getZ());
            world.addFreshEntity(elementalOrb);
            return ActionResult.sidedSuccess(player.getItemInHand(hand), false);
        }
        return super.use(world, player, hand);
    }
}
