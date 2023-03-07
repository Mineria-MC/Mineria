package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.items.IMineriaItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getItem();

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void inject_fireImmune(CallbackInfoReturnable<Boolean> ret) {
        ItemStack stack = getItem();
        if(stack.getItem() instanceof IMineriaItem mineriaItem && mineriaItem.isFireResistant(stack)) {
            ret.setReturnValue(true);
        }
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;canBeHurtBy(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    private boolean redirect_canBeHurtBy(Item item, DamageSource source) {
        ItemStack stack = getItem();
        if(stack.getItem() instanceof IMineriaItem mineriaItem) {
            return mineriaItem.canBeHurtBy(stack, source);
        }
        return item.canBeHurtBy(source);
    }
}
