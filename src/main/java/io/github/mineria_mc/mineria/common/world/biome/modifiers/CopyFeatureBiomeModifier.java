package io.github.mineria_mc.mineria.common.world.biome.modifiers;

import io.github.mineria_mc.mineria.common.init.MineriaBiomeModifierSerializers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record CopyFeatureBiomeModifier(HolderSet<PlacedFeature> toCopy, Holder<PlacedFeature> toPlace, GenerationStep.Decoration step) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if(phase == Phase.ADD) {
            BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            if(settings.getFeatures(step).stream().anyMatch(toCopy::contains)) {
                settings.addFeature(step, toPlace);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return MineriaBiomeModifierSerializers.COPY_FEATURE.get();
    }
}
