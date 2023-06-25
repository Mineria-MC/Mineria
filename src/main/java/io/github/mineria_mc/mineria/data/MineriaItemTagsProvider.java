package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.*;
import static io.github.mineria_mc.mineria.common.init.MineriaItems.*;
import static io.github.mineria_mc.mineria.common.init.MineriaItems.Tags.*;
import static net.minecraft.tags.ItemTags.MUSIC_DISCS;
import static net.minecraftforge.common.Tags.Items.*;

public class MineriaItemTagsProvider extends ItemTagsProvider {
    public MineriaItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, MineriaBlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagsProvider.contentsGetter(), Mineria.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        // Minecraft
        addObjects(MUSIC_DISCS, MUSIC_DISC_PIPPIN_THE_HUNCHBACK);

        // Forge
        tag(MUSHROOMS).add(getItemFromBlock(GIROLLE.get()), getItemFromBlock(HORN_OF_PLENTY.get()), getItemFromBlock(PUFFBALL.get()));
        addObjects(NUGGETS, LEAD_NUGGET, SILVER_NUGGET, TITANE_NUGGET);
        addObjects(RODS, GOLD_STICK, IRON_STICK, COPPER_STICK, SILVER_STICK);
        addObjects(INGOTS, LEAD_INGOT, SILVER_INGOT, TITANE_INGOT, VANADIUM_INGOT);
        addObjects(GEMS, LONSDALEITE);

        // Mineria
        copy(MineriaBlocks.Tags.LEAD_ORES, LEAD_ORES);
        copy(MineriaBlocks.Tags.SILVER_ORES, SILVER_ORES);
        copy(MineriaBlocks.Tags.TITANE_ORES, TITANE_ORES);
        copy(MineriaBlocks.Tags.LONSDALEITE_ORES, LONSDALEITE_ORES);
        copy(MineriaBlocks.Tags.PLANTS, PLANTS);
        copy(MineriaBlocks.Tags.ALLOWED_BLOCKS_RITUAL_TABLE, ALLOWED_BLOCKS_RITUAL_TABLE);

        addObjects(ANTI_POISONS, CATHOLICON, CHARCOAL_ANTI_POISON, MILK_ANTI_POISON, NAUSEOUS_ANTI_POISON, ANTI_POISON, MIRACLE_ANTI_POISON);
        addObjects(LAXATIVE_DRINKS, CHARCOAL_ANTI_POISON, CATHOLICON, RHUBARB_TEA, SENNA_TEA);
        addObjects(POISONOUS_TEAS, ELDERBERRY_TEA, STRYCHNOS_TOXIFERA_TEA, STRYCHNOS_NUX_VOMICA_TEA, BELLADONNA_TEA, MANDRAKE_TEA);
        addObjects(TEAS, PLANTAIN_TEA, MINT_TEA, THYME_TEA, NETTLE_TEA, PULMONARY_TEA, RHUBARB_TEA, SENNA_TEA, BLACK_ELDERBERRY_TEA, ELDERBERRY_TEA, STRYCHNOS_TOXIFERA_TEA, STRYCHNOS_NUX_VOMICA_TEA, BELLADONNA_TEA, MANDRAKE_TEA, MANDRAKE_ROOT_TEA, GOJI_TEA, SAUSSUREA_COSTUS_ROOT_TEA, FIVE_FLAVOR_FRUIT_TEA, PULSATILLA_CHINENSIS_ROOT_TEA);
    }

    @SafeVarargs
    private void addObjects(TagKey<Item> tag, RegistryObject<Item>... items) {
        IntrinsicTagAppender<Item> appender = tag(tag);
        for(RegistryObject<Item> item : items) {
            appender.add(item.get());
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Mineria Item Tags";
    }
}
