package com.mineria.mod.common.items;

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
		super(properties.stacksTo(16));
		this.xpValue = xpValue;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (player.abilities.instabuild)
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
	public boolean isFoil(ItemStack stack)
	{
		return this.xpValue == 64;
	}
}
