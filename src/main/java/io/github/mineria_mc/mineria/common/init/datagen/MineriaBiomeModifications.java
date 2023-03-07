package io.github.mineria_mc.mineria.common.init.datagen;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.StrychnosPlantBlock;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaFeatures;
import io.github.mineria_mc.mineria.common.world.biome.modifiers.CopyFeatureBiomeModifier;
import io.github.mineria_mc.mineria.common.world.feature.ModVinesFeatureConfig;
import io.github.mineria_mc.mineria.common.world.feature.SpruceYewTree;
import io.github.mineria_mc.mineria.common.world.gen.height_providers.AnyTriangleHeight;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static io.github.mineria_mc.mineria.common.init.datagen.MineriaBiomes.Tags.*;

public class MineriaBiomeModifications {
    private static final MineriaBootstrapEntries.Simple<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = new MineriaBootstrapEntries.Simple<>(Registries.CONFIGURED_FEATURE);
    private static final MineriaBootstrapEntries.Simple<PlacedFeature> PLACED_FEATURES = new MineriaBootstrapEntries.Simple<>(Registries.PLACED_FEATURE);
    private static final Map<GenerationStep.Decoration, Map<Either<ResourceKey<Biome>, TagKey<Biome>>, List<ResourceKey<PlacedFeature>>>> FEATURES_FOR_STEP = new HashMap<>();
    private static final Map<GenerationStep.Decoration, List<Pair<ResourceKey<PlacedFeature>, ResourceKey<PlacedFeature>>>> COPIED_FEATURES = new HashMap<>();

    public static void addAll() {
        addPlants();
        addOres();
        addYewTree();
    }

