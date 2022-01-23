package com.mineria.mod.common.blocks.apothecary_table;

import com.mineria.mod.common.blocks.MineriaBlock;
import com.mineria.mod.common.init.MineriaTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ApothecaryTableBlock extends MineriaBlock
{
    public ApothecaryTableBlock()
    {
        super(Material.METAL, 3.5F, 3.5F, SoundType.WOOD, 0, ToolType.AXE);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return MineriaTileEntities.APOTHECARY_TABLE.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        if(!world.isClientSide)
        {
            TileEntity tile = world.getBlockEntity(pos);
            if(tile instanceof ApothecaryTableTileEntity)
                NetworkHooks.openGui((ServerPlayerEntity) player, (ApothecaryTableTileEntity) tile, pos);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean hasFlags)
    {
        TileEntity tile = world.getBlockEntity(pos);
        if(tile instanceof ApothecaryTableTileEntity)
            InventoryHelper.dropContents(world, pos, ((ApothecaryTableTileEntity) tile).getInventory().toNonNullList());

        super.onRemove(state, world, pos, newState, hasFlags);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }
}
