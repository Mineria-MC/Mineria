package com.mineria.mod.blocks.extractor;

import com.mineria.mod.util.handler.CustomItemStackHandler;
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

import java.util.*;

public class TileEntityExtractor extends TileEntity implements ITickable
{
	private final CustomItemStackHandler handler = new CustomItemStackHandler(10);

	private String customName;
	private int extractTime;
	private int totalExtractTime = 200;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.handler;
		return super.getCapability(capability, facing);
	}
	
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
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.extractor");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.handler.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.extractTime = compound.getInteger("ExtractTime");
		this.totalExtractTime = compound.getInteger("TotalExtractTime");
		
		if(compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("ExtractTime", (short)this.extractTime);
		compound.setInteger("TotalExtractTime", (short)this.totalExtractTime);
		compound.setTag("Inventory", this.handler.serializeNBT());
		
		if(this.hasCustomName()) compound.setString("CustomName", this.customName);
		return compound;
	}
	
	public boolean isExtracting()
	{
		return this.extractTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isExtracting(TileEntityExtractor te)
	{
		return te.getField(0) > 0;
	}
	
	@Override
	public void update()
	{
		boolean flag = this.isExtracting();
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

			if (flag != this.isExtracting())
			{
				dirty = true;
				BlockExtractor.setState(this.isExtracting(), this.world, this.pos);
			}
		}

		if (dirty)
		{
			this.markDirty();
		}
	}
	
	private boolean canExtract()
	{
		ItemStack stack = (this.handler.getStackInSlot(1));
		NBTTagCompound compound = stack.getTagCompound();
		if(compound == null || !compound.hasKey("BlockEntityTag", 10))
		{
			return false;
		}
		NBTTagCompound compound1 = compound.getCompoundTag("BlockEntityTag");

		if ((this.handler.getStackInSlot(0)).isEmpty() || stack.isEmpty() || (this.handler.getStackInSlot(2)).isEmpty() || compound1.getInteger("Water") == 0)
		{
			return false;
		}
		else
		{
			Map<Integer, ItemStack> outputs = ExtractorRecipes.getInstance().getExtractingResult(this.handler.getStackInSlot(0), this.handler.getStackInSlot(1));

			if(outputs.isEmpty())
			{
				return false;
			}
			else
			{
				for(int i = 3; i < this.handler.getSlots(); i++)
				{
					ItemStack stack1 = this.handler.getStackInSlot(i);
					ItemStack output = this.getOutputStackFromSlot(i, outputs);

					if(stack1.isEmpty())
					{
						continue;
					}
					else if (!stack1.isItemEqual(output))
					{
						return false;
					}

					int res = stack1.getCount() + output.getCount();
					if(!(res <= 64 && res <= stack1.getMaxStackSize()))
					{
						return false;
					}
				}
				return true;
			}
		}
	}

	private void extractItem()
	{
		if (this.canExtract())
		{
			ItemStack stack = this.handler.getStackInSlot(0);
			ItemStack stack1 = this.handler.getStackInSlot(1);
			Map<Integer, ItemStack> outputs = ExtractorRecipes.getInstance().getExtractingResult(stack, stack1);

			for(int i = 0; i < 2; i++)
			{
				for(Map.Entry<Integer, ItemStack> entry : outputs.entrySet())
				{
					Random rand = new Random();
					int chance = rand.nextInt(1000);

					if (chance <= entry.getKey())
					{
						ItemStack output = entry.getValue();
						ItemStack stack3 = this.handler.getStackInSlot(this.getOutputIndexFromStack(output, outputs));

						if(stack3.getCount() + output.getCount() > 64)
						{
							continue;
						}
						if (stack3.isEmpty())
						{
							this.handler.setStackInSlot(this.getOutputIndexFromStack(output, outputs), output.copy());
						}
						else if (stack3.getItem() == output.getItem())
						{
							stack3.grow(output.getCount());
						}
					}
				}
			}

			//stack.shrink(1);
			//NBTTagCompound compound = stack1.getTagCompound();
			//NBTTagCompound compound1 = compound.getCompoundTag("BlockEntityTag");
			//compound1.setInteger("Water", compound1.getInteger("Water") - 1);
			//stack2.shrink(1);
		}
	}

	private ItemStack getOutputStackFromSlot(int slotId, Map<Integer, ItemStack> map)
	{
        List<Integer> ints = new ArrayList<>();
        map.forEach((key, value) -> ints.add(key));
        ints.sort(Comparator.reverseOrder());
        return map.get(ints.get(slotId - 3));
	}

	private int getOutputIndexFromStack(ItemStack stack, Map<Integer, ItemStack> map)
	{
        List<Integer> ints = new ArrayList<>();
        map.forEach((key, value) -> ints.add(key));
        ints.sort(Comparator.reverseOrder());
        for(Map.Entry<Integer, ItemStack> entry : map.entrySet())
        {
            if(entry.getValue().isItemEqual(stack))
            {
                return ints.indexOf(entry.getKey()) + 3;
            }
        }
        return -1;
	}
	
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	public int getField(int id)
	{
		switch(id)
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
		switch(id) 
		{
		case 0:
			this.extractTime = value;
			break;
		case 1:
			this.totalExtractTime = value;
		}
	}

	public CustomItemStackHandler getInventory()
	{
		return this.handler;
	}
}
