package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityXpBlock extends TileEntity
{
	private final CustomItemStackHandler inventory = new CustomItemStackHandler(1);

	private String customName;

	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.xp_block");
	}

	public boolean hasCustomName()
	{
		return this.customName != null && !this.customName.isEmpty();
	}
	
	public void setCustomName(String name)
    {
        this.customName = name;
    }

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.inventory;
		return super.getCapability(capability, facing);
	}

	public CustomItemStackHandler getInventory()
	{
		return inventory;
	}

	private void spawnXp(EntityPlayer player)
	{
		ItemStack result = new ItemStack(ItemsInit.XP_ORB);
		ItemStack output = this.inventory.getStackInSlot(0);
		if (output.getCount() < 16)
		{
			if(player.capabilities.isCreativeMode)
			{
				if (output.isEmpty())
				{
					this.inventory.setStackInSlot(0, result.copy());
				}
				else if (output.getItem() == result.getItem())
				{
					output.grow(1);
				}
			}
			else if (player.experienceTotal > 0)
			{
				giveExperiencePoints(-1, player);
				if (output.isEmpty())
				{
					this.inventory.setStackInSlot(0, result.copy());
				}
				else if (output.getItem() == result.getItem())
				{
					output.grow(1);
				}
			}
		}
	}

	private static void giveExperiencePoints(int amount, EntityPlayer player)
	{
		player.addScore(amount);
		player.experience += (float)amount / (float)player.xpBarCap();
		player.experienceTotal = MathHelper.clamp(player.experienceTotal + amount, 0, Integer.MAX_VALUE);

		while(player.experience < 0.0F)
		{
			float f = player.experience * (float)player.xpBarCap();
			if (player.experienceLevel > 0)
			{
				player.addExperienceLevel(-1);
				player.experience = 1.0F + f / (float)player.xpBarCap();
			}
			else
			{
				player.addExperienceLevel(-1);
				player.experience = 0.0F;
			}
		}

		while(player.experience >= 1.0F)
		{
			player.experience = (player.experience - 1.0F) * (float)player.xpBarCap();
			player.addExperienceLevel(1);
			player.experience /= (float)player.xpBarCap();
		}
	}

	public static void execute(int actionID, BlockPos pos, World world, EntityPlayer player)
	{
		if(actionID == 0)
		{
			TileEntity tile = world.getTileEntity(pos);

			if(tile instanceof TileEntityXpBlock)
				((TileEntityXpBlock)tile).spawnXp(player);
		}
	}
}
