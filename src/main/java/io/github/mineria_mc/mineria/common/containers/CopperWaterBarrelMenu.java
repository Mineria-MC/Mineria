package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.copper.CopperWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CopperWaterBarrelMenu extends MineriaMenu<CopperWaterBarrelBlockEntity> {
    public CopperWaterBarrelMenu(int id, Inventory playerInv, CopperWaterBarrelBlockEntity tileEntity) {
        super(MineriaMenuTypes.COPPER_WATER_BARREL.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 44);
    }

    public static CopperWaterBarrelMenu create(int windowID, Inventory playerInv, FriendlyByteBuf data) {
        return new CopperWaterBarrelMenu(windowID, playerInv, getTileEntity(CopperWaterBarrelBlockEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(CopperWaterBarrelBlockEntity tile) {
        for (int slotIndex = 0; slotIndex < 8; slotIndex++) {
            this.addSlot(new SlotItemHandler(tile.getInventory(), slotIndex, 17 + slotIndex * 18, 13) {
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return !(stack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || stack.getItem().equals(Items.SHULKER_BOX));
                }
            });
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if (index < 9) {
                if (!this.moveItemStackTo(slotStack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stackToTransfer;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}