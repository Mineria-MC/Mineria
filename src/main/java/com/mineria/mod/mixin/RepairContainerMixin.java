package com.mineria.mod.mixin;

import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fml.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(RepairContainer.class)
public abstract class RepairContainerMixin extends AbstractRepairContainer
{
    public RepairContainerMixin(@Nullable ContainerType<?> type, int windowId, PlayerInventory playerInv, IWorldPosCallable worldPosCallable)
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
    private static List<Enchantment> enchantments;

    @Redirect(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;setEnchantments(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"))
    private void replaceFourElements(Map<Enchantment, Integer> map, ItemStack stack)
    {
        rand.setSeed(this.player.getEnchantmentSeed());

        if(map.containsKey(MineriaEnchantments.FOUR_ELEMENTS.get()))
        {
            map.remove(MineriaEnchantments.FOUR_ELEMENTS.get());
            if(enchantments == null)
                enchantments = Stream.of(MineriaEnchantments.FIRE_ELEMENT, MineriaEnchantments.WATER_ELEMENT, MineriaEnchantments.AIR_ELEMENT, MineriaEnchantments.GROUND_ELEMENT).map(RegistryObject::get).collect(Collectors.toList());
            map.put(enchantments.get(rand.nextInt(enchantments.size())), 1);
        }
        EnchantmentHelper.setEnchantments(map, stack);
    }
}
