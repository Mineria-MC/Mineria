package io.github.mineria_mc.mineria.common.world.biome;

import io.github.mineria_mc.mineria.common.init.datagen.MineriaBootstrapContext;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaBootstrapEntries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public abstract class MineriaBiomeInfo implements MineriaBootstrapEntries.Entry<Biome> {
    private final ResourceKey<Biome> biomeKey;

    protected MineriaBiomeInfo(ResourceKey<Biome> biomeKey) {
        this.biomeKey = biomeKey;
    }

    public ResourceKey<Biome> getBiomeKey() {
        return biomeKey;
    }

    @Override
    public final Biome create(MineriaBootstrapContext<Biome> ctx) {
        return build(ctx.lookup(Registries.PLACED_FEATURE), ctx.lookup(Registries.CONFIGURED_CARVER));
    }

    public final Biome build(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        return create().generationSettings(configureGeneration(features, carvers).build()).specialEffects(configureEffects().build()).mobSpawnSettings(configureMobSpawns().build()).build();
    }

    protected abstract Biome.BiomeBuilder create();

    protected abstract BiomeGenerationSettings.Builder configureGeneration(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers);

    protected abstract BiomeSpecialEffects.Builder configureEffects();

    protected abstract MobSpawnSettings.Builder configureMobSpawns();

    public abstract int getSpawnWeight();

    public boolean canSpawnUnderTemperature(Climate.Parameter temperature) {
        return false;
    }

    public boolean canSpawnUnderHumidity(Climate.Parameter humidity) {
        return false;
    }

    protected static int getSkyColorWithTemperatureModifier(float temperature) {
        float temperatureModifier = temperature / 3.0F;
        temperatureModifier = Mth.clamp(temperatureModifier, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - temperatureModifier * 0.05F, 0.5F + temperatureModifier * 0.1F, 1.0F);
    }
}
