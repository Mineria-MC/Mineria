package io.github.mineria_mc.mineria.common.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class XpBlockSlot extends Slot {

    public XpBlockSlot(Container container, int index, int xPos, int yPos) {
        super(container, index, xPos, yPos);
    }
    
    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
