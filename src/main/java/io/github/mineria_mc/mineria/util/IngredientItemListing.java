package io.github.mineria_mc.mineria.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An {@link net.minecraft.world.entity.npc.VillagerTrades.ItemListing} implementation for prices that are Ingredients. <br/>
 * These get resolved to a random stack when {@link net.minecraft.world.entity.npc.VillagerTrades.ItemListing#getOffer(Entity, RandomSource)} is called.
 */
public class IngredientItemListing implements VillagerTrades.ItemListing {
    protected final Pair<Ingredient, Integer> price;
    protected final Pair<Ingredient, Integer> price2;
    protected final ItemStack forSale;
    protected final int maxTrades;
    protected final int xp;
    protected final float priceMultiplier;

    public IngredientItemListing(Pair<Ingredient, Integer> price, Pair<Ingredient, Integer> price2, ItemStack forSale, int maxTrades, int xp, float priceMultiplier) {
        this.price = price;
        this.price2 = price2;
        this.forSale = forSale;
        this.maxTrades = maxTrades;
        this.xp = xp;
        this.priceMultiplier = priceMultiplier;
    }

    public IngredientItemListing(Pair<Ingredient, Integer> price, ItemStack forSale, int maxTrades, int xp, float priceMultiplier) {
        this(price, Pair.of(Ingredient.EMPTY, 0), forSale, maxTrades, xp, priceMultiplier);
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
        ItemStack price = getRandomItemStack(this.price.getFirst(), rand);
        price.setCount(this.price.getSecond());

        ItemStack price2 = getRandomItemStack(this.price2.getFirst(), rand);
        price2.setCount(this.price2.getSecond());

        return new MerchantOffer(price, price2, forSale, maxTrades, xp, priceMultiplier);
    }

    private ItemStack getRandomItemStack(Ingredient ingredient, RandomSource random) {
        if (ingredient.isEmpty()) return ItemStack.EMPTY;
        ItemStack[] items = ingredient.getItems();
        return items.length > 1 ? items[random.nextInt(items.length)] : items[0];
    }
}
