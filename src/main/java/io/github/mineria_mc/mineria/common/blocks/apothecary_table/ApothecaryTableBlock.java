package io.github.mineria_mc.mineria.common.blocks.apothecary_table;

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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ApothecaryTableBlock extends Block implements EntityBlock {
    public ApothecaryTableBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava().strength(3.5F).sound(SoundType.WOOD));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ApothecaryTableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide || blockEntityType != MineriaBlockEntities.APOTHECARY_TABLE.get() ? null : (pLevel, pPos, pState, pBlockEntity) -> ((ApothecaryTableBlockEntity) pBlockEntity).serverTick(level);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!world.isClientSide) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof ApothecaryTableBlockEntity)
                NetworkHooks.openScreen((ServerPlayer) player, (ApothecaryTableBlockEntity) tile, pos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean hasFlags) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof ApothecaryTableBlockEntity)
            Containers.dropContents(world, pos, ((ApothecaryTableBlockEntity) tile).getInventory().toNonNullList());

        super.onRemove(state, world, pos, newState, hasFlags);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}
