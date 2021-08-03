package com.mineria.mod.mixin;

import com.mineria.mod.capabilities.CapabilityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin
{
    @Inject(method = "onItemUseFinish", at = @At("HEAD"))
    public void onItemUseFinish(ItemStack stack, World world, LivingEntity living, CallbackInfoReturnable<ItemStack> cir)
    {
        if(stack.isFood())
        {
            living.getCapability(CapabilityRegistry.INGESTED_FOOD_CAP).ifPresent(cap -> cap.foodIngested(stack.getItem()));
        }
    }
}
