package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.datagen.MineriaBiomes;
import io.github.mineria_mc.mineria.common.world.biome.MineriaBiomeInfo;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverworldBiomeBuilder.class)
public class OverworldBiomeBuilderMixin {
    @Shadow @Final private Climate.Parameter[] temperatures;

    @Shadow @Final private Climate.Parameter[] humidities;

    @Inject(method = "pickMiddleBiome", at = @At("HEAD"), cancellable = true)
    public void mineria$inject_pickMiddleBiome(int temperature, int humidity, Climate.Parameter param, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        for(MineriaBiomeInfo info : MineriaBiomes.getBiomesInfo()) {
            if(MineriaUtils.RANDOM.nextInt(info.getSpawnWeight()) == 0 && info.canSpawnUnderTemperature(temperatures[temperature]) && info.canSpawnUnderHumidity(humidities[humidity])) {
                cir.setReturnValue(info.getBiomeKey());
            }
        }
    }
}
