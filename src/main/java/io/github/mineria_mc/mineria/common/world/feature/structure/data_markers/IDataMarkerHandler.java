package io.github.mineria_mc.mineria.common.world.feature.structure.data_markers;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public interface IDataMarkerHandler {
    void handleDataMarker(ServerLevelAccessor level, String function, BlockPos pos, StructureTemplate.StructureBlockInfo info, BlockPos seedPos, BoundingBox box, StructurePoolElement spe, Rotation rotation, RandomSource random);
}