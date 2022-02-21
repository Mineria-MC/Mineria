package com.mineria.mod.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * An {@link net.minecraft.entity.merchant.villager.VillagerTrades.ITrade} implementation for prices that are Ingredients. <br/>
 * These get resolved to a random stack when {@link net.minecraft.entity.merchant.villager.VillagerTrades.ITrade#getOffer(Entity, Random)} is called.
 */
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
        ItemStack price = getRandomItemStack(this.price.getFirst(), rand);
        price.setCount(this.price.getSecond());

        ItemStack price2 = getRandomItemStack(this.price2.getFirst(), rand);
        price2.setCount(this.price2.getSecond());

        return new MerchantOffer(price, price2, forSale, maxTrades, xp, priceMult);
    }

    private ItemStack getRandomItemStack(Ingredient ingredient, Random random)
    {
        if(ingredient.isEmpty()) return ItemStack.EMPTY;
        ItemStack[] items = ingredient.getItems();
        return items.length > 1 ? items[random.nextInt(items.length)] : items[0];
    }
}
