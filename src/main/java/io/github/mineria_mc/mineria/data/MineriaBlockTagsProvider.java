package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.*;
import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.Tags.*;
import static net.minecraft.tags.BlockTags.*;

public class MineriaBlockTagsProvider extends BlockTagsProvider {
    public MineriaBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Mineria.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        // Minecraft
        addObjects(BEACON_BASE_BLOCKS, LEAD_BLOCK, TITANE_BLOCK, LONSDALEITE_BLOCK, SILVER_BLOCK, COMPRESSED_LEAD_BLOCK);
        addObjects(LEAVES, SPRUCE_YEW_LEAVES, SAKURA_LEAVES);
        addObjects(NEEDS_DIAMOND_TOOL, TITANE_ORE, LONSDALEITE_ORE, DEEPSLATE_TITANE_ORE, DEEPSLATE_LONSDALEITE_ORE, TITANE_BLOCK, LONSDALEITE_BLOCK, COMPRESSED_LEAD_BLOCK);
        addObjects(NEEDS_IRON_TOOL, LEAD_ORE, DEEPSLATE_LEAD_ORE, NETHER_GOLD_ORE, SILVER_ORE, DEEPSLATE_SILVER_ORE, LEAD_BLOCK, SILVER_BLOCK, EXTRACTOR, DISTILLER, LEAD_SPIKE, COMPRESSED_LEAD_SPIKE, GOLDEN_WATER_BARREL);
        addObjects(NEEDS_STONE_TOOL, XP_BLOCK, INFUSER, MINERAL_SAND, XP_WALL, COPPER_WATER_BARREL, IRON_FLUID_BARREL);
        addObjects(SAND, MINERAL_SAND);
        addObjects(SAPLINGS, SPRUCE_YEW_SAPLING, SAKURA_SAPLING);
        addObjects(MINEABLE_WITH_AXE, INFUSER, APOTHECARY_TABLE, WATER_BARREL, INFINITE_WATER_BARREL, COPPER_WATER_BARREL, IRON_FLUID_BARREL, GOLDEN_WATER_BARREL, TNT_BARREL);
        addObjects(MINEABLE_WITH_HOE, SPRUCE_YEW_LEAVES, SAKURA_LEAVES, ELDERBERRY_BUSH, BLACK_ELDERBERRY_BUSH, LYCIUM_CHINENSE, PUFFBALL_POWDER);
        addObjects(MINEABLE_WITH_PICKAXE, LEAD_ORE, TITANE_ORE, LONSDALEITE_ORE, NETHER_GOLD_ORE, SILVER_ORE, DEEPSLATE_LEAD_ORE, DEEPSLATE_TITANE_ORE, DEEPSLATE_LONSDALEITE_ORE, DEEPSLATE_SILVER_ORE,
                LEAD_BLOCK, TITANE_BLOCK, LONSDALEITE_BLOCK, SILVER_BLOCK, COMPRESSED_LEAD_BLOCK, XP_BLOCK, TITANE_EXTRACTOR, DISTILLER, EXTRACTOR, LEAD_SPIKE, COMPRESSED_LEAD_SPIKE, XP_WALL);
        addObjects(MINEABLE_WITH_SHOVEL, MINERAL_SAND);

        // Forge
        addObjects(Tags.Blocks.ORES, LEAD_ORE, TITANE_ORE, LONSDALEITE_ORE, NETHER_GOLD_ORE, SILVER_ORE, DEEPSLATE_LEAD_ORE, DEEPSLATE_TITANE_ORE, DEEPSLATE_LONSDALEITE_ORE, DEEPSLATE_SILVER_ORE);
        addObjects(Tags.Blocks.STORAGE_BLOCKS, LEAD_BLOCK, TITANE_BLOCK, LONSDALEITE_BLOCK, SILVER_BLOCK, COMPRESSED_LEAD_BLOCK);
        addObjects(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, DEEPSLATE_LEAD_ORE, DEEPSLATE_TITANE_ORE, DEEPSLATE_LONSDALEITE_ORE, DEEPSLATE_SILVER_ORE);
        addObjects(Tags.Blocks.ORES_IN_GROUND_STONE, LEAD_ORE, TITANE_ORE, LONSDALEITE_ORE, SILVER_ORE);
        addObjects(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, NETHER_GOLD_ORE);

        // Mineria
        addObjects(LEAD_ORES, LEAD_ORE, DEEPSLATE_LEAD_ORE);
        addObjects(SILVER_ORES, SILVER_ORE, DEEPSLATE_SILVER_ORE);
        addObjects(TITANE_ORES, TITANE_ORE, DEEPSLATE_TITANE_ORE);
        addObjects(LONSDALEITE_ORES, LONSDALEITE_ORE, DEEPSLATE_LONSDALEITE_ORE);
        addObjects(ALLOWED_BLOCKS_RITUAL_TABLE, BLUE_GLOWSTONE).add(Blocks.TORCH, Blocks.SOUL_TORCH);
        addObjects(PLANTS, PLANTAIN, MINT, THYME, NETTLE, PULMONARY, RHUBARB, SENNA, SENNA_BUSH, ELDERBERRY_BUSH, BLACK_ELDERBERRY_BUSH, STRYCHNOS_TOXIFERA, STRYCHNOS_NUX_VOMICA, BELLADONNA, MANDRAKE, LYCIUM_CHINENSE, SAUSSUREA_COSTUS, SCHISANDRA_CHINENSIS, PULSATILLA_CHINENSIS);
        addObjects(MINEABLE_WITH_BILLHOOK, MANDRAKE, PULSATILLA_CHINENSIS, SAUSSUREA_COSTUS).add(Blocks.OAK_LEAVES);
    }

    @SafeVarargs
    private IntrinsicTagAppender<Block> addObjects(TagKey<Block> tag, RegistryObject<Block>... blocks) {
        IntrinsicTagAppender<Block> appender = tag(tag);
        for(RegistryObject<Block> block : blocks) {
            appender.add(block.get());
        }
        return appender;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Mineria Block Tags";
    }
}
