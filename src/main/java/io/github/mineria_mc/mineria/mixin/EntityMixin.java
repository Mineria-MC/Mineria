package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
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
}
