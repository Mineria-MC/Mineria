package io.github.mineria_mc.mineria.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

/**
 * An {@link net.minecraft.world.entity.npc.VillagerTrades.ItemListing} implementation for maps that locate structures.
 */
public class MapTrade implements VillagerTrades.ItemListing {
    private final ItemStack price;
    private final ItemStack price2;
    private final TagKey<Structure> structure;
    private final MapDecoration.Type decorationType;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMultiplier;

    public MapTrade(ItemStack price, ItemStack price2, TagKey<Structure> structure, MapDecoration.Type decorationType, int maxUses, int villagerXp, float priceMultiplier) {
        this.price = price;
        this.price2 = price2;
        this.structure = structure;
        this.decorationType = decorationType;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMultiplier = priceMultiplier;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, @Nonnull RandomSource rand) {
        if (entity.level instanceof ServerLevel world) {
            BlockPos pos = world.findNearestMapStructure(this.structure, entity.blockPosition(), 100, true);
            if (pos != null) {
                ItemStack map = MapItem.create(world, pos.getX(), pos.getZ(), (byte) 2, true, true);
                MapItem.renderBiomePreviewMap(world, map);
                MapItemSavedData.addTargetDecoration(map, pos, "+", this.decorationType);
                map.setHoverName(Component.translatable("filled_map." + this.structure.location().getPath().toLowerCase(Locale.ROOT)));
                return new MerchantOffer(this.price, this.price2, map, this.maxUses, this.villagerXp, this.priceMultiplier);
            }
        }
        return null;
    }
}
