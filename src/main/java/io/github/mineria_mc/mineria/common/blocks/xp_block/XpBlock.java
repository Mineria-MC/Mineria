package io.github.mineria_mc.mineria.common.blocks.xp_block;

import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class XpBlock extends Block implements EntityBlock {
    public XpBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5F, 5.0F).sound(SoundType.METAL).requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new XpBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide || blockEntityType != MineriaBlockEntities.XP_BLOCK.get() ? null : (pLevel, pPos, pState, pBlockEntity) -> XpBlockEntity.serverTick(pLevel, pPos, pState, (XpBlockEntity) pBlockEntity);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        XpBlockEntity xpTile = (XpBlockEntity) worldIn.getBlockEntity(pos);
        if (!worldIn.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, xpTile, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof XpBlockEntity && state.getBlock() != newState.getBlock())
            Containers.dropContents(worldIn, pos, ((XpBlockEntity) tile).getInventory().toNonNullList());

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
