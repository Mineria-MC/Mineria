package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.MineriaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.extensions.IForgeMobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements IForgeMobEffectInstance {
    @Inject(method = "save", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    public void mineria$inject_save(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        nbt.putString("Serializer", "mineria:default");
    }

    @Inject(method = "loadSpecifiedEffect", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
    private static void mineria$inject_loadSpecifiedEffect(MobEffect effect, CompoundTag nbt, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (nbt.contains("Serializer")) {
            cir.setReturnValue(Objects.requireNonNull(
                    MineriaRegistries.EFFECT_SERIALIZERS.get().getValue(new ResourceLocation(nbt.getString("Serializer"))),
                    "No default key for mineria:effect_instance_serializer registry! A mod messes up everything."
            ).deserialize(effect, nbt));
        }
    }
}
