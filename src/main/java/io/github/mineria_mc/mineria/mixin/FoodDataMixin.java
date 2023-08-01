package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"))
    private int mineria$redirect_getNutrition(FoodProperties props, Item item, ItemStack stack, @Nullable LivingEntity living) {
        int nutrition = props.getNutrition();
        if(living != null && living.hasEffect(MineriaEffects.NUTRITION_QUALITY.get())) {
            nutrition += living.getEffect(MineriaEffects.NUTRITION_QUALITY.get()).getAmplifier() + 1;
        }
        return nutrition;
    }

    /**
     * @param player we need to specify the argument signature in the target signature to have access to its arguments
     */
    @Redirect(method = "tick(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean mineria$hasNaturalRegeneration(GameRules instance, GameRules.Key<GameRules.BooleanValue> ruleKey, Player player) {
        return instance.getBoolean(ruleKey) && !(player.hasEffect(MobEffects.POISON) || player.hasEffect(MineriaEffects.NO_NATURAL_REGENERATION.get()));
    }
}