    private static void addPlants() {
        plant(MineriaBlocks.PLANTAIN, b -> generatePlant(b).biome(PLANTS_PLAINS, 100));
        plant(MineriaBlocks.MINT, b -> generatePlant(b).biome(PLANTS_PLAINS, 60).biome(PLANTS_FOREST, 30).biome(PLANTS_JUNGLE, 10));
        plant(MineriaBlocks.THYME, b -> generatePlant(b).biome(PLANTS_PLAINS, 20).biome(PLANTS_SAVANNA, 70).biome(PLANTS_HILL, 10));
        plant(MineriaBlocks.NETTLE, b -> generatePlant(b).biome(PLANTS_PLAINS, 5).biome(PLANTS_FOREST, 25).biome(PLANTS_JUNGLE, 70));
        plant(MineriaBlocks.PULMONARY, b -> generatePlant(b).biome(PLANTS_PLAINS, 20).biome(PLANTS_FOREST, 80));
        plant(MineriaBlocks.RHUBARB, b -> generatePlant(b).biome(PLANTS_PLAINS, 40).biome(PLANTS_FOREST, 30).biome(PLANTS_BAMBOO_JUNGLE, 10));
        plant(MineriaBlocks.SENNA, b -> generatePlant(b).biome(PLANTS_SAVANNA, 35).biome(PLANTS_PLAINS, 5).biome(PLANTS_WOODED_BADLANDS, 10));
        plant(MineriaBlocks.SENNA_BUSH, b -> generateBush(b).biome(PLANTS_SAVANNA, 35).biome(PLANTS_PLAINS, 5).biome(PLANTS_WOODED_BADLANDS, 10));
        plant(MineriaBlocks.BLACK_ELDERBERRY_BUSH, b -> generateBush(b).biome(PLANTS_FOREST, 70).biome(PLANTS_PLAINS, 30));
        plant(MineriaBlocks.ELDERBERRY_BUSH, b -> generateBush(b).biome(PLANTS_FOREST, 80).biome(PLANTS_PLAINS, 20));
        plant(MineriaBlocks.BELLADONNA, b -> generatePlant(b).plantRarity(PlantRarity.UNCOMMON).biome(PLANTS_DARK_FOREST, 10).biome(PLANTS_FOREST, 60).biome(PLANTS_PLAINS, 30));
        plant(MineriaBlocks.MANDRAKE, b -> generatePlant(b).plantRarity(PlantRarity.RARE).biome(PLANTS_DARK_FOREST, 70).biome(PLANTS_FOREST, 20).biome(PLANTS_PLAINS, 10));
        plant(MineriaBlocks.STRYCHNOS_TOXIFERA, b -> generateVine(b.defaultBlockState().setValue(StrychnosPlantBlock.AGE, 2)).plantRarity(PlantRarity.VERY_RARE).biome(PLANTS_JUNGLE, 100));
        plant(MineriaBlocks.STRYCHNOS_NUX_VOMICA, b -> generateVine(b.defaultBlockState().setValue(StrychnosPlantBlock.AGE, 2)).plantRarity(PlantRarity.RARE).biome(PLANTS_JUNGLE, 100));
        plant(MineriaBlocks.LYCIUM_CHINENSE, b -> generateBush(b).biome(PLANTS_BAMBOO_JUNGLE, 60));
        plant(MineriaBlocks.SAUSSUREA_COSTUS, b -> generatePlant(b).biome(PLANTS_BAMBOO_JUNGLE, 10));
        plant(MineriaBlocks.SCHISANDRA_CHINENSIS, b -> generateBambooForestVine(b.defaultBlockState(), 80, 32));
        plant(MineriaBlocks.PULSATILLA_CHINENSIS, b -> generatePlant(b).biome(PLANTS_BAMBOO_JUNGLE, 30));

        copyFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL, MineriaPlacements.GIROLLE_BASE);
        copyFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL, MineriaPlacements.HORN_OF_PLENTY_BASE);
        copyFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL, MineriaPlacements.PUFFBALL_BASE);
    }

    private static void plant(RegistryObject<Block> plant, Function<Block, BiomeFeatureBuilder> builderFunc) {
        add(plant.getId().getPath(), builderFunc.apply(plant.get()));
    }

    private static void add(String name, BiomeFeatureBuilder builder) {
        for (Pair<Either<ResourceKey<Biome>, TagKey<Biome>>, Integer> validBiome : builder.validBiomes) {
            UnaryOperator<ResourceLocation> prefixed = id -> !id.getNamespace().equals("minecraft") ? id.withPrefix(id.getNamespace() + "_") : id;
            String biomeName = validBiome.getFirst().map(key -> prefixed.apply(key.location()).getPath(), tag -> prefixed.apply(tag.location()).getPath());
            String configuredId = "configured_" + name + "_" + biomeName;
            String placedId = "placed_" + name + "_" + biomeName;
            if(CONFIGURED_FEATURES.contains(configuredId) || PLACED_FEATURES.contains(placedId)) {
                continue;
            }
            ResourceKey<ConfiguredFeature<?, ?>> configured = CONFIGURED_FEATURES.register(configuredId, builder::applyFeature);
            ResourceKey<PlacedFeature> placed = PLACED_FEATURES.register(placedId, ctx -> builder.applyPlacement(ctx, configured, validBiome.getSecond()));
            FEATURES_FOR_STEP.compute(builder.step, (decoration, map) -> {
                if(map == null) {
                    map = new HashMap<>();
                }
                map.compute(validBiome.getFirst(), (either, placedFeatures) -> {
                    if(placedFeatures == null) {
                        placedFeatures = new ArrayList<>();
                    }
                    placedFeatures.add(placed);
                    return placedFeatures;
                });
                return map;
            });
        }
    }

    private static BiomeFeatureBuilder generatePlant(Block block) {
        return new BiomeFeatureBuilder(GenerationStep.Decoration.VEGETAL_DECORATION, builder -> new ConfiguredFeature<>(Feature.FLOWER, MineriaConfiguredFeatures.patch(32, block)),
                (builder, feature, rarity) -> new PlacedFeature(feature, List.of(
                        CountPlacement.of(2),
                        RarityFilter.onAverageOnceEvery(Math.abs(rarity - 99) * getCountFromRarity(builder.plantRarity)),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ))
        );
    }

    private static BiomeFeatureBuilder generateBush(Block block) {
        return new BiomeFeatureBuilder(GenerationStep.Decoration.VEGETAL_DECORATION,
                builder -> new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        new RandomPatchConfiguration(16, 3, 2, PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(block)), BlockPredicate.allOf(
                                        BlockPredicate.replaceable(),
                                        BlockPredicate.noFluid(),
                                        BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK, block)
                                )
                        ))),
                (builder, feature, rarity) -> new PlacedFeature(feature, List.of(
                        RarityFilter.onAverageOnceEvery(Math.abs(rarity - 99) * getCountFromRarity(builder.plantRarity) * 2),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ))
        );
    }

    private static BiomeFeatureBuilder generateVine(BlockState state) {
        return new BiomeFeatureBuilder(GenerationStep.Decoration.VEGETAL_DECORATION,
                builder -> new ConfiguredFeature<>(MineriaFeatures.MOD_VINES, new ModVinesFeatureConfig(state, 3, 10, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)),
                (builder, feature, rarity) -> new PlacedFeature(feature, List.of(
                        RarityFilter.onAverageOnceEvery(Math.abs(rarity - 99) * getCountFromRarity(builder.plantRarity) * 2),
                        InSquarePlacement.spread(),
                        BiomeFilter.biome()
                ))
        );
    }

    private static BiomeFeatureBuilder generateBambooForestVine(BlockState state, int rarity, int count) {
        return new BiomeFeatureBuilder(GenerationStep.Decoration.VEGETAL_DECORATION,
                builder -> new ConfiguredFeature<>(MineriaFeatures.MOD_VINES, new ModVinesFeatureConfig(state, 3, 10, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)),
                (builder, feature, unused) -> new PlacedFeature(feature, List.of(
                        CountPlacement.of(count),
                        RarityFilter.onAverageOnceEvery(Math.abs(rarity - 99) * getCountFromRarity(PlantRarity.COMMON)),
                        InSquarePlacement.spread(),
                        BiomeFilter.biome()
                ))
        ).biome(PLANTS_BAMBOO_JUNGLE, 1);
    }

    private static int getCountFromRarity(PlantRarity rarity) {
        return switch (rarity) {
            case COMMON -> 1;
            case UNCOMMON -> 3;
            case RARE -> 5;
            case VERY_RARE -> 8;
        } * 3;
    }

    private static void copyFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> featureToCopy, ResourceKey<PlacedFeature> featureToPlace) {
        COPIED_FEATURES.compute(step, (decoration, pairs) -> {
            if(pairs == null) {
                pairs = new ArrayList<>();
            }
            pairs.add(Pair.of(featureToCopy, featureToPlace));
            return pairs;
        });
    }

    private static void addOres() {
        add("golden_silverfish_netherrack", generateOreUniform(new BlockMatchTest(Blocks.NETHERRACK), MineriaBlocks.GOLDEN_SILVERFISH_NETHERRACK, 5, 0f, 32, 128, 16).biome(BiomeTags.IS_NETHER, 1));
        add("mineral_sand", generateOreUniform(new BlockMatchTest(Blocks.SAND), MineriaBlocks.MINERAL_SAND, 8, 0f, 40, 70, 12).biome(Tags.Biomes.IS_DESERT, 1).biome(BiomeTags.IS_BEACH, 1));

        add("lead_ore", generateOre(MineriaBlocks.LEAD_ORE, MineriaBlocks.DEEPSLATE_LEAD_ORE, 9, 0f,
                TrapezoidHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192)), 14).biome(BiomeTags.IS_OVERWORLD, 1));
        add("silver_ore", generateOre(MineriaBlocks.SILVER_ORE, MineriaBlocks.DEEPSLATE_SILVER_ORE, 9, 0f,
                TrapezoidHeight.of(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)), 3).biome(BiomeTags.IS_OVERWORLD, 1));
        add("titane_ore", generateOre(MineriaBlocks.TITANE_ORE, MineriaBlocks.DEEPSLATE_TITANE_ORE, 6, 0.4f,
                AnyTriangleHeight.of(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(20), VerticalAnchor.absolute(-16)), 4).biome(BiomeTags.IS_OVERWORLD, 1));
        add("lonsdaleite_ore", generateOre(MineriaBlocks.LONSDALEITE_ORE, MineriaBlocks.DEEPSLATE_LONSDALEITE_ORE, 4, 1.0f,
                UniformHeight.of(VerticalAnchor.absolute(-10), VerticalAnchor.absolute(10)), 3).biome(BiomeTags.IS_OVERWORLD, 1));
        add("deep_lonsdaleite_ore", generateOre(MineriaBlocks.LONSDALEITE_ORE, MineriaBlocks.DEEPSLATE_LONSDALEITE_ORE, 3, 0.5f,
                TrapezoidHeight.of(VerticalAnchor.aboveBottom(-18), VerticalAnchor.aboveBottom(18)), 15).biome(BiomeTags.IS_OVERWORLD, 1));
    }

    private static BiomeFeatureBuilder generateOreUniform(RuleTest test, RegistryObject<Block> block, int veinSize, float reducedAirExposure, int minHeight, int maxHeight, int amount) {
        return new BiomeFeatureBuilder(GenerationStep.Decoration.UNDERGROUND_ORES,
                builder -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(test, block.get().defaultBlockState(), veinSize, reducedAirExposure)),
                (builder, feature, rarity) -> new PlacedFeature(feature, List.of(
                        CountPlacement.of(amount),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                        BiomeFilter.biome()
                ))
        );
    }

    private static BiomeFeatureBuilder generateOre(RegistryObject<Block> stoneVariant, RegistryObject<Block> deepslateVariant, int veinSize, float reducedAirExposure, HeightProvider provider, int amount) {
        return new BiomeFeatureBuilder(GenerationStep.Decoration.UNDERGROUND_ORES, builder -> {
            List<OreConfiguration.TargetBlockState> targets = List.of(OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), stoneVariant.get().defaultBlockState()), OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), deepslateVariant.get().defaultBlockState()));
            return new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, veinSize, reducedAirExposure));
        }, (builder, feature, unused) -> new PlacedFeature(feature, List.of(
                CountPlacement.of(amount),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(provider),
                BiomeFilter.biome()
        )));
    }

    private static void addYewTree() {
        add("yew_tree", new BiomeFeatureBuilder(GenerationStep.Decoration.VEGETAL_DECORATION,
                builder -> new ConfiguredFeature<>(Feature.TREE, SpruceYewTree.SPRUCE_YEW_TREE.get()),
                (builder, feature, rarity) -> new PlacedFeature(feature, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(20), MineriaBlocks.SPRUCE_YEW_SAPLING.get())))
                .biome(BiomeTags.IS_TAIGA, 1));
    }

    private static class BiomeFeatureBuilder {
        private final GenerationStep.Decoration step;
        private final FeatureFactory feature;
        private final PlacementFactory placement;
        private PlantRarity plantRarity = PlantRarity.COMMON;
        private final List<Pair<Either<ResourceKey<Biome>, TagKey<Biome>>, Integer>> validBiomes = new ArrayList<>();

        private BiomeFeatureBuilder(GenerationStep.Decoration step, FeatureFactory feature, PlacementFactory placement) {
            this.step = step;
            this.feature = feature;
            this.placement = placement;
        }

        private BiomeFeatureBuilder plantRarity(PlantRarity plantRarity) {
            this.plantRarity = plantRarity;
            return this;
        }

        private BiomeFeatureBuilder biome(TagKey<Biome> biomeTag, int rarity) {
            this.validBiomes.add(Pair.of(Either.right(biomeTag), rarity));
            return this;
        }

        private BiomeFeatureBuilder biome(ResourceKey<Biome> biome, int rarity) {
            this.validBiomes.add(Pair.of(Either.left(biome), rarity));
            return this;
        }

        private ConfiguredFeature<?, ?> applyFeature(MineriaBootstrapContext<ConfiguredFeature<?, ?>> ctx) {
            return feature.configure(this);
        }

        private PlacedFeature applyPlacement(MineriaBootstrapContext<PlacedFeature> ctx, ResourceKey<ConfiguredFeature<?, ?>> configured, int rarity) {
            return placement.place(this, ctx.get(Registries.CONFIGURED_FEATURE, configured), rarity);
        }
    }

    @FunctionalInterface
    private interface FeatureFactory {
        ConfiguredFeature<?, ?> configure(BiomeFeatureBuilder builder);
    }

    @FunctionalInterface
    private interface PlacementFactory {
        PlacedFeature place(BiomeFeatureBuilder builder, Holder<ConfiguredFeature<?, ?>> feature, int rarity);
    }

    private enum PlantRarity {COMMON, UNCOMMON, RARE, VERY_RARE}

    public static void makeConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        CONFIGURED_FEATURES.registerAll(ctx);
    }

    public static void makePlacedFeatures(BootstapContext<PlacedFeature> ctx) {
        PLACED_FEATURES.registerAll(ctx);
    }

    public static void makeBiomeModifiers(BootstapContext<BiomeModifier> context) {
        MineriaBootstrapContext<BiomeModifier> ctx = MineriaBootstrapContext.wrap(context);

        UnaryOperator<ResourceLocation> withNamespace = id -> !id.getNamespace().equals("minecraft") ? id.withPrefix(id.getNamespace() + "_") : id;

        for (Map.Entry<GenerationStep.Decoration, Map<Either<ResourceKey<Biome>, TagKey<Biome>>, List<ResourceKey<PlacedFeature>>>> stepToFeatures : FEATURES_FOR_STEP.entrySet()) {
            for (Map.Entry<Either<ResourceKey<Biome>, TagKey<Biome>>, List<ResourceKey<PlacedFeature>>> biomeToFeature : stepToFeatures.getValue().entrySet()) {
                String biomeKey = biomeToFeature.getKey().map(key -> withNamespace.apply(key.location()).getPath(), tag -> withNamespace.apply(tag.location()).getPath());
                ResourceKey<BiomeModifier> modifierKey = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Mineria.MODID, biomeKey + "_add"));

                HolderSet<Biome> biomes = biomeToFeature.getKey().map(key -> HolderSet.direct(ctx.get(Registries.BIOME, key)), tag -> ctx.values(Registries.BIOME, tag));
                HolderSet<PlacedFeature> features = HolderSet.direct(key -> ctx.get(Registries.PLACED_FEATURE, key), biomeToFeature.getValue());

                ctx.register(modifierKey, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, features, stepToFeatures.getKey()));
            }
        }

        for (Map.Entry<GenerationStep.Decoration, List<Pair<ResourceKey<PlacedFeature>, ResourceKey<PlacedFeature>>>> stepToCopiedFeatures : COPIED_FEATURES.entrySet()) {
            for (Pair<ResourceKey<PlacedFeature>, ResourceKey<PlacedFeature>> copyInfo : stepToCopiedFeatures.getValue()) {
                String toCopyKey = withNamespace.apply(copyInfo.getFirst().location()).getPath();
                String toPlaceKey = copyInfo.getSecond().location().getPath();
                ResourceKey<BiomeModifier> modifierKey = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Mineria.MODID, toPlaceKey + "_copying_" + toCopyKey));

                HolderSet<PlacedFeature> featuresToCopy = HolderSet.direct(ctx.get(Registries.PLACED_FEATURE, copyInfo.getFirst()));
                Holder<PlacedFeature> featureToPlace = ctx.get(Registries.PLACED_FEATURE, copyInfo.getSecond());

                ctx.register(modifierKey, new CopyFeatureBiomeModifier(featuresToCopy, featureToPlace, stepToCopiedFeatures.getKey()));
            }
        }
    }
}
