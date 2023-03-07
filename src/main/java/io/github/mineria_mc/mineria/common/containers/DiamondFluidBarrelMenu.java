package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.barrel.diamond.DiamondFluidBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.DiamondFluidBarrelSlot;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.common.items.DiamondBarrelUpgradeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DiamondFluidBarrelMenu extends MineriaMenu<DiamondFluidBarrelBlockEntity> {
    private final ContainerData data;

    public DiamondFluidBarrelMenu(int id, Inventory playerInv, DiamondFluidBarrelBlockEntity tileEntity) {
        super(MineriaMenuTypes.DIAMOND_FLUID_BARREL.get(), id, tileEntity);

        this.data = tileEntity.getContainerData();
        this.addDataSlots(data);

        this.createPlayerInventorySlots(playerInv, 19, 84);
    }

    public static DiamondFluidBarrelMenu create(int id, Inventory playerInv, FriendlyByteBuf data) {
        return new DiamondFluidBarrelMenu(id, playerInv, getTileEntity(DiamondFluidBarrelBlockEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(DiamondFluidBarrelBlockEntity tile) {
        for(int i = 0; i < 3; i++) {
            addSlot(new DiamondFluidBarrelSlot(tile.getUpgradeInventory(), i, 15, 18 + 18 * i));
        }

        for(int i = 0; i < 8; i++) {
            addSlot(new DiamondFluidBarrelSlot.OptionalInventorySlot(tile.getTopInv(), i, 41 + 18 * i, 18));
        }

        for(int i = 0; i < 8; i++) {
            addSlot(new DiamondFluidBarrelSlot.OptionalInventorySlot(tile.getMiddleInv(), i, 41 + 18 * i, 36));
        }

        for(int i = 0; i < 8; i++) {
            addSlot(new DiamondFluidBarrelSlot.OptionalInventorySlot(tile.getBottomInv(), i, 41 + 18 * i, 54));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInventoryActive(int index) {
        return data.get(index) > 0;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        final int lastIndex = this.slots.size();

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if (index >= 3 && index <= 26) {
                // Barrel Inventory slot clicked

                if (!this.moveItemStackTo(slotStack, 27, lastIndex, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27) {
                // Player Inventory slot clicked

                if (slotStack.getItem() instanceof DiamondBarrelUpgradeItem) {
                    if (!this.moveItemStackTo(slotStack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(!this.moveItemStackTo(slotStack, 3, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 27, lastIndex, false)) {
                // Upgrade slot clicked

                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stackToTransfer.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return stackToTransfer;
    }
}
