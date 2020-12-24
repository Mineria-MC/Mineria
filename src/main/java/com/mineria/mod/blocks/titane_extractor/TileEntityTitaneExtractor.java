package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTitaneExtractor extends TileEntity implements ITickable, ISidedInventory
{
	private static final int[] SLOTS_TOP = new int[] {0, 1};
    private static final int[] SLOTS_BOTTOM = new int[] {3};
    private static final int[] SLOTS_SIDES = new int[] {2};
	
	private NonNullList<ItemStack> titaneExtactorItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	
	private int burnTime;
	private int currentItemBurnTime;
	private int extractTime;
    private int totalExtractTime;
    private String titaneExtractorCustomName;
	
	@Override
	public int getSizeInventory()
	{
		return this.titaneExtactorItemStacks.size();
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.titaneExtactorItemStacks)
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
		return this.titaneExtactorItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.titaneExtactorItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.titaneExtactorItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.titaneExtactorItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.titaneExtactorItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && index + 1 == 1 && !flag)
        {
        	ItemStack stack1 = (ItemStack)this.titaneExtactorItemStacks.get(index + 1);
            this.totalExtractTime = this.getExtractTime(stack, stack1);
            this.extractTime = 0;
            this.markDirty();
        }
	}
	
	@Override
	public String getName()
    {
        return this.hasCustomName() ? this.titaneExtractorCustomName : "container.titane_extractor";
    }
	
	@Override
	public ITextComponent getDisplayName()
	{
		return this.hasCustomName() ? new TextComponentString(this.titaneExtractorCustomName) : new TextComponentTranslation("container.titane_extractor");
	}
	
	@Override
	public boolean hasCustomName()
    {
        return this.titaneExtractorCustomName != null && !this.titaneExtractorCustomName.isEmpty();
    }
	
	public void setCustomInventoryName(String name)
    {
        this.titaneExtractorCustomName = name;
    }
	
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.titaneExtactorItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.titaneExtactorItemStacks);
        this.burnTime = compound.getInteger("BurnTime");
        this.extractTime = compound.getInteger("ExtractTime");
        this.totalExtractTime = compound.getInteger("ExtractTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.titaneExtactorItemStacks.get(2));

        if (compound.hasKey("CustomName", 8))
        {
            this.titaneExtractorCustomName = compound.getString("CustomName");
        }
    }
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.burnTime);
        compound.setInteger("ExtractTime", (short)this.extractTime);
        compound.setInteger("ExtractTimeTotal", (short)this.totalExtractTime);
        ItemStackHelper.saveAllItems(compound, this.titaneExtactorItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.titaneExtractorCustomName);
        }

        return compound;
    }

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public boolean isExtracting()
    {
        return this.burnTime > 0;
    }
	
	@SideOnly(Side.CLIENT)
    public static boolean isExtracting(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }
	
	@Override
	public void update()
	{
		boolean flag = this.isExtracting();
        boolean flag1 = false;

        if (this.isExtracting())
        {
            --this.burnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.titaneExtactorItemStacks.get(2);

            if (this.isExtracting() || !itemstack.isEmpty() && !((ItemStack)this.titaneExtactorItemStacks.get(0)).isEmpty() || !((ItemStack)this.titaneExtactorItemStacks.get(1)).isEmpty())
            {
                if (!this.isExtracting() && this.canExtract())
                {
                	this.burnTime = 200;
                    this.currentItemBurnTime = this.burnTime;

                    if (this.isExtracting())
                    {
                        flag1 = true;

                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();

                            if (itemstack.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.titaneExtactorItemStacks.set(2, item1);
                            }
                        }
                    }
                }

                if (this.isExtracting() && this.canExtract())
                {
                    ++this.extractTime;

                    if (this.extractTime == this.totalExtractTime)
                    {
                        this.extractTime = 0;
                        this.totalExtractTime = this.getExtractTime((ItemStack)this.titaneExtactorItemStacks.get(0), (ItemStack)this.titaneExtactorItemStacks.get(1));
                        this.extractItem();
                        flag1 = true;
                    }
                }
            }
            else if (!this.isExtracting() && this.extractTime > 0)
            {
                this.extractTime = MathHelper.clamp(this.extractTime - 2, 0, this.totalExtractTime);
            }

            if (flag != this.isExtracting())
            {
                flag1 = true;
                BlockTitaneExtractor.setState(this.isExtracting(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
	}
	
	public int getExtractTime(ItemStack stack, ItemStack stack2)
    {
        return 200;
    }
	
	private boolean canExtract()
	{
        ItemStack stack = ((ItemStack)this.titaneExtactorItemStacks.get(1));
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null || !compound.hasKey("BlockEntityTag", 10))
        {
            return false;
        }
        NBTTagCompound compound1 = compound.getCompoundTag("BlockEntityTag");

		if (((ItemStack)this.titaneExtactorItemStacks.get(0)).isEmpty() || stack.isEmpty() || ((ItemStack)this.titaneExtactorItemStacks.get(2)).isEmpty() || compound1.getInteger("Water") == 0)
		{
			return false;
		}
		else
		{
			ItemStack itemstack = TitaneExtractorRecipes.getInstance().getExtractingResult((ItemStack)this.titaneExtactorItemStacks.get(0), (ItemStack)this.titaneExtactorItemStacks.get(1));

			if (itemstack.isEmpty())
			{
				return false;
			}
			else
			{
				ItemStack itemstack1 = (ItemStack)this.titaneExtactorItemStacks.get(3);

				if (itemstack1.isEmpty())
				{
					return true;
				}
				else if (!itemstack1.isItemEqual(itemstack))
				{
					return false;
				}
				int res = itemstack1.getCount() + itemstack.getCount();
				return res <= getInventoryStackLimit() && res <= itemstack1.getMaxStackSize();
			}
		}
	}
	 
	public void extractItem()
	{
		if (this.canExtract())
		{
			ItemStack itemstack = this.titaneExtactorItemStacks.get(0);
			ItemStack itemstack1 = this.titaneExtactorItemStacks.get(1);
			ItemStack itemstack2 = TitaneExtractorRecipes.getInstance().getExtractingResult(itemstack, itemstack1);
			ItemStack itemstack3 = this.titaneExtactorItemStacks.get(3);
			ItemStack itemstack5 = this.titaneExtactorItemStacks.get(2);

			if (itemstack3.isEmpty())
			{
				this.titaneExtactorItemStacks.set(3, itemstack2.copy());
			}
			else if (itemstack3.getItem() == itemstack2.getItem())
			{
				itemstack3.grow(itemstack2.getCount());
			}

			itemstack.shrink(1);
            NBTTagCompound compound = itemstack1.getTagCompound();
            NBTTagCompound compound1 = compound.getCompoundTag("BlockEntityTag");
            compound1.setInteger("Water", compound1.getInteger("Water") - 1);
            itemstack5.shrink(1);
		}
	}
	
	public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        }
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB))
            {
                return 150;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOOL))
            {
                return 100;
            }
            else if (item == Item.getItemFromBlock(Blocks.CARPET))
            {
                return 67;
            }
            else if (item == Item.getItemFromBlock(Blocks.LADDER))
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
            {
                return 100;
            }
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            {
                return 16000;
            }
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()))
            {
                return 200;
            }
            else if (item == Items.STICK)
            {
                return 100;
            }
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN)
                {
                    return 200;
                }
                else if (item == Items.COAL)
                {
                    return 1600;
                }
                else if (item == Items.LAVA_BUCKET)
                {
                    return 20000;
                }
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD)
                    {
                        return 2400;
                    }
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR)
                    {
                        return 200;
                    }
                    else
                    {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                }
                else
                {
                    return 100;
                }
            }
            else
            {
                return 300;
            }
        }
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
		if (index == 3)
        {
            return false;
        }
        else if (index != 2)
        {
            return true;
        }
        else
        {
            return stack.getItem().equals(ItemsInit.filter);
        }
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerTitaneExtractor(playerInventory, this);
    }

	@Override
	public int getField(int id)
	{
		switch (id)
        {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.extractTime;
            case 3:
                return this.totalExtractTime;
            default:
                return 0;
        }
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
        {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.extractTime = value;
                break;
            case 3:
                this.totalExtractTime = value;
        }
	}

	@Override
	public int getFieldCount()
	{
		return 4;
	}

	@Override
	public void clear()
	{
		this.titaneExtactorItemStacks.clear();
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }
}
