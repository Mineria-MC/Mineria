package io.github.mineria_mc.mineria.common.world.feature.structure.data_markers;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.blocks.ElementaryStoneBlock;
import io.github.mineria_mc.mineria.common.entity.AbstractDruidEntity;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class RitualStructureDataMarkerHandler extends DataMarkerHandlerBase {
    @Override
    public void handleDataMarker(ServerLevelAccessor level, String function, BlockPos pos, StructureTemplate.StructureBlockInfo info, BlockPos seedPos, BoundingBox box, StructurePoolElement spe, Rotation rotation, RandomSource random) {
        final ImmutableList<EntityType<? extends AbstractDruidEntity>> druids = ImmutableList.of(MineriaEntities.DRUID.get(), MineriaEntities.OVATE.get(), MineriaEntities.BARD.get());

        if (function.equalsIgnoreCase("druid")) {
            level.removeBlock(pos, false);
            AbstractDruidEntity druid = druids.get(random.nextInt(druids.size())).create(level.getLevel());
            if (druid != null) {
                druid.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(random.nextFloat() * 360F), 0);
                druid.yHeadRot = druid.yBodyRot = druid.getYRot();
                druid.finalizeSpawn(level, level.getCurrentDifficultyAt(druid.blockPosition()), MobSpawnType.STRUCTURE, null, null);
//                    checkBlocksNearDruid(level, druid);
                level.addFreshEntityWithPassengers(druid);
            }
        }
    }

    private static void checkBlocksNearDruid(WorldGenLevel level, AbstractDruidEntity druid) {
        BlockPos.betweenClosedStream(druid.getBoundingBox().inflate(2, 0, 2))
                .filter(pos -> !RitualStructureDataMarkerHandler.isValidBlock(level.getBlockState(pos)))
                .forEach(pos -> level.removeBlock(pos, false));
    }

    private static boolean isValidBlock(BlockState state) {
        return state.isAir() || state.getBlock() instanceof ElementaryStoneBlock || state.is(MineriaBlocks.RITUAL_TABLE.get());
    }
}
