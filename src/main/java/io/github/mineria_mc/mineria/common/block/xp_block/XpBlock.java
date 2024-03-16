package io.github.mineria_mc.mineria.common.block.xp_block;

import io.github.mineria_mc.mineria.common.registries.MineriaBlockEntityRegistry;
import net.minecraft.core.BlockPos;
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

@SuppressWarnings("deprecation")
public class XpBlock extends Block implements EntityBlock {

    public XpBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5F, 5.0F).sound(SoundType.METAL).requiresCorrectToolForDrops());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new XpBlockEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide || blockEntityType != MineriaBlockEntityRegistry.XP_BLOCK ? null : (pLevel, pPos, pState, pBlockEntity) -> XpBlockEntity.serverTick(pLevel, pPos, pState, (XpBlockEntity)pBlockEntity);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        XpBlockEntity xpTile = (XpBlockEntity)level.getBlockEntity(blockPos);
        if(!level.isClientSide) {
            player.openMenu(xpTile);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        BlockEntity tile = level.getBlockEntity(blockPos);
        if(tile instanceof XpBlockEntity && blockState.getBlock() != newState.getBlock())
            Containers.dropContents(level, blockPos, ((XpBlockEntity)tile).getInventory());
        
        super.onRemove(blockState, level, blockPos, newState, isMoving);
    }
}
