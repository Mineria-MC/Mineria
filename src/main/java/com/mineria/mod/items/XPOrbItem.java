package com.mineria.mod.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class XPOrbItem extends Item
{
	private final int xpValue;

	public XPOrbItem(int xpValue, Item.Properties properties)
	{
		super(properties.maxStackSize(16));
		this.xpValue = xpValue;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (player.abilities.isCreativeMode)
		{
			player.giveExperiencePoints(xpValue);
			return new ActionResult<>(ActionResultType.CONSUME, stack);
		}
		else
		{
			player.giveExperiencePoints(xpValue);
			stack.shrink(1);
			return new ActionResult<>(ActionResultType.CONSUME, stack);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return this.xpValue == 64;
	}
}
