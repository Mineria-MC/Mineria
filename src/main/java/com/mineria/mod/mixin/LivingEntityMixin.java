package com.mineria.mod.mixin;

import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.items.IMineriaItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    @Shadow public abstract boolean hasEffect(MobEffect p_70644_1_);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect p_70660_1_);

    @Shadow public abstract Map<MobEffect, MobEffectInstance> getActiveEffectsMap();

    public LivingEntityMixin(EntityType<?> entityTypeIn, Level worldIn)
    {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "hurt", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void hurt(DamageSource source, float dmg, CallbackInfoReturnable<Boolean> cir)
    {
        Entity directEntity = source.getDirectEntity();
        if(directEntity instanceof LivingEntity living)
        {
            if(living.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IMineriaItem)
            {
                this.invulnerableTime = ((IMineriaItem) living.getItemInHand(InteractionHand.MAIN_HAND).getItem()).getInvulnerableTime(this);
            }
        }
    }

    @Inject(method = "curePotionEffects", at = @At("HEAD"), remap = false)
    public void curePotionEffects(ItemStack curativeItem, CallbackInfoReturnable<Boolean> cir)
    {
        if(!this.level.isClientSide)
        {
            if (this.hasEffect(MobEffects.POISON) && this.getEffect(MobEffects.POISON) instanceof PoisonEffectInstance)
            {
                if(this.getEffect(MobEffects.POISON).isCurativeItem(curativeItem))
                {
                    if(this.hasEffect(MobEffects.CONFUSION))
                    {
                        MobEffectInstance nausea = this.getEffect(MobEffects.CONFUSION);
                        nausea.setCurativeItems(this.getEffect(MobEffects.POISON).getCurativeItems());
                        this.getActiveEffectsMap().put(MobEffects.CONFUSION, nausea);
                    }
                    if(this.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))
                    {
                        MobEffectInstance slowness = this.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
                        slowness.setCurativeItems(this.getEffect(MobEffects.POISON).getCurativeItems());
                        this.getActiveEffectsMap().put(MobEffects.MOVEMENT_SLOWDOWN, slowness);
                    }
                }
            }
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    public void onEffectRemoved(MobEffectInstance effect, CallbackInfo ci)
    {
        if (effect instanceof PoisonEffectInstance)
        {
            ((PoisonEffectInstance) effect).onPotionCured((LivingEntity) (Object) this);
            if(this.hasEffect(MobEffects.CONFUSION))
                this.getEffect(MobEffects.CONFUSION).setCurativeItems(effect.getCurativeItems());
            if(this.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))
                this.getEffect(MobEffects.MOVEMENT_SLOWDOWN).setCurativeItems(effect.getCurativeItems());
        }
    }
}
