package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.enchantments.FourElementsEnchantment;
import io.github.mineria_mc.mineria.common.init.MineriaEnchantments;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin(@Nullable MenuType<?> type, int windowId, Inventory playerInv, ContainerLevelAccess worldPosCallable) {
        super(type, windowId, playerInv, worldPosCallable);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void mineria$inject_createResult(CallbackInfo ci) {
        ItemStack toRepair = this.inputSlots.getItem(0);
        ItemStack repairItem = this.inputSlots.getItem(1);

        if (toRepair.is(MineriaItems.KUNAI.get()) && ItemStack.isSameItem(toRepair, repairItem)) {
            ci.cancel();
        }
    }

    @Unique
    private final Random mineria$rand = new Random();
    @Unique
    private static List<FourElementsEnchantment> mineria$enchantments;

    @Redirect(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V"))
    private void mineria$redirect_replaceFourElements(Map<Enchantment, Integer> map, ItemStack stack) {
        mineria$rand.setSeed(this.player.getEnchantmentSeed());

        if (map.containsKey(MineriaEnchantments.FOUR_ELEMENTS.get())) {
            map.remove(MineriaEnchantments.FOUR_ELEMENTS.get());
            if (mineria$enchantments == null) {
                mineria$enchantments = Stream.of(MineriaEnchantments.FIRE_ELEMENT, MineriaEnchantments.WATER_ELEMENT, MineriaEnchantments.AIR_ELEMENT, MineriaEnchantments.GROUND_ELEMENT).map(RegistryObject::get).toList();
            }
            map.put(mineria$enchantments.get(mineria$rand.nextInt(mineria$enchantments.size())), 1);
        }
        EnchantmentHelper.setEnchantments(map, stack);
    }
}
