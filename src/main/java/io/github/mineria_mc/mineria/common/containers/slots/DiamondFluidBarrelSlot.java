package io.github.mineria_mc.mineria.common.containers.slots;

import io.github.mineria_mc.mineria.common.items.DiamondBarrelUpgradeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class DiamondFluidBarrelSlot extends SlotItemHandler {
    public DiamondFluidBarrelSlot(IItemHandler handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof DiamondBarrelUpgradeItem;
    }

    public static class OptionalInventorySlot extends SlotItemHandler {
        public OptionalInventorySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isActive() {
            return getItemHandler().getSlots() > 0;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return isActive() && super.mayPlace(stack);
        }
    }
}
