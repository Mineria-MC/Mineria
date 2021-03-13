package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class XPOrbItem extends Item
{
	public XPOrbItem()
	{
		super(new Item.Properties().group(Mineria.MINERIA_GROUP).maxStackSize(16));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (player.abilities.isCreativeMode)
		{
			player.giveExperiencePoints(1);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}
		else
		{
			player.giveExperiencePoints(1);
			stack.shrink(1);
			return new ActionResult<>(ActionResultType.CONSUME, stack);
		}
	}
}
