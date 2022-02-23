package com.mineria.mod.mixin;

import com.mineria.mod.common.items.DrinkItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin
{
    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void finishUsingItem(ItemStack stack, Level world, LivingEntity living, CallbackInfoReturnable<ItemStack> cir)
    {
        DrinkItem.unlockLaxativeDrinks(living);
    }
}
