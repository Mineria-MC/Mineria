package com.mineria.mod.common.blocks.ritual_table;

import com.mineria.mod.common.blocks.ElementaryStoneBlock;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class RitualTableBlock extends Block implements EntityBlock
{
    private static BlockPattern shape;
    private static BlockPattern protectionShape;

    public RitualTableBlock()
    {
        super(Properties.of(Material.STONE).strength(-1, 3600000.0F).noDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new RitualTableTileEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return blockEntityType != MineriaTileEntities.RITUAL_TABLE.get() ? null : (pLevel, pPos, pState, pBlockEntity) -> RitualTableTileEntity.tick(pLevel, pPos, pState, (RitualTableTileEntity) pBlockEntity);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if(tile instanceof RitualTableTileEntity)
        {
            RitualTableTileEntity ritualTable = (RitualTableTileEntity) tile;
            boolean emptyPlacedItem = ritualTable.getPlacedItem().isEmpty();
            boolean emptyHeldItem = player.getItemInHand(hand).isEmpty();
            if(emptyPlacedItem && emptyHeldItem)
            {
                ritualTable.tryStartRitual(world, player);
                return InteractionResult.SUCCESS;
            } else if(emptyPlacedItem)
            {
                ritualTable.placeItem(player.getItemInHand(hand));
                player.setItemInHand(hand, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            } else if(emptyHeldItem)
            {
                player.setItemInHand(hand, ritualTable.getPlacedItem().copy());
                ritualTable.setPlacedItem(ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static BlockPattern getOrCreateShape()
    {
        if(shape == null)
            shape = BlockPatternBuilder.start()
                    .aisle("???B???", "???????", "???????", "B?????B", "???????", "???????", "???B???")
                    .aisle("???G???", "???????", "???????", "W??T??F", "???????", "???????", "???A???")
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .where('B', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.BLUE_GLOWSTONE)))
                    .where('G', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.GROUND_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.SOUTH))))
                    .where('W', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.WATER_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.EAST))))
                    .where('T', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.RITUAL_TABLE)))
                    .where('F', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.FIRE_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.WEST))))
                    .where('A', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.AIR_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.NORTH)))).build();

        return shape;
    }

    public static BlockPattern getOrCreateProtectionShape()
    {
        if(protectionShape == null)
            protectionShape = BlockPatternBuilder.start()
                    .aisle("???G???", "???????", "???????", "W??T??F", "???????", "???????", "???A???")
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .where('G', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.GROUND_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.SOUTH))))
                    .where('W', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.WATER_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.EAST))))
                    .where('T', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.RITUAL_TABLE)))
                    .where('F', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.FIRE_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.WEST))))
                    .where('A', BlockInWorld.hasState(BlockStatePredicate.forBlock(MineriaBlocks.AIR_ELEMENTARY_STONE).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.NORTH)))).build();

        return protectionShape;
    }
}
