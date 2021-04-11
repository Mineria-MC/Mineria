package com.mineria.mod.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemXPOrb extends ItemMineria
{
	private final int amount;

	public ItemXPOrb(int amount)
	{
		super(new Builder().setMaxStackSize(16));
		this.amount = amount;
	}

	private TextFormatting getItemNameColor()
	{
		return this.amount == 64 ? TextFormatting.LIGHT_PURPLE : this.amount == 16 ? TextFormatting.AQUA : this.amount == 4 ? TextFormatting.YELLOW : TextFormatting.WHITE;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return getItemNameColor() + super.getItemStackDisplayName(stack);
	}

	@Override
	public Item setUnlocalizedName(String unlocalizedName)
	{
		return super.setUnlocalizedName("mineria_" + unlocalizedName);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return this.amount == 64;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		player.addExperience(amount);
		if(!player.capabilities.isCreativeMode) stack.shrink(1);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
}
