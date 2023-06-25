package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import io.github.mineria_mc.mineria.util.DeferredRegisterUtil;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    private static final Set<ResourceKey<Potion>> MINERIA_POTIONS = MineriaPotions.POTIONS.getEntries().stream().map(MineriaUtils::key).collect(Collectors.toSet());

    @Inject(method = "lambda$generatePotionEffectTypes$35", at = @At("HEAD"), cancellable = true)
    private static void mineria$inject_filterPotions(Holder.Reference<Potion> potion, CallbackInfoReturnable<Boolean> cir) {
        if(potion.is(MINERIA_POTIONS::contains)) {
            cir.setReturnValue(false);
        }
    }
}
