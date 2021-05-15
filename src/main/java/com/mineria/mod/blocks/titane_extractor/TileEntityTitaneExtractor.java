package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityTitaneExtractor extends TileEntity implements ITickable
{
	private final CustomItemStackHandler inventory = new CustomItemStackHandler(4);

	private int extractTime;
    private int totalExtractTime = 200;
    private String customName;

    public boolean hasCustomName()
    {
        return this.customName != null && !customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }
	
	@Override
	public ITextComponent getDisplayName()
	{
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.titane_extractor");
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
        this.extractTime = compound.getInteger("ExtractTime");
        this.totalExtractTime = compound.getInteger("ExtractTimeTotal");

        if (compound.hasKey("CustomName", 8))
        {
            this.setCustomName(compound.getString("CustomName"));
        }
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("ExtractTime", this.extractTime);
        compound.setInteger("ExtractTimeTotal", this.totalExtractTime);
        compound.setTag("Inventory", this.inventory.serializeNBT());

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }
	
	public boolean isExtracting()
    {
        return this.extractTime > 0;
    }
	
	@SideOnly(Side.CLIENT)
    public static boolean isExtracting(TileEntityTitaneExtractor inventory)
    {
        return inventory.getField(0) > 0;
    }
	
	@Override
	public void update()
	{
        boolean alreadyExtracting = this.isExtracting();
        boolean dirty = false;

        if (!this.world.isRemote)
        {
            if (this.canExtract())
            {
                ++this.extractTime;

                if (this.extractTime == this.totalExtractTime)
                {
                    this.extractTime = 0;
                    this.extractItem();
                    dirty = true;
                }
            }
            if (!this.canExtract() && this.extractTime > 0)
            {
                this.extractTime = MathHelper.clamp(this.extractTime - 2, 0, this.totalExtractTime);
            }

            if (alreadyExtracting != this.isExtracting())
            {
                dirty = true;
                BlockTitaneExtractor.setState(this.isExtracting(), this.world, this.pos);
            }
        }

        if (dirty)
        {
            this.markDirty();
        }
	}
	
	private boolean canExtract()
	{
	    ItemStack input = this.inventory.getStackInSlot(0);
        ItemStack waterSource = this.inventory.getStackInSlot(1);
        ItemStack filter = this.inventory.getStackInSlot(2);
        boolean hasWater = AbstractTileEntityWaterBarrel.checkWaterFromStack(waterSource);

		if (input.isEmpty() || waterSource.isEmpty() || filter.isEmpty() || !hasWater)
		{
			return false;
		}
		else
		{
			ItemStack result = new ItemStack(ItemsInit.TITANE_NUGGET);
            ItemStack output = this.inventory.getStackInSlot(3);

            if (output.isEmpty())
            {
                return true;
            }
            else if (!output.isItemEqual(result))
            {
                return false;
            }
            int res = output.getCount() + result.getCount();
            return res <= 64 && res <= output.getMaxStackSize();
		}
	}
	 
	private void extractItem()
	{
		if (this.canExtract())
		{
			ItemStack input = this.inventory.getStackInSlot(0);
			ItemStack waterSource = this.inventory.getStackInSlot(1);
            ItemStack filter = this.inventory.getStackInSlot(2);
			ItemStack output = this.inventory.getStackInSlot(3);
            ItemStack result = new ItemStack(ItemsInit.TITANE_NUGGET);

			if (output.isEmpty())
			{
				this.inventory.setStackInSlot(3, result.copy());
			}
			else if (output.getItem() == result.getItem())
			{
				output.grow(result.getCount());
			}

			input.shrink(1);
            this.inventory.setStackInSlot(1, AbstractTileEntityWaterBarrel.decreaseFluidFromStack(waterSource));
            filter.shrink(1);
		}
	}

	public boolean isUsableByPlayer(EntityPlayer player)
	{
	    return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public int getField(int id)
	{
		switch (id)
        {
            case 0:
                return this.extractTime;
            case 1:
                return this.totalExtractTime;
            default:
                return 0;
        }
	}

	public void setField(int id, int value)
	{
		switch (id)
        {
            case 0:
                this.extractTime = value;
                break;
            case 1:
                this.totalExtractTime = value;
        }
	}

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.inventory;
        return super.getCapability(capability, facing);
    }

    public CustomItemStackHandler getInventory()
    {
        return inventory;
    }
}
