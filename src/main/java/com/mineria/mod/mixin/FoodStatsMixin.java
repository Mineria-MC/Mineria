package com.mineria.mod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodStats.class)
public abstract class FoodStatsMixin
{
    @Shadow
    private int foodTimer;

    @Shadow
    private int prevFoodLevel;

    @Shadow
    private int foodLevel;

    @Shadow
    private float foodExhaustionLevel;

    @Shadow
    private float foodSaturationLevel;

    @Shadow
    public abstract void addExhaustion(float exhaustion);

    /**
     * @reason Injection using INVOKE InjectionPoint doesn't work
     * @author LGatodu47
     */
    @Overwrite
    public void tick(PlayerEntity player)
    {
        Difficulty difficulty = player.world.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0F)
        {
            this.foodExhaustionLevel -= 4.0F;
            if (this.foodSaturationLevel > 0.0F)
            {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL)
            {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean flag = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) /* ADDED */ &&  !player.isPotionActive(Effects.POISON) /* ADDED */;
        if (flag && this.foodSaturationLevel > 0.0F && player.shouldHeal() && this.foodLevel >= 20)
        {
            ++this.foodTimer;
            if (this.foodTimer >= 10)
            {
                float f = Math.min(this.foodSaturationLevel, 6.0F);
                player.heal(f / 6.0F);
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && player.shouldHeal())
        {
            ++this.foodTimer;
            if (this.foodTimer >= 80)
            {
                player.heal(1.0F);
                this.addExhaustion(6.0F);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0)
        {
            ++this.foodTimer;
            if (this.foodTimer >= 80)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)
                {
                    player.attackEntityFrom(DamageSource.STARVE, 1.0F);
                }

                this.foodTimer = 0;
            }
        } else
        {
            this.foodTimer = 0;
        }
    }
}
