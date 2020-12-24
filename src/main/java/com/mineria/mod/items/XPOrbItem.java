package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class XPOrbItem extends Item
{
	public XPOrbItem(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		this.setMaxStackSize(16);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if ((player).capabilities.isCreativeMode)
		{
			(player).addExperience(1);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		else
		{
			(player).addExperience(1);
			stack.shrink(1);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
	}
}
