package com.mineria.mod.common.world.feature;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.data.worldgen.Features;

public class ModDefaultBiomeFeatures
{
    public static void withGrassVegetation(BiomeGenerationSettings.Builder builder)
    {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_PLAIN_DECORATED);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
    }
}
