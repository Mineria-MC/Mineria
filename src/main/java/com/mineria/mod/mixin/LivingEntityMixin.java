package com.mineria.mod.mixin;

import com.mineria.mod.effects.PoisonEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    @Shadow @Nullable public abstract EffectInstance getActivePotionEffect(Effect p_70660_1_);

    @Shadow public abstract boolean isPotionActive(Effect p_70644_1_);

    @Shadow public abstract Map<Effect, EffectInstance> getActivePotionMap();

    public LivingEntityMixin(EntityType<?> entityTypeIn, World worldIn)
    {
        super(entityTypeIn, worldIn);
    }

    /**
     * @reason @Inject wouldn't work
     * @author LGatodu47
     */
    /*@Overwrite(remap = false)
    public boolean curePotionEffects(ItemStack curativeItem) {
        if (this.world.isRemote)
            return false;
        boolean ret = false;
        Iterator<EffectInstance> itr = this.activePotionsMap.values().iterator();
        while (itr.hasNext()) {
            EffectInstance effect = itr.next();
            if (effect.isCurativeItem(curativeItem) && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent((LivingEntity) (Object) this, effect))) {
                this.onFinishedPotionEffect(effect);
                itr.remove();
                ret = true;
                this.potionsNeedUpdate = true;
            }
        }
        return ret;
    }*/

    @Inject(method = "curePotionEffects", at = @At("HEAD"), remap = false)
    public void curePotionEffects(ItemStack curativeItem, CallbackInfoReturnable<Boolean> cir)
    {
        if(!this.world.isRemote)
        {
            if (this.isPotionActive(Effects.POISON) && this.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                if(this.getActivePotionEffect(Effects.POISON).isCurativeItem(curativeItem))
                {
                    if(this.isPotionActive(Effects.NAUSEA))
                    {
                        EffectInstance nausea = this.getActivePotionEffect(Effects.NAUSEA);
                        nausea.setCurativeItems(this.getActivePotionEffect(Effects.POISON).getCurativeItems());
                        this.getActivePotionMap().put(Effects.NAUSEA, nausea);
                    }
                    if(this.isPotionActive(Effects.SLOWNESS))
                    {
                        EffectInstance slowness = this.getActivePotionEffect(Effects.SLOWNESS);
                        slowness.setCurativeItems(this.getActivePotionEffect(Effects.POISON).getCurativeItems());
                        this.getActivePotionMap().put(Effects.SLOWNESS, slowness);
                    }
                }
            }
        }
    }

    @Inject(method = "onFinishedPotionEffect", at = @At("HEAD"))
    public void onFinishedPotionEffect(EffectInstance effect, CallbackInfo ci)
    {
        if (effect instanceof PoisonEffectInstance)
        {
            ((PoisonEffectInstance) effect).onPotionCured((LivingEntity) (Object) this);
            if(this.isPotionActive(Effects.NAUSEA))
                this.getActivePotionEffect(Effects.NAUSEA).setCurativeItems(effect.getCurativeItems());
            if(this.isPotionActive(Effects.SLOWNESS))
                this.getActivePotionEffect(Effects.SLOWNESS).setCurativeItems(effect.getCurativeItems());
        }
    }
}
