package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.blocks.titane_extractor.slots.SlotTitaneExtractorFilter;
import com.mineria.mod.blocks.titane_extractor.slots.SlotTitaneExtractorOutput;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.items.ItemBlockBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerTitaneExtractor extends Container
{
	private final TileEntityTitaneExtractor tileTitaneExtractor;
    private int extractTime;
    private int totalExtractTime;
    private int burnTime;
    private int currentItemBurnTime;
	
    public ContainerTitaneExtractor(InventoryPlayer playerInventory, TileEntityTitaneExtractor titaneExtractorInventory)
    {
    	this.tileTitaneExtractor = titaneExtractorInventory;
    	this.addSlotToContainer(new Slot(titaneExtractorInventory, 0, 10, 7));
		this.addSlotToContainer(new Slot(titaneExtractorInventory, 1, 41, 7));
		this.addSlotToContainer(new SlotTitaneExtractorFilter(titaneExtractorInventory, 2, 24, 78));
		this.addSlotToContainer(new SlotTitaneExtractorOutput(playerInventory.player, titaneExtractorInventory, 3, 95, 47));
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x + y*9 + 9, 20 + x*18, 101 + y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInventory, x, 20 + x * 18, 159));
		}
    }
    
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileTitaneExtractor);
    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.extractTime != this.tileTitaneExtractor.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileTitaneExtractor.getField(2));
            }

            if (this.burnTime != this.tileTitaneExtractor.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileTitaneExtractor.getField(0));
            }

            if (this.currentItemBurnTime != this.tileTitaneExtractor.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileTitaneExtractor.getField(1));
            }

            if (this.totalExtractTime != this.tileTitaneExtractor.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileTitaneExtractor.getField(3));
            }
        }

        this.extractTime = this.tileTitaneExtractor.getField(2);
        this.burnTime = this.tileTitaneExtractor.getField(0);
        this.currentItemBurnTime = this.tileTitaneExtractor.getField(1);
        this.totalExtractTime = this.tileTitaneExtractor.getField(3);
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileTitaneExtractor.setField(id, data);
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileTitaneExtractor.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 3)
            {
                if(!this.mergeItemStack(itemstack1, 4, 40, true)) return ItemStack.EMPTY;

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 0 && index != 1 && index != 2)
            {
                if (!TitaneExtractorRecipes.getInstance().getExtractingResult(itemstack1, itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(itemstack1.getItem().equals(new ItemStack(BlocksInit.mineral_sand).getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(itemstack1.getItem() instanceof ItemBlockBarrel)
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemstack1.getItem().equals(ItemsInit.filter))
                {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 4 && index < 31)
                {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4, 40, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
