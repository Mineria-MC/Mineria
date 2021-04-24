package com.mineria.mod.blocks.extractor;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityExtractor extends TileEntity implements ITickable
{
	public static final Map<Integer, ItemStack> RECIPE_OUTPUTS = MineriaUtils.make(new HashMap<>(), map -> {
		map.put(800, new ItemStack(Items.IRON_INGOT));
		map.put(600, new ItemStack(ItemsInit.LEAD_INGOT));
		map.put(300, new ItemStack(ItemsInit.COPPER_INGOT));
		map.put(120, new ItemStack(ItemsInit.SILVER_INGOT));
		map.put(100, new ItemStack(Items.GOLD_INGOT));
		map.put(10, new ItemStack(Items.DIAMOND));
		map.put(1, new ItemStack(ItemsInit.LONSDALEITE));
	});

	private final CustomItemStackHandler handler = new CustomItemStackHandler(10);

	private String customName;
	private int extractTime;
	private int totalExtractTime = 200;
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
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
		ItemStack input = this.handler.getStackInSlot(0);
		ItemStack barrel = this.handler.getStackInSlot(1);
		ItemStack filter = this.handler.getStackInSlot(2);
		boolean hasWater = AbstractTileEntityWaterBarrel.checkWaterFromStack(barrel);

		if (input.isEmpty() || barrel.isEmpty() || filter.isEmpty() || !hasWater)
		{
			return false;
		}
		else
		{
			Map<Integer, ItemStack> outputs = RECIPE_OUTPUTS;

			for(int index = 3; index < this.handler.getSlots(); index++)
			{
				ItemStack output = this.handler.getStackInSlot(index);
				ItemStack result = getOutputStackFromSlot(index, outputs);

				if(output.isEmpty())
				{
					continue;
				}
				else if (!output.isItemEqual(result))
				{
					return false;
				}

				int res = output.getCount() + result.getCount();
				if(!(res <= 64 && res <= output.getMaxStackSize()))
				{
					return false;
				}
			}
			return true;
		}
	}

	private void extractItem()
	{
		if (this.canExtract())
		{
			ItemStack input = this.handler.getStackInSlot(0);
			ItemStack barrel = this.handler.getStackInSlot(1);
			ItemStack filter = this.handler.getStackInSlot(2);
			Map<Integer, ItemStack> outputs = RECIPE_OUTPUTS;

			final int multiplier = 2;

			for(int i = 0; i < multiplier; i++)
			{
				for(Map.Entry<Integer, ItemStack> entry : outputs.entrySet())
				{
					Random rand = new Random();
					int chance = rand.nextInt(1000);

					if (chance <= entry.getKey())
					{
						ItemStack result = entry.getValue();
						ItemStack output = this.handler.getStackInSlot(getOutputIndexFromStack(result, outputs));

						if(output.getCount() + result.getCount() > 64)
						{
							continue;
						}
						if (output.isEmpty())
						{
							this.handler.setStackInSlot(getOutputIndexFromStack(result, outputs), result.copy());
						}
						else if (output.getItem() == result.getItem())
						{
							output.grow(result.getCount());
						}
					}
				}
			}

			input.shrink(1);
			AbstractTileEntityWaterBarrel.decreaseFluidFromStack(barrel);
			filter.shrink(1);
		}
	}

	private static ItemStack getOutputStackFromSlot(int slotId, Map<Integer, ItemStack> map)
	{
        List<Integer> ints = new ArrayList<>(map.keySet());
        ints.sort(Comparator.reverseOrder());
        return map.get(ints.get(slotId - 3));
	}

	private static int getOutputIndexFromStack(ItemStack stack, Map<Integer, ItemStack> map)
	{
        List<Integer> ints = new ArrayList<>(map.keySet());
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
