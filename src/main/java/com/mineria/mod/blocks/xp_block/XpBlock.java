package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.init.TileEntitiesInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

public class XpBlock extends Block
{
    public XpBlock()
    {
        super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2.5F, 5.0F).sound(SoundType.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE));
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
        return TileEntitiesInit.XP_BLOCK.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        XpBlockTileEntity xpTile = (XpBlockTileEntity)worldIn.getTileEntity(pos);
        if(!worldIn.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity) player, xpTile, pos);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof XpBlockTileEntity && state.getBlock() != newState.getBlock())
            InventoryHelper.dropItems(worldIn, pos, ((XpBlockTileEntity)tile).getInventory().toNonNullList());

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
