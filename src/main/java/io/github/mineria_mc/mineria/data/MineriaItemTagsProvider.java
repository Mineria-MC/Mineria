package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.*;
import static io.github.mineria_mc.mineria.common.init.MineriaItems.*;
import static io.github.mineria_mc.mineria.common.init.MineriaItems.Tags.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraftforge.common.Tags.Items.*;

public class MineriaItemTagsProvider extends ItemTagsProvider {
    public MineriaItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, MineriaBlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagsProvider.contentsGetter(), Mineria.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
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
        addObjects(TOOLS_DAGGERS, TITANE_DAGGER, LONSDALEITE_DAGGER);
        addObjects(TOOLS_DOUBLE_AXES, TITANE_DOUBLE_AXE, LONSDALEITE_DOUBLE_AXE);

        // Minecraft
        addObjects(MUSIC_DISCS, MUSIC_DISC_PIPPIN_THE_HUNCHBACK);
        addObjects(SWORDS, COPPER_SWORD, LEAD_SWORD, COMPRESSED_LEAD_SWORD, SILVER_SWORD, TITANE_SWORD, TITANE_SWORD_WITH_COPPER_HANDLE, TITANE_SWORD_WITH_IRON_HANDLE,
                TITANE_SWORD_WITH_SILVER_HANDLE, TITANE_SWORD_WITH_GOLD_HANDLE, LONSDALEITE_SWORD);
        addObjects(PICKAXES, SILVER_PICKAXE, TITANE_PICKAXE, LONSDALEITE_PICKAXE);
        addObjects(AXES, SILVER_AXE, TITANE_AXE, LONSDALEITE_AXE);
        addObjects(SHOVELS, SILVER_SHOVEL, TITANE_SHOVEL, LONSDALEITE_SHOVEL);
        addObjects(HOES, SILVER_HOE, TITANE_HOE, LONSDALEITE_HOE);

        // Forge
        tag(MUSHROOMS).add(getItemFromBlock(GIROLLE.get()), getItemFromBlock(HORN_OF_PLENTY.get()), getItemFromBlock(PUFFBALL.get()));
        addObjects(NUGGETS, LEAD_NUGGET, SILVER_NUGGET, TITANE_NUGGET);
        addObjects(RODS, GOLD_STICK, IRON_STICK, COPPER_STICK, SILVER_STICK);
        addObjects(INGOTS, LEAD_INGOT, SILVER_INGOT, TITANE_INGOT, VANADIUM_INGOT);
        addObjects(GEMS, LONSDALEITE);
        addObjects(TOOLS_BOWS, COPPER_BOW);
        tag(Tags.Items.TOOLS).addTags(TOOLS_DAGGERS, TOOLS_DOUBLE_AXES);
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
