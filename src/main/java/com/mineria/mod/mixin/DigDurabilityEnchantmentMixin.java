package com.mineria.mod.mixin;

import com.mineria.mod.common.items.KunaiItem;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DigDurabilityEnchantment.class)
public class DigDurabilityEnchantmentMixin
{
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    public void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
    {
        if(stack.getItem() instanceof KunaiItem) cir.setReturnValue(false);
    }
}
