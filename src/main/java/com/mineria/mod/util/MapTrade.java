package com.mineria.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

/**
 * An {@link net.minecraft.entity.merchant.villager.VillagerTrades.ITrade} implementation for maps that locate structures.
 */
public class MapTrade implements VillagerTrades.ITrade
{
    private final ItemStack price;
    private final ItemStack price2;
    private final Structure<?> structure;
    private final MapDecoration.Type decorationType;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMult;

    public MapTrade(ItemStack price, ItemStack price2, Structure<?> structure, MapDecoration.Type decorationType, int maxUses, int villagerXp, float priceMult)
    {
        this.price = price;
        this.price2 = price2;
        this.structure = structure;
        this.decorationType = decorationType;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMult = priceMult;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, Random rand)
    {
        if(entity.level instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld) entity.level;
            BlockPos pos = world.findNearestMapFeature(this.structure, entity.blockPosition(), 100, true);
            if (pos != null)
            {
                ItemStack map = FilledMapItem.create(world, pos.getX(), pos.getZ(), (byte) 2, true, true);
                FilledMapItem.renderBiomePreviewMap(world, map);
                MapData.addTargetDecoration(map, pos, "+", this.decorationType);
                map.setHoverName(new TranslationTextComponent("filled_map." + this.structure.getFeatureName().toLowerCase(Locale.ROOT)));
                return new MerchantOffer(this.price, this.price2, map, this.maxUses, this.villagerXp, this.priceMult);
            }
        }
        return null;
    }
}
