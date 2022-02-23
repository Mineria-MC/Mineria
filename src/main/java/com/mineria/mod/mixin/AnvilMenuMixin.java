package com.mineria.mod.mixin;

import com.mineria.mod.common.enchantments.FourElementsEnchantment;
import com.mineria.mod.common.init.MineriaEnchantments;
import com.mineria.mod.common.init.MineriaItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu
{
    public AnvilMenuMixin(@Nullable MenuType<?> type, int windowId, Inventory playerInv, ContainerLevelAccess worldPosCallable)
    {
        super(type, windowId, playerInv, worldPosCallable);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void createResult(CallbackInfo ci)
    {
        ItemStack toRepair = this.inputSlots.getItem(0);
        ItemStack repairItem = this.inputSlots.getItem(1);

        if(toRepair.getItem().equals(MineriaItems.KUNAI) && toRepair.sameItem(repairItem))
            ci.cancel();
    }

    private final Random rand = new Random();
    private static List<FourElementsEnchantment> enchantments;

    @Redirect(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V"))
    private void replaceFourElements(Map<Enchantment, Integer> map, ItemStack stack)
    {
        rand.setSeed(this.player.getEnchantmentSeed());

        if(map.containsKey(MineriaEnchantments.FOUR_ELEMENTS.get()))
        {
            map.remove(MineriaEnchantments.FOUR_ELEMENTS.get());
            if(enchantments == null)
                enchantments = Stream.of(MineriaEnchantments.FIRE_ELEMENT, MineriaEnchantments.WATER_ELEMENT, MineriaEnchantments.AIR_ELEMENT, MineriaEnchantments.GROUND_ELEMENT).map(RegistryObject::get).toList();
            map.put(enchantments.get(rand.nextInt(enchantments.size())), 1);
        }
        EnchantmentHelper.setEnchantments(map, stack);
    }
}
