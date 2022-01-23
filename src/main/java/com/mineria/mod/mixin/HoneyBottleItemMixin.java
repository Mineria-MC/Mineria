package com.mineria.mod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin
{
    @Redirect(method = "finishUsingItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;removeEffect(Lnet/minecraft/potion/Effect;)Z"))
    private boolean finishUsingItem(LivingEntity living, Effect effect, ItemStack stack, World world, LivingEntity living2)
    {
        return living.curePotionEffects(stack);
    }
}
