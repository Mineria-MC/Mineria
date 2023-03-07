package io.github.mineria_mc.mineria.common.world.feature.structure.data_markers;

import io.github.mineria_mc.mineria.common.data.MineriaLootTables;
import io.github.mineria_mc.mineria.common.entity.AsiaticHerbalistEntity;
import io.github.mineria_mc.mineria.common.entity.BuddhistEntity;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class PagodaDataMarkerHandler extends DataMarkerHandlerBase {
    @Override
    public void handleDataMarker(ServerLevelAccessor level, String function, BlockPos pos, StructureTemplate.StructureBlockInfo info, BlockPos seedPos, BoundingBox box, StructurePoolElement spe, Rotation rotation, RandomSource random) {
        switch (function) {
            case "chest" -> {
                level.removeBlock(pos, false);
                createChest(level, box, random, pos, MineriaLootTables.PAGODA_CHEST, null);
            }
            case "flower_pot" -> {
                level.removeBlock(pos, false);
                SimpleWeightedRandomList<Block> list = new SimpleWeightedRandomList.Builder<Block>()
                        .add(Blocks.POTTED_JUNGLE_SAPLING, 20)
                        .add(Blocks.POTTED_BAMBOO, 20)
                        .add(Blocks.POTTED_DARK_OAK_SAPLING, 15)
                        .add(Blocks.POTTED_LILY_OF_THE_VALLEY, 10)
                        .add(MineriaBlocks.POTTED_SAKURA_SAPLING.get(), 10)
                        .add(MineriaBlocks.POTTED_NETTLE.get(), 5)
                        .add(MineriaBlocks.POTTED_PLANTAIN.get(), 5)
                        .add(MineriaBlocks.POTTED_THYME.get(), 5)
                        .add(Blocks.POTTED_WITHER_ROSE, 1).build();
                list.getRandomValue(random).ifPresent(block -> level.setBlock(pos, block.defaultBlockState(), 2));
            }
            case "buddhist" -> {
                level.removeBlock(pos, false);
                BuddhistEntity buddhist = MineriaEntities.BUDDHIST.get().create(level.getLevel());
                if (buddhist != null) {
                    buddhist.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(random.nextFloat() * 360F), 0);
                    buddhist.yHeadRot = buddhist.yBodyRot = buddhist.getYRot();
                    buddhist.finalizeSpawn(level, level.getCurrentDifficultyAt(buddhist.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                    level.addFreshEntityWithPassengers(buddhist);
                }
            }
            case "asiatic_herbalist" -> {
                level.removeBlock(pos, false);
                AsiaticHerbalistEntity asiaticHerbalist = MineriaEntities.ASIATIC_HERBALIST.get().create(level.getLevel());
                if (asiaticHerbalist != null) {
                    asiaticHerbalist.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(random.nextFloat() * 360F), 0);
                    asiaticHerbalist.yHeadRot = asiaticHerbalist.yBodyRot = asiaticHerbalist.getYRot();
                    asiaticHerbalist.finalizeSpawn(level, level.getCurrentDifficultyAt(asiaticHerbalist.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                    level.addFreshEntityWithPassengers(asiaticHerbalist);
                }
            }
        }
    }
}
