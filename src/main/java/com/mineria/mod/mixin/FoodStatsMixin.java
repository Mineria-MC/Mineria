package com.mineria.mod.mixin;

import com.mineria.mod.common.init.MineriaEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.FoodStats;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodStats.class)
public class FoodStatsMixin
{
    /**
     * @param player we need to specify the argument signature in the target signature to have access to its arguments
     */
    @Redirect(method = "tick(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"))
    private boolean hasNaturalRegeneration(GameRules instance, GameRules.RuleKey<GameRules.BooleanValue> ruleKey, PlayerEntity player)
    {
        return instance.getBoolean(ruleKey) && !(player.hasEffect(Effects.POISON) || player.hasEffect(MineriaEffects.NO_NATURAL_REGENERATION.get()));
    }
}
