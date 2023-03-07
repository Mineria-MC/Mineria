package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.util.HopperHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanillaInventoryCodeHooks.class)
public class VanillaInventoryCodeHooksMixin {
    @Inject(method = "insertStack", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onStackInsertion(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot, CallbackInfoReturnable<ItemStack> cir) {
        if (destInventory instanceof HopperHandler handler && !handler.canInsertHopperItem(slot, stack)) {
            cir.setReturnValue(stack);
        }
    }

    @Redirect(method = "lambda$extractHook$0", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;extractItem(IIZ)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), remap = false)
    private static ItemStack onStackExtract(IItemHandler instance, int index, int amount, boolean simulate) {
        ItemStack stack = instance.getStackInSlot(index);

        if (instance instanceof HopperHandler handler && !handler.canExtractHopperItem(index, stack)) {
            return ItemStack.EMPTY;
        }

        return instance.extractItem(index, amount, simulate);
    }
}
