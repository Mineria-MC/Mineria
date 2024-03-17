package io.github.mineria_mc.mineria.common.containers.slots;

import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FilterSlot extends Slot {

    public FilterSlot(Container inventory, int index, int xPos, int yPos) {
        super(inventory, index, xPos, yPos);
    }
    
    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.is(MineriaItemRegistry.FILTER);
    }
}
