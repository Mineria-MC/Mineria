package com.mineria.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Random;

public class IngredientTrade implements VillagerTrades.ITrade
{
    protected final Pair<Ingredient, Integer> price;
    protected final Pair<Ingredient, Integer> price2;
    protected final ItemStack forSale;
    protected final int maxTrades;
    protected final int xp;
    protected final float priceMult;

    public IngredientTrade(Pair<Ingredient, Integer> price, Pair<Ingredient, Integer> price2, ItemStack forSale, int maxTrades, int xp, float priceMult)
    {
        this.price = price;
        this.price2 = price2;
        this.forSale = forSale;
        this.maxTrades = maxTrades;
        this.xp = xp;
        this.priceMult = priceMult;
    }

    public IngredientTrade(Pair<Ingredient, Integer> price, ItemStack forSale, int maxTrades, int xp, float priceMult)
    {
        this(price, Pair.of(Ingredient.EMPTY, 0), forSale, maxTrades, xp, priceMult);
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, Random rand)
    {
        ItemStack price = getRandomItemStack(this.price.getLeft(), rand);
        price.setCount(this.price.getRight());

        ItemStack price2 = getRandomItemStack(this.price2.getLeft(), rand);
        price2.setCount(this.price2.getRight());

        return new MerchantOffer(price, price2, forSale, maxTrades, xp, priceMult);
    }

    private ItemStack getRandomItemStack(Ingredient ingredient, Random random)
    {
        if(ingredient.hasNoMatchingItems())
            return ItemStack.EMPTY;

        if(ingredient.getMatchingStacks().length > 1)
            return ingredient.getMatchingStacks()[random.nextInt(ingredient.getMatchingStacks().length)];
        else
            return ingredient.getMatchingStacks()[0];
    }
}
