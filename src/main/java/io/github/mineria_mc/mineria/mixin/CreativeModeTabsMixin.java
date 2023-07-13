package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    @Unique
    private static final Set<ResourceKey<Potion>> MINERIA_POTIONS = MineriaPotions.POTIONS.getEntries().stream().map(MineriaUtils::key).collect(Collectors.toSet());

    @Redirect(method = "generatePotionEffectTypes", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderLookup;listElements()Ljava/util/stream/Stream;"))
    private static Stream<Holder.Reference<Potion>> mineria$redirect_listElements(HolderLookup<Potion> lookup) {
        return lookup.listElements().filter(potion -> !potion.is(MINERIA_POTIONS::contains));
    }
}
