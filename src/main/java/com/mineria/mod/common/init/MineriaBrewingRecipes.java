package com.mineria.mod.common.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.Util;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class MineriaBrewingRecipes
{
    public static void register()
    {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Util.make(new ItemStack(Items.POTION), stack -> PotionUtils.setPotion(stack, Potions.WATER))), Ingredient.of(Items.SUGAR), new ItemStack(MineriaItems.SYRUP));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Util.make(new ItemStack(Items.POTION), stack -> PotionUtils.setPotion(stack, Potions.WATER))), Ingredient.of(MineriaItems.ORANGE_BLOSSOM), new ItemStack(MineriaItems.DISTILLED_ORANGE_BLOSSOM_WATER));
        registerPotionMix(Potions.AWKWARD, MineriaItems.DRUID_HEART, MineriaPotions.VAMPIRE.get());
        registerPotionMix(MineriaPotions.VAMPIRE.get(), Items.REDSTONE, MineriaPotions.LONG_VAMPIRE.get());
        registerPotionMix(MineriaPotions.VAMPIRE.get(), Items.GLOWSTONE_DUST, MineriaPotions.STRONG_VAMPIRE.get());
    }

    private static void registerPotionMix(Potion input, Item ingredient, Potion output)
    {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotionItem(input, Items.POTION, MineriaItems.MINERIA_POTION)), Ingredient.of(ingredient), makePotionItem(output, Items.POTION, MineriaItems.MINERIA_POTION));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotionItem(input, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION)), Ingredient.of(ingredient), makePotionItem(output, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotionItem(input, Items.LINGERING_POTION, MineriaItems.MINERIA_LINGERING_POTION)), Ingredient.of(ingredient), makePotionItem(output, Items.LINGERING_POTION, MineriaItems.MINERIA_LINGERING_POTION));
        registerContainerMix(output);
    }

    private static void registerContainerMix(Potion potion)
    {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotionItem(potion, Items.POTION, MineriaItems.MINERIA_POTION)), Ingredient.of(Items.GUNPOWDER), makePotionItem(potion, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotionItem(potion, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION)), Ingredient.of(Items.DRAGON_BREATH), makePotionItem(potion, Items.LINGERING_POTION, MineriaItems.MINERIA_LINGERING_POTION));
    }

    private static ItemStack makePotionItem(Potion potion, Item vanillaVariant, Item mineriaVariant)
    {
        ItemStack potionStack = potion.getRegistryName().getNamespace().equals("mineria") ? new ItemStack(mineriaVariant) : new ItemStack(vanillaVariant);
        PotionUtils.setPotion(potionStack, potion);
        return potionStack;
    }
}
