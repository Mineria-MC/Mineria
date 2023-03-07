package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.world.biome.modifiers.CopyFeatureBiomeModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MineriaBiomeModifierSerializers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Mineria.MODID);

    public static final RegistryObject<Codec<CopyFeatureBiomeModifier>> COPY_FEATURE = BIOME_MODIFIER_SERIALIZERS.register("copy_feature", () ->
            RecordCodecBuilder.create(instance -> instance.group(
                    PlacedFeature.LIST_CODEC.fieldOf("to_copy").forGetter(CopyFeatureBiomeModifier::toCopy),
                    PlacedFeature.CODEC.fieldOf("to_place").forGetter(CopyFeatureBiomeModifier::toPlace),
                    GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(CopyFeatureBiomeModifier::step)
            ).apply(instance, CopyFeatureBiomeModifier::new))
    );
}
