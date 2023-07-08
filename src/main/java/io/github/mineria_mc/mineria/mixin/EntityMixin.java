package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.capabilities.TickingDataTypes;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @SuppressWarnings("UnusedAssignment")
    @Inject(method = "setShiftKeyDown", at = @At("HEAD"))
    public void mineria$setShiftKeyDown(boolean keyDownIn, CallbackInfo info) {
        if ((Entity) (Object) this instanceof LivingEntity living) {
            if (living.hasEffect(MobEffects.POISON) && living.getEffect(MobEffects.POISON) instanceof PoisonMobEffectInstance poison) {
                if (poison.doSpasms() && keyDownIn) {
                    keyDownIn = false;
                }
            }
        }
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;deserializeCaps(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    private void mineria$inject_load(CompoundTag nbt, CallbackInfo ci) {
        if((Entity) (Object) this instanceof LivingEntity living) {
            CompoundTag caps = nbt.getCompound("ForgeCaps");
            if(caps.contains("mineria:poison_exposure") && caps.getCompound("mineria:poison_exposure").contains("ExposureMap")) {
                living.getCapability(MineriaCapabilities.TICKING_DATA).ifPresent(cap -> cap.updateLegacyCapability(TickingDataTypes.POISON_EXPOSURE, caps.getCompound("mineria:poison_exposure").getCompound("ExposureMap")));
            }
            if(caps.contains("mineria:element_exposure") && caps.getCompound("mineria:element_exposure").contains("ExposureMap")) {
                living.getCapability(MineriaCapabilities.TICKING_DATA).ifPresent(cap -> cap.updateLegacyCapability(TickingDataTypes.ELEMENT_EXPOSURE, caps.getCompound("mineria:element_exposure").getCompound("ExposureMap")));
            }
        }
    }
}
