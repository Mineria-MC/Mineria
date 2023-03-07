package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.items.IMineriaItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean hasEffect(MobEffect effect);

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(MobEffect effect);

    public LivingEntityMixin(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "hurt", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void hurt(DamageSource source, float dmg, CallbackInfoReturnable<Boolean> cir) {
        Entity directEntity = source.getDirectEntity();
        if (directEntity instanceof LivingEntity living) {
            if (living.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IMineriaItem item) {
                this.invulnerableTime = item.getInvulnerableTime(this);
            }
        }
    }

    @Inject(method = "curePotionEffects", at = @At("HEAD"), remap = false)
    public void curePotionEffects(ItemStack curativeItem, CallbackInfoReturnable<Boolean> cir) {
        if (!this.level.isClientSide) {
            if (hasEffect(MobEffects.POISON) && getEffect(MobEffects.POISON) instanceof PoisonMobEffectInstance poison) {
                if (poison.isCurativeItem(curativeItem)) {
                    ifEffectPresent(MobEffects.CONFUSION, e -> e.setCurativeItems(poison.getCurativeItems()));
                    ifEffectPresent(MobEffects.MOVEMENT_SLOWDOWN, e -> e.setCurativeItems(poison.getCurativeItems()));
                }
            }
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    public void onEffectRemoved(MobEffectInstance effect, CallbackInfo ci) {
        if (effect instanceof PoisonMobEffectInstance) {
            ((PoisonMobEffectInstance) effect).onPotionCured((LivingEntity) (Object) this);
            ifEffectPresent(MobEffects.CONFUSION, e -> e.setCurativeItems(effect.getCurativeItems()));
            ifEffectPresent(MobEffects.MOVEMENT_SLOWDOWN, e -> e.setCurativeItems(effect.getCurativeItems()));
        }
    }

    @Unique
    private void ifEffectPresent(MobEffect effect, Consumer<MobEffectInstance> consumer) {
        if(hasEffect(effect)) {
            MobEffectInstance instance = getEffect(effect);
            if(instance != null) {
                consumer.accept(instance);
            }
        }
    }
}
