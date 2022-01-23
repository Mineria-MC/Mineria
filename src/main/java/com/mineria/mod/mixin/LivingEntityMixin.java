package com.mineria.mod.mixin;

import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.items.IMineriaItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
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
    @Shadow public abstract boolean hasEffect(Effect p_70644_1_);

    @Shadow @Nullable public abstract EffectInstance getEffect(Effect p_70660_1_);

    @Shadow public abstract Map<Effect, EffectInstance> getActiveEffectsMap();

    public LivingEntityMixin(EntityType<?> entityTypeIn, World worldIn)
    {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "hurt", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;invulnerableTime:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void hurt(DamageSource source, float dmg, CallbackInfoReturnable<Boolean> cir)
    {
        Entity directEntity = source.getDirectEntity();
        if(directEntity instanceof LivingEntity)
        {
            LivingEntity living = (LivingEntity) directEntity;
            if(living.getItemInHand(Hand.MAIN_HAND).getItem() instanceof IMineriaItem)
            {
                this.invulnerableTime = ((IMineriaItem) living.getItemInHand(Hand.MAIN_HAND).getItem()).getInvulnerableTime(this);
            }
        }
    }

    @Inject(method = "curePotionEffects", at = @At("HEAD"), remap = false)
    public void curePotionEffects(ItemStack curativeItem, CallbackInfoReturnable<Boolean> cir)
    {
        if(!this.level.isClientSide)
        {
            if (this.hasEffect(Effects.POISON) && this.getEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                if(this.getEffect(Effects.POISON).isCurativeItem(curativeItem))
                {
                    if(this.hasEffect(Effects.CONFUSION))
                    {
                        EffectInstance nausea = this.getEffect(Effects.CONFUSION);
                        nausea.setCurativeItems(this.getEffect(Effects.POISON).getCurativeItems());
                        this.getActiveEffectsMap().put(Effects.CONFUSION, nausea);
                    }
                    if(this.hasEffect(Effects.MOVEMENT_SLOWDOWN))
                    {
                        EffectInstance slowness = this.getEffect(Effects.MOVEMENT_SLOWDOWN);
                        slowness.setCurativeItems(this.getEffect(Effects.POISON).getCurativeItems());
                        this.getActiveEffectsMap().put(Effects.MOVEMENT_SLOWDOWN, slowness);
                    }
                }
            }
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    public void onEffectRemoved(EffectInstance effect, CallbackInfo ci)
    {
        if (effect instanceof PoisonEffectInstance)
        {
            ((PoisonEffectInstance) effect).onPotionCured((LivingEntity) (Object) this);
            if(this.hasEffect(Effects.CONFUSION))
                this.getEffect(Effects.CONFUSION).setCurativeItems(effect.getCurativeItems());
            if(this.hasEffect(Effects.MOVEMENT_SLOWDOWN))
                this.getEffect(Effects.MOVEMENT_SLOWDOWN).setCurativeItems(effect.getCurativeItems());
        }
    }
}
