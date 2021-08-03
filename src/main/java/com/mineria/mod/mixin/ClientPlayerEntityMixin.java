package com.mineria.mod.mixin;

import com.mineria.mod.effects.PoisonEffectInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity
{
    @Shadow public float prevTimeInPortal;

    @Shadow public float timeInPortal;

    @Shadow @Final protected Minecraft mc;

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    @Shadow public abstract void closeScreen();

    /**
     * @reason Injections partially works
     * @author LGatodu47
     */
    /*@Nullable
    @Overwrite
    public EffectInstance removeActivePotionEffect(Effect effect)
    {
        if(effect == Effects.POISON || effect == Effects.NAUSEA)
        {
            this.prevTimeInPortal = 0.0F;
            this.timeInPortal = 0.0F;
        }

        return super.removeActivePotionEffect(effect);
    }*/

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    public void mixIsSneaking(CallbackInfoReturnable<Boolean> cir)
    {
        if(this.isPotionActive(Effects.POISON) && this.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)
        {
            PoisonEffectInstance poison = (PoisonEffectInstance) this.getActivePotionEffect(Effects.POISON);
            if(poison.doSpasms())
            {
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * @reason it's easier
     * @author LGatodu47
     */
    @Overwrite
    private void handlePortalTeleportation()
    {
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal)
        {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.isPauseScreen())
            {
                if (this.mc.currentScreen instanceof ContainerScreen)
                {
                    this.closeScreen();
                }

                this.mc.displayGuiScreen((Screen) null);
            }

            if (this.timeInPortal == 0.0F)
            {
                this.mc.getSoundHandler().play(SimpleSound.ambientWithoutAttenuation(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4F + 0.8F, 0.25F));
            }

            this.timeInPortal += 0.0125F;
            if (this.timeInPortal >= 1.0F)
            {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        } else if (this.isPotionActive(Effects.NAUSEA) && this.getActivePotionEffect(Effects.NAUSEA).getDuration() > 60)
        {
            int amplifier = this.getActivePotionEffect(Effects.NAUSEA).getAmplifier();
            float max = 0.5F * (amplifier + 1);
            this.timeInPortal += 0.006666667F;
            if (this.timeInPortal > max)
            {
                this.timeInPortal = max;
            }
        } else
        {
            if (this.timeInPortal > 0.0F)
            {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F)
            {
                this.timeInPortal = 0.0F;
            }
        }

        this.decrementTimeUntilPortal();
    }
}
