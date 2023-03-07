package io.github.mineria_mc.mineria.common.world.terrablender;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.worldgen.RegionUtils;

import java.util.List;
import java.util.function.Consumer;

public class MineriaBiomeRegion extends Region {
    public MineriaBiomeRegion() {
        super(new ResourceLocation(Mineria.MODID, "overworld"), RegionType.OVERWORLD, 10);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        List<Climate.ParameterPoint> points = RegionUtils.getVanillaParameterPoints(Biomes.PLAINS).stream().collect(ImmutableList.toImmutableList());
        for (Climate.ParameterPoint point : points) {
            addBiome(mapper, Climate.parameters(increase(point.temperature(), 0.1f), point.humidity(), Climate.Parameter.span(0.03f, 1f), abs(divide(point.erosion(), 4)), point.depth(), increase(point.weirdness(), 0.1f), 0.0f), MineriaBiomes.EASTERN_PLAINS);
        }
        addBiomeSimilar(mapper, Biomes.PLAINS, Biomes.PLAINS);

        addBiomeSimilar(mapper, Biomes.RIVER, Biomes.RIVER);
        addBiomeSimilar(mapper, Biomes.FROZEN_RIVER, Biomes.FROZEN_RIVER);
        addBiomeSimilar(mapper, Biomes.BEACH, Biomes.BEACH);
        addBiomeSimilar(mapper, Biomes.SNOWY_BEACH, Biomes.SNOWY_BEACH);
        addBiomeSimilar(mapper, Biomes.STONY_SHORE, Biomes.STONY_SHORE);
        addBiomeSimilar(mapper, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN);
        addBiomeSimilar(mapper, Biomes.OCEAN, Biomes.OCEAN);
        addBiomeSimilar(mapper, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN);
        addBiomeSimilar(mapper, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN);
        addBiomeSimilar(mapper, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN);
        addBiomeSimilar(mapper, Biomes.DEEP_OCEAN, Biomes.DEEP_OCEAN);
        addBiomeSimilar(mapper, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN);
        addBiomeSimilar(mapper, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_COLD_OCEAN);
        addBiomeSimilar(mapper, Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        addBiomeSimilar(mapper, Biomes.FOREST, Biomes.FOREST);
        addBiomeSimilar(mapper, Biomes.DESERT, Biomes.DESERT);
        addBiomeSimilar(mapper, Biomes.SWAMP, Biomes.SWAMP);
        addBiomeSimilar(mapper, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST);
        addBiomeSimilar(mapper, Biomes.TAIGA, Biomes.TAIGA);
        addBiomeSimilar(mapper, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
        addBiomeSimilar(mapper, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA);
        addBiomeSimilar(mapper, Biomes.GROVE, Biomes.GROVE);
        addBiomeSimilar(mapper, Biomes.JUNGLE, Biomes.JUNGLE);
        addBiomeSimilar(mapper, Biomes.BAMBOO_JUNGLE, Biomes.BAMBOO_JUNGLE);
        addBiomeSimilar(mapper, Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE);
        addBiomeSimilar(mapper, Biomes.SAVANNA, Biomes.SAVANNA);
        addBiomeSimilar(mapper, Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU);
        addBiomeSimilar(mapper, Biomes.WINDSWEPT_SAVANNA, Biomes.WINDSWEPT_SAVANNA);
        addBiomeSimilar(mapper, Biomes.MEADOW, Biomes.MEADOW);
        addBiomeSimilar(mapper, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST);
        addBiomeSimilar(mapper, Biomes.FROZEN_PEAKS, Biomes.FROZEN_PEAKS);
        addBiomeSimilar(mapper, Biomes.STONY_PEAKS, Biomes.STONY_PEAKS);
        addBiomeSimilar(mapper, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES);
        addBiomeSimilar(mapper, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS);
        addBiomeSimilar(mapper, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS);
        addBiomeSimilar(mapper, Biomes.ICE_SPIKES, Biomes.ICE_SPIKES);
        addBiomeSimilar(mapper, Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELDS);
    }

    private Climate.Parameter increase(Climate.Parameter parameter, float value) {
        return new Climate.Parameter(parameter.min() + Climate.quantizeCoord(value), parameter.max() + Climate.quantizeCoord(value));
    }

    private Climate.Parameter decrease(Climate.Parameter parameter, long value) {
        return new Climate.Parameter(parameter.min() - value, parameter.max() - value);
    }

    private Climate.Parameter divide(Climate.Parameter parameter, long divisor) {
        return new Climate.Parameter(parameter.min() / divisor, parameter.max() / divisor);
    }

    private Climate.Parameter abs(Climate.Parameter parameter) {
        return new Climate.Parameter(Math.abs(parameter.min()), Math.abs(parameter.max()));
    }
}
