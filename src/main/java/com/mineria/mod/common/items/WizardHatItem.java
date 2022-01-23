package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import net.minecraft.enchantment.IArmorVanishable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WizardHatItem extends Item implements IArmorVanishable
{
    public WizardHatItem()
    {
        super(new Properties().stacksTo(1).tab(Mineria.APOTHECARY_GROUP).rarity(Rarity.UNCOMMON));
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack)
    {
        return EquipmentSlotType.HEAD;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack heldItem = player.getItemInHand(hand);
        ItemStack headItem = player.getItemBySlot(EquipmentSlotType.HEAD);
        if (headItem.isEmpty())
        {
            player.setItemSlot(EquipmentSlotType.HEAD, heldItem.copy());
            heldItem.setCount(0);
            return ActionResult.sidedSuccess(heldItem, world.isClientSide());
        }
        return ActionResult.fail(heldItem);
    }
}
