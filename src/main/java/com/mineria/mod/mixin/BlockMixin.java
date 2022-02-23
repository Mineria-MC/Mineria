package com.mineria.mod.mixin;

import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Block.class)
public class BlockMixin
{
    @Inject(method = "playerDestroy", at = @At("TAIL"))
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity tile, ItemStack stack, CallbackInfo ci)
    {
        if(player instanceof ServerPlayer)
        {
            MineriaCriteriaTriggers.MINED_BLOCK.trigger((ServerPlayer) player, state, pos, stack);
        }
    }
}
