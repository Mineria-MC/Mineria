package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import io.github.mineria_mc.mineria.util.DeferredRegisterUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    @Redirect(method = "generatePotionEffectTypes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab$Output;accept(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/CreativeModeTab$TabVisibility;)V"))
    private static void mineria$redirect_potionAdded(CreativeModeTab.Output instance, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        if(DeferredRegisterUtil.contains(MineriaPotions.POTIONS, PotionUtils.getPotion(stack))) {
            return;
        }
        instance.accept(stack, visibility);
    }
}
