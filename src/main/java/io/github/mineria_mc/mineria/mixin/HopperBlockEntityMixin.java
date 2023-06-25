package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.util.HopperHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Inject(method = "canPlaceItemInContainer", at = @At("HEAD"), cancellable = true)
    private static void hopperInsertHook(Container container, ItemStack stack, int index, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (container instanceof HopperHandler handler) {
            cir.setReturnValue(handler.canInsertHopperItem(index, stack));
        }
    }

    @Inject(method = "canTakeItemFromContainer", at = @At("HEAD"), cancellable = true)
    private static void hopperExtractHook(Container hopper, Container container, ItemStack stack, int index, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (container instanceof HopperHandler handler) {
            cir.setReturnValue(handler.canExtractHopperItem(index, stack));
        }
    }
}
