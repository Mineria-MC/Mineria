package com.mineria.mod.common.blocks.xp_block;

import com.mineria.mod.common.init.MineriaTileEntities;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class XpBlock extends Block implements EntityBlock
{
    public XpBlock()
    {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(2.5F, 5.0F).sound(SoundType.METAL).requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new XpBlockTileEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return level.isClientSide || blockEntityType != MineriaTileEntities.XP_BLOCK.get() ? null : (pLevel, pPos, pState, pBlockEntity) -> XpBlockTileEntity.serverTick(pLevel, pPos, pState, (XpBlockTileEntity) pBlockEntity);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        XpBlockTileEntity xpTile = (XpBlockTileEntity)worldIn.getBlockEntity(pos);
        if(!worldIn.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayer) player, xpTile, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(tile instanceof XpBlockTileEntity && state.getBlock() != newState.getBlock())
            Containers.dropContents(worldIn, pos, ((XpBlockTileEntity)tile).getInventory().toNonNullList());

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
