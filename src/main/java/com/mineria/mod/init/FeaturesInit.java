package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.world.feature.ModVinesFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class FeaturesInit
{
    private static final List<Feature<?>> FEATURES = new ArrayList<>();

    public static final Feature<BlockStateFeatureConfig> MOD_VINES = registerFeature("mod_vines", new ModVinesFeature());

    private static <FC extends IFeatureConfig> Feature<FC> registerFeature(String name, Feature<FC> feature)
    {
        FEATURES.add(feature.setRegistryName(new ResourceLocation(References.MODID, name)));
        return feature;
    }

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        FEATURES.forEach(event.getRegistry()::register);
    }
}
