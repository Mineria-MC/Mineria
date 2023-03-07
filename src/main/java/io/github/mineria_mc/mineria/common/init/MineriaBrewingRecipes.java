package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.util.DeferredRegisterUtil;
import net.minecraft.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class MineriaBrewingRecipes {
    public static void register() {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Util.make(new ItemStack(Items.POTION), stack -> PotionUtils.setPotion(stack, Potions.WATER))), Ingredient.of(Items.SUGAR), new ItemStack(MineriaItems.SYRUP.get()));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Util.make(new ItemStack(Items.POTION), stack -> PotionUtils.setPotion(stack, Potions.WATER))), Ingredient.of(MineriaItems.ORANGE_BLOSSOM.get()), new ItemStack(MineriaItems.DISTILLED_ORANGE_BLOSSOM_WATER.get()));
        registerPotionMix(Potions.AWKWARD, MineriaItems.DRUID_HEART.get(), MineriaPotions.VAMPIRE.get());
        registerPotionMix(MineriaPotions.VAMPIRE.get(), Items.REDSTONE, MineriaPotions.LONG_VAMPIRE.get());
        registerPotionMix(MineriaPotions.VAMPIRE.get(), Items.GLOWSTONE_DUST, MineriaPotions.STRONG_VAMPIRE.get());
    }

    private static void registerPotionMix(Potion input, Item ingredient, Potion output) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotion(input, Items.POTION, MineriaItems.MINERIA_POTION.get())), Ingredient.of(ingredient), makePotion(output, Items.POTION, MineriaItems.MINERIA_POTION.get()));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotion(input, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION.get())), Ingredient.of(ingredient), makePotion(output, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION.get()));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotion(input, Items.LINGERING_POTION, MineriaItems.MINERIA_LINGERING_POTION.get())), Ingredient.of(ingredient), makePotion(output, Items.LINGERING_POTION, MineriaItems.MINERIA_LINGERING_POTION.get()));
        registerContainerMix(output);
    }

    private static void registerContainerMix(Potion potion) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotion(potion, Items.POTION, MineriaItems.MINERIA_POTION.get())), Ingredient.of(Items.GUNPOWDER), makePotion(potion, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION.get()));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(makePotion(potion, Items.SPLASH_POTION, MineriaItems.MINERIA_SPLASH_POTION.get())), Ingredient.of(Items.DRAGON_BREATH), makePotion(potion, Items.LINGERING_POTION, MineriaItems.MINERIA_LINGERING_POTION.get()));
    }

    private static ItemStack makePotion(Potion potion, Item vanillaVariant, Item mineriaVariant) {
        ItemStack potionStack = DeferredRegisterUtil.contains(MineriaPotions.POTIONS, potion) ? new ItemStack(mineriaVariant) : new ItemStack(vanillaVariant);
        PotionUtils.setPotion(potionStack, potion);
        return potionStack;
    }
}
