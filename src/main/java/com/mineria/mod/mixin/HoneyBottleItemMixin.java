package com.mineria.mod.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin
{
    @Redirect(method = "finishUsingItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
    private boolean finishUsingItem(LivingEntity living, MobEffect effect, ItemStack stack, Level world, LivingEntity living2)
    {
        return living.curePotionEffects(stack);
    }
}
