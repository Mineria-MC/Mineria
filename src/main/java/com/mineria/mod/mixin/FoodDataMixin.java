package com.mineria.mod.mixin;

import com.mineria.mod.common.init.MineriaEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodData.class)
public class FoodDataMixin
{
    /**
     * @param player we need to specify the argument signature in the target signature to have access to its arguments
     */
    @Redirect(method = "tick(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean hasNaturalRegeneration(GameRules instance, GameRules.Key<GameRules.BooleanValue> ruleKey, Player player)
    {
        return instance.getBoolean(ruleKey) && !(player.hasEffect(MobEffects.POISON) || player.hasEffect(MineriaEffects.NO_NATURAL_REGENERATION.get()));
    }
}
