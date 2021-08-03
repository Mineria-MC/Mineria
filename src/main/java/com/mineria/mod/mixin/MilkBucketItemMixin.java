package com.mineria.mod.mixin;

import com.mineria.mod.capabilities.CapabilityRegistry;
import com.mineria.mod.items.DrinkItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin
{
    @Inject(method = "onItemUseFinish", at = @At("HEAD"))
    public void onItemUseFinish(ItemStack stack, World world, LivingEntity living, CallbackInfoReturnable<ItemStack> cir)
    {
        living.getCapability(CapabilityRegistry.INGESTED_FOOD_CAP).ifPresent(cap -> cap.foodIngested(stack.getItem()));
        DrinkItem.unlockLaxativeDrinks(living);
    }
}
