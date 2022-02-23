package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.world.feature.ModVinesFeature;
import com.mineria.mod.common.world.feature.ModVinesFeatureConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class MineriaFeatures
{
    private static final List<Feature<?>> FEATURES = new ArrayList<>();

    public static final Feature<ModVinesFeatureConfig> MOD_VINES = registerFeature("mod_vines", new ModVinesFeature());

    private static <FC extends FeatureConfiguration> Feature<FC> registerFeature(String name, Feature<FC> feature)
    {
        FEATURES.add(feature.setRegistryName(new ResourceLocation(Mineria.MODID, name)));
        return feature;
    }

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        FEATURES.forEach(event.getRegistry()::register);
    }
}
