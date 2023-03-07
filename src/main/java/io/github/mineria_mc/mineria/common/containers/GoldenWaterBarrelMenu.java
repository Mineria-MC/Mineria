package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.barrel.golden.GoldenWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.items.DrinkItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GoldenWaterBarrelMenu extends MineriaMenu<GoldenWaterBarrelBlockEntity> {
    public GoldenWaterBarrelMenu(int id, Inventory playerInv, GoldenWaterBarrelBlockEntity tileEntity) {
        super(MineriaMenuTypes.GOLDEN_WATER_BARREL.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 98);
    }

    public static GoldenWaterBarrelMenu create(int id, Inventory playerInv, FriendlyByteBuf data) {
        return new GoldenWaterBarrelMenu(id, playerInv, getTileEntity(GoldenWaterBarrelBlockEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(GoldenWaterBarrelBlockEntity tile) {
        for (int y = 0; y < 4; y++) {
            this.addSlot(new SlotItemHandler(tile.getInventory(), y, 26, 16 + y * 18) {
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof DrinkItem || stack.is(MineriaItems.CUP.get());
                }
            });
        }

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                this.addSlot(new SlotItemHandler(tile.getInventory(), x + y * 4 + 4, 62 + x * 18, 16 + y * 18) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof PotionItem || stack.getItem() instanceof BottleItem;
                    }
                });
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if (index < 20) {
                if (!this.moveItemStackTo(slotStack, 20, this.slots.size(), true))
                    return ItemStack.EMPTY;

                slot.onQuickCraft(slotStack, stackToTransfer);
            } else {
                if (slotStack.getItem() instanceof DrinkItem || slotStack.is(MineriaItems.CUP.get())) {
                    if (!this.moveItemStackTo(slotStack, 0, 4, false))
                        return ItemStack.EMPTY;
                } else if (slotStack.getItem() instanceof PotionItem || slotStack.getItem() instanceof BottleItem) {
                    if (!this.moveItemStackTo(slotStack, 4, 20, false))
                        return ItemStack.EMPTY;
                } else if (index < 45) {
                    if (!this.moveItemStackTo(slotStack, this.slots.size() - 9, this.slots.size(), false))
                        return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stackToTransfer.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stackToTransfer;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
