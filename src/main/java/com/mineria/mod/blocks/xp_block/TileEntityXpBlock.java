package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityXpBlock extends TileEntity implements ISidedInventory, ITickable
{
	private String customName;
	private ItemStack result = ItemStack.EMPTY;
	private EntityPlayer player;
	private boolean playerFound = false;
	private int ticks;
	
	private NonNullList<ItemStack> xpBlockItemStacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.xpBlockItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.xpBlockItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.xpBlockItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.xpBlockItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.xpBlockItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.xpBlockItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag)
		{
			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 16;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
	}

	@Override
	public void openInventory(EntityPlayer player)
	{

	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		this.playerFound = false;
		this.ticks = 0;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		this.result = ItemStack.EMPTY;
		this.playerFound = false;
		this.ticks = 0;
	}

	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "container.xp_block";
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.xp_block");
	}

	@Override
	public boolean hasCustomName()
	{
		return this.customName != null && !this.customName.isEmpty();
	}
	
	public void setName(String name)
    {
        this.customName = name;
    }
	
	public String getGuiID()
    {
        return "mineria:xp_block";
    }
	
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerXpBlock(playerInventory, this, world, pos);
    }

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return false;
	}
	
	public void spawnXp(EntityPlayer player)
	{
		ItemStack stack = new ItemStack(ItemsInit.mineria_xp_orb);
		ItemStack stack1 = this.xpBlockItemStacks.get(0);
		if (stack1.getCount() < this.getInventoryStackLimit())
		{
			if (player.experienceTotal > 0)
			{
				player.addExperience(-1);
				if (stack1.isEmpty())
				{
					this.xpBlockItemStacks.set(0, stack.copy());
				}
				else if (stack1.getItem() == stack.getItem())
				{
					stack1.grow(1);
				}
			}
		}
	}

	public void setPlayer(EntityPlayer player)
	{
		this.player = player;
		this.playerFound = true;
	}

	@Override
	public void update()
	{
		if(playerFound)
		{
			ticks++;
			if (ticks >= 60)
			{
				ticks = 0;
				this.spawnXp(this.player);
			}
		}
	}
}
