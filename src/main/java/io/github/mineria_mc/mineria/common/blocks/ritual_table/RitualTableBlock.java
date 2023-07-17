package io.github.mineria_mc.mineria.common.blocks.ritual_table;

import io.github.mineria_mc.mineria.common.blocks.ElementaryStoneBlock;
import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
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
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.state.pattern.BlockInWorld.hasState;
import static net.minecraft.world.level.block.state.predicate.BlockStatePredicate.forBlock;

public class RitualTableBlock extends Block implements EntityBlock {
    private static BlockPattern shape;
    private static BlockPattern protectionShape;

    public RitualTableBlock() {
        super(Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(-1, 3600000.0F).noLootTable());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new RitualTableBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return blockEntityType != MineriaBlockEntities.RITUAL_TABLE.get() ? null : (level1, pos, state1, blockEntity) -> {
            if(level1.isClientSide()) {
                ((RitualTableBlockEntity) blockEntity).clientTick(level1, pos);
            } else {
                ((RitualTableBlockEntity) blockEntity).serverTick(level1, pos);
            }
        };
    }

    @Override
    public boolean triggerEvent(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(id, param);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof RitualTableBlockEntity ritualTable) {
            boolean emptyPlacedItem = ritualTable.getPlacedItem().isEmpty();
            boolean emptyHeldItem = player.getItemInHand(hand).isEmpty();

            if (emptyPlacedItem && emptyHeldItem) {
                ritualTable.tryStartBossRitual(world, pos, player);
                return InteractionResult.SUCCESS;
            } else if (emptyPlacedItem) {
                ritualTable.placeItem(world, player.getItemInHand(hand));
                player.setItemInHand(hand, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            } else if (emptyHeldItem) {
                player.setItemInHand(hand, ritualTable.getPlacedItem().copy());
                ritualTable.placeItem(world, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static BlockPattern getOrCreateShape() {
        if (shape == null)
            shape = BlockPatternBuilder.start()
                    .aisle("???B???", "???????", "???????", "B?????B", "???????", "???????", "???B???")
                    .aisle("???G???", "???????", "???????", "W??T??F", "???????", "???????", "???A???")
                    .where('?', hasState(BlockStatePredicate.ANY))
                    .where('B', hasState(forBlock(MineriaBlocks.BLUE_GLOWSTONE.get())))
                    .where('G', hasState(forBlock(MineriaBlocks.GROUND_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.SOUTH))))
                    .where('W', hasState(forBlock(MineriaBlocks.WATER_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.EAST))))
                    .where('T', hasState(forBlock(MineriaBlocks.RITUAL_TABLE.get())))
                    .where('F', hasState(forBlock(MineriaBlocks.FIRE_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.WEST))))
                    .where('A', hasState(forBlock(MineriaBlocks.AIR_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.NORTH))))
                    .build();

        return shape;
    }

    public static BlockPattern getOrCreateProtectionShape() {
        if (protectionShape == null)
            protectionShape = BlockPatternBuilder.start()
                    .aisle("???G???", "???????", "???????", "W??T??F", "???????", "???????", "???A???")
                    .where('?', hasState(BlockStatePredicate.ANY))
                    .where('G', hasState(forBlock(MineriaBlocks.GROUND_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.SOUTH))))
                    .where('W', hasState(forBlock(MineriaBlocks.WATER_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.EAST))))
                    .where('T', hasState(forBlock(MineriaBlocks.RITUAL_TABLE.get())))
                    .where('F', hasState(forBlock(MineriaBlocks.FIRE_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.WEST))))
                    .where('A', hasState(forBlock(MineriaBlocks.AIR_ELEMENTARY_STONE.get()).where(ElementaryStoneBlock.FACING, Predicate.isEqual(Direction.NORTH))))
                    .build();

        return protectionShape;
    }
}
