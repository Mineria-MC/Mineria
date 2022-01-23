package com.mineria.mod.mixin;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
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
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks, CallbackInfo ci)
    {
        List<ItemStack> filteredStacks = stacks.stream().filter(stack -> !PotionUtils.getPotion(stack).getRegistryName().getNamespace().equals("mineria")).collect(Collectors.toList());
        stacks.clear();
        stacks.addAll(filteredStacks);
    }
}
