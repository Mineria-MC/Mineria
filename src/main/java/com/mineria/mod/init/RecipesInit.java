package com.mineria.mod.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class RecipesInit
{
	public static void registerFurnaceRecipes()
	{
		GameRegistry.addSmelting(ItemsInit.TITANE_INGOT, new ItemStack(ItemsInit.VANADIUM_INGOT, 1), 3.0F);
		GameRegistry.addSmelting(BlocksInit.TITANE_ORE, new ItemStack(ItemsInit.TITANE_INGOT, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.COPPER_ORE, new ItemStack(ItemsInit.COPPER_INGOT, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.SILVER_ORE, new ItemStack(ItemsInit.SILVER_INGOT, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.LEAD_ORE, new ItemStack(ItemsInit.LEAD_INGOT, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.NETHER_GOLD_ORE, new ItemStack(Items.GOLD_INGOT, 1), 1.0F);
		GameRegistry.addSmelting(BlocksInit.LONSDALEITE_ORE, new ItemStack(ItemsInit.LONSDALEITE, 1), 10.0F);
	}
}
