package com.mineria.mod.common.blocks.ritual_table;

import com.mineria.mod.common.blocks.ElementaryStoneBlock;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class RitualTableBlock extends Block
{
    private static BlockPattern shape;
    private static BlockPattern protectionShape;

    public RitualTableBlock()
    {
        super(Properties.of(Material.STONE).strength(-1, 3600000.0F).noDrops());
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
        return MineriaTileEntities.RITUAL_TABLE.get().create();
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result)
    {
        TileEntity tile = world.getBlockEntity(pos);
        if(tile instanceof RitualTableTileEntity)
        {
            RitualTableTileEntity ritualTable = (RitualTableTileEntity) tile;
            boolean emptyPlacedItem = ritualTable.getPlacedItem().isEmpty();
            boolean emptyHeldItem = player.getItemInHand(hand).isEmpty();
            if(emptyPlacedItem && emptyHeldItem)
            {
                ritualTable.tryStartRitual(world, player);
                return ActionResultType.SUCCESS;
            } else if(emptyPlacedItem)
            {
                ritualTable.placeItem(player.getItemInHand(hand));
                player.setItemInHand(hand, ItemStack.EMPTY);
                return ActionResultType.SUCCESS;
            } else if(emptyHeldItem)
            {
                player.setItemInHand(hand, ritualTable.getPlacedItem().copy());
                ritualTable.setPlacedItem(ItemStack.EMPTY);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    public static BlockPattern getOrCreateShape()
    {
        if(shape == null)
            shape = BlockPatternBuilder.start()
                    .aisle("???B???", "???????", "???????", "B?????B", "???????", "???????", "???B???")
                    .aisle("???G???", "???????", "???????", "W??T??F", "???????", "???????", "???A???")
                    .where('?', CachedBlockInfo.hasState(BlockStateMatcher.ANY))
                    .where('B', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.BLUE_GLOWSTONE)))
                    .where('G', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.GROUND_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.SOUTH))))
                    .where('W', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.WATER_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.EAST))))
                    .where('T', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.RITUAL_TABLE)))
                    .where('F', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.FIRE_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.WEST))))
                    .where('A', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.AIR_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.NORTH)))).build();

        return shape;
    }

    public static BlockPattern getOrCreateProtectionShape()
    {
        if(protectionShape == null)
            protectionShape = BlockPatternBuilder.start()
                    .aisle("???G???", "???????", "???????", "W??T??F", "???????", "???????", "???A???")
                    .where('?', CachedBlockInfo.hasState(BlockStateMatcher.ANY))
                    .where('G', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.GROUND_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.SOUTH))))
                    .where('W', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.WATER_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.EAST))))
                    .where('T', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.RITUAL_TABLE)))
                    .where('F', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.FIRE_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.WEST))))
                    .where('A', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(MineriaBlocks.AIR_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.NORTH)))).build();

        return protectionShape;
    }
}
