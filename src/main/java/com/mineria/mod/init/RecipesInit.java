package com.mineria.mod.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipesInit
{
	public static void init()
	{
		GameRegistry.addSmelting(ItemsInit.titane_ingot, new ItemStack(ItemsInit.vanadium_ingot, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.titane_ore, new ItemStack(ItemsInit.titane_ingot, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.copper_ore, new ItemStack(ItemsInit.copper_ingot, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.silver_ore, new ItemStack(ItemsInit.silver_ingot, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.lead_ore, new ItemStack(ItemsInit.lead_ingot, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.nether_gold_ore, new ItemStack(Items.GOLD_INGOT, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.lonsdaleite_ore, new ItemStack(ItemsInit.lonsdaleite, 1), 1.0F);
	}
}
