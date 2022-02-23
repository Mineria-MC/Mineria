package com.mineria.mod.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

/**
 * An {@link net.minecraft.world.entity.npc.VillagerTrades.ItemListing} implementation for maps that locate structures.
 */
public class MapTrade implements VillagerTrades.ItemListing
{
    private final ItemStack price;
    private final ItemStack price2;
    private final StructureFeature<?> structure;
    private final MapDecoration.Type decorationType;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMult;

    public MapTrade(ItemStack price, ItemStack price2, StructureFeature<?> structure, MapDecoration.Type decorationType, int maxUses, int villagerXp, float priceMult)
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
        if(entity.level instanceof ServerLevel)
        {
            ServerLevel world = (ServerLevel) entity.level;
            BlockPos pos = world.findNearestMapFeature(this.structure, entity.blockPosition(), 100, true);
            if (pos != null)
            {
                ItemStack map = MapItem.create(world, pos.getX(), pos.getZ(), (byte) 2, true, true);
                MapItem.renderBiomePreviewMap(world, map);
                MapItemSavedData.addTargetDecoration(map, pos, "+", this.decorationType);
                map.setHoverName(new TranslatableComponent("filled_map." + this.structure.getFeatureName().toLowerCase(Locale.ROOT)));
                return new MerchantOffer(this.price, this.price2, map, this.maxUses, this.villagerXp, this.priceMult);
            }
        }
        return null;
    }
}
