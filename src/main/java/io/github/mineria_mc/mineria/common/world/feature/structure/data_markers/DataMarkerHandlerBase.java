package io.github.mineria_mc.mineria.common.world.feature.structure.data_markers;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

import static net.minecraft.world.level.levelgen.structure.StructurePiece.reorient;

public abstract class DataMarkerHandlerBase implements IDataMarkerHandler {
    public static DataMarkerHandlerBase none() {
        return new DataMarkerHandlerBase() {
            @Override
            public void handleDataMarker(ServerLevelAccessor level, String function, BlockPos pos, StructureTemplate.StructureBlockInfo info, BlockPos seedPos, BoundingBox box, StructurePoolElement spe, Rotation rotation, RandomSource random) {
            }
        };
    }

    protected static boolean createChest(LevelAccessor level, BoundingBox box, RandomSource rand, BlockPos pos, ResourceLocation lootTable, @Nullable BlockState state) {
        if (box.isInside(pos) && !level.getBlockState(pos).is(Blocks.CHEST)) {
            if (state == null)
                state = reorient(level, pos, Blocks.CHEST.defaultBlockState());

            level.setBlock(pos, state, 2);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ChestBlockEntity chest)
                chest.setLootTable(lootTable, rand.nextLong());

            return true;
        }
        return false;
    }
}
