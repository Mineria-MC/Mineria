package com.mineria.mod.mixin;

import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Block.class)
public class BlockMixin
{
    @Inject(method = "playerDestroy", at = @At("TAIL"))
    public void playerDestroy(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tile, ItemStack stack, CallbackInfo ci)
    {
        if(player instanceof ServerPlayerEntity)
        {
            MineriaCriteriaTriggers.MINED_BLOCK.trigger((ServerPlayerEntity) player, state, pos, stack);
        }
    }
}
