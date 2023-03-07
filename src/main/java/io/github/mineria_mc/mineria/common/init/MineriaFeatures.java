package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.common.world.feature.ModVinesFeature;
import io.github.mineria_mc.mineria.common.world.feature.ModVinesFeatureConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

public class MineriaFeatures {
    private static final Map<String, Feature<?>> FEATURES = new HashMap<>();

    public static final Feature<ModVinesFeatureConfig> MOD_VINES = registerFeature("mod_vines", new ModVinesFeature());

    private static <FC extends FeatureConfiguration> Feature<FC> registerFeature(String name, Feature<FC> feature) {
        FEATURES.put(name, feature);
        return feature;
    }

    public static void registerFeatures(RegisterEvent event) {
        event.register(Registries.FEATURE, helper -> FEATURES.forEach(helper::register));
    }
}
