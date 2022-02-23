package com.mineria.mod.mixin;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(PotionItem.class)
public class PotionItemMixin
{
    @Inject(method = "fillItemCategory", at = @At("TAIL"))
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks, CallbackInfo ci)
    {
        List<ItemStack> filteredStacks = stacks.stream().filter(stack -> !PotionUtils.getPotion(stack).getRegistryName().getNamespace().equals("mineria")).toList();
        stacks.clear();
        stacks.addAll(filteredStacks);
    }
}
