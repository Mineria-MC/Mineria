package com.mineria.mod.blocks.xp_block;

public class TileEntityXpBlock// extends TileEntity implements ISidedInventory
{
	/*
	private String customName;

	private final NonNullList<ItemStack> xpBlockItemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
	
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
        return new ContainerXpBlock(playerInventory, this);
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
		ItemStack stack = new ItemStack(ItemsInit.MINERIA_XP_ORB);
		ItemStack stack1 = this.xpBlockItemStacks.get(0);
		if (stack1.getCount() < this.getInventoryStackLimit())
		{
			if(player.capabilities.isCreativeMode)
			{
				if (stack1.isEmpty())
				{
					this.xpBlockItemStacks.set(0, stack.copy());
				}
				else if (stack1.getItem() == stack.getItem())
				{
					stack1.grow(1);
				}
			}
			else if (player.experienceTotal > 0)
			{
				this.decreasePlayerExperience(1, player);
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

	private void decreasePlayerExperience(int amount, EntityPlayer player)
	{
		player.experience -= (float)amount / (float)this.calcXpBarCap(player.experienceLevel - 1);
		player.experienceTotal -= amount;

		if(player.experience <= 0.0F)
		{
			player.experienceLevel -= 1;
			player.experience = 1.0F;
		}
	}

	public int calcXpBarCap(int level)
	{
		if (level >= 30)
		{
			return 112 + (level - 30) * 9;
		}
		else
		{
			return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
		}
	}

	public static void executeProcedure(int x, int y, int z, World world, EntityPlayer player)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(tileEntity instanceof TileEntityXpBlock)
		{
			TileEntityXpBlock tile = (TileEntityXpBlock)tileEntity;
			tile.spawnXp(player);
		}
	}

	 */
}
