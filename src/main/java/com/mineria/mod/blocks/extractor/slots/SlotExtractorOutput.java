package com.mineria.mod.blocks.extractor.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotExtractorOutput extends SlotItemHandler
{
    private final EntityPlayer player;
    private int removeCount;

    public SlotExtractorOutput(EntityPlayer player, IItemHandler inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }

    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }

    protected void onCrafting(ItemStack stack, int amount)
    {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }
}
