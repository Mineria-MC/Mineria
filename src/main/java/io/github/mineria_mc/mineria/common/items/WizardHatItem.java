package io.github.mineria_mc.mineria.common.items;

import net.minecraft.world.item.Wearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class WizardHatItem extends Item implements Wearable {
    public WizardHatItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.isEmpty()) {
            player.setItemSlot(EquipmentSlot.HEAD, heldItem.copy());
            heldItem.setCount(0);
            return InteractionResultHolder.sidedSuccess(heldItem, world.isClientSide());
        }
        return InteractionResultHolder.fail(heldItem);
    }
}
