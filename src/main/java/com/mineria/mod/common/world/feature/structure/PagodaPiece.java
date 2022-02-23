package com.mineria.mod.common.world.feature.structure;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.AsiaticHerbalistEntity;
import com.mineria.mod.common.entity.BuddhistEntity;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaStructures;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Random;

public class PagodaPiece /*extends TemplateStructurePiece*/
{
    /*private Rotation rotation;

    public PagodaPiece(StructureManager templateManagerIn, BlockPos pos, Rotation rotationIn)
    {
        super(MineriaStructures.PMP, 0);
        this.templatePosition = pos;
        this.rotation = rotationIn;
        this.setupPiece(templateManagerIn);
    }

    public PagodaPiece(StructureManager templateManagerIn, CompoundTag nbt)
    {
        super(MineriaStructures.PMP, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(StructureManager templateManager)
    {
        StructureTemplate template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "pagoda"));
        StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
        this.setup(template, this.templatePosition, placementsettings);
    }

    @Override
    protected void addAdditionalSaveData(ServerLevel pLevel, CompoundTag tagCompound)
    {
        super.addAdditionalSaveData(pLevel, tagCompound);
        tagCompound.putString("Rot", this.rotation.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor world, Random rand, BoundingBox boundingBox)
    {
        switch(function)
        {
            case "chest":
                world.removeBlock(pos, false);
                createChest(world, boundingBox, rand, pos, new ResourceLocation(Mineria.MODID, "chests/pagoda_chest"), null);
                break;
            case "flower_pot":
                world.removeBlock(pos, false);
                SimpleWeightedRandomList<Block> list = new SimpleWeightedRandomList.Builder<Block>()
                        .add(Blocks.POTTED_JUNGLE_SAPLING, 20)
                        .add(Blocks.POTTED_BAMBOO, 20)
                        .add(Blocks.POTTED_DARK_OAK_SAPLING, 15)
                        .add(Blocks.POTTED_LILY_OF_THE_VALLEY, 10)
                        .add(MineriaBlocks.POTTED_SAKURA_SAPLING, 10)
                        .add(MineriaBlocks.POTTED_NETTLE, 5)
                        .add(MineriaBlocks.POTTED_PLANTAIN, 5)
                        .add(MineriaBlocks.POTTED_THYME, 5)
                        .add(Blocks.POTTED_WITHER_ROSE, 1).build();
                list.getRandomValue(rand).ifPresent(block -> world.setBlock(pos, block.defaultBlockState(), 2));
                break;
            case "buddhist":
                world.removeBlock(pos, false);
                BuddhistEntity buddhist = MineriaEntities.BUDDHIST.get().create(world.getLevel());
                if(buddhist != null)
                {
                    buddhist.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(rand.nextFloat() * 360F), 0);
                    buddhist.yHeadRot = buddhist.getYRot();
                    buddhist.yBodyRot = buddhist.getYRot();
                    buddhist.finalizeSpawn(world, world.getCurrentDifficultyAt(buddhist.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                    world.addFreshEntityWithPassengers(buddhist);
                }
                break;
            case "asiatic_herbalist":
                world.removeBlock(pos, false);
                AsiaticHerbalistEntity asiaticHerbalist = MineriaEntities.ASIATIC_HERBALIST.get().create(world.getLevel());
                if(asiaticHerbalist != null)
                {
                    asiaticHerbalist.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(rand.nextFloat() * 360F), 0);
                    asiaticHerbalist.yHeadRot = asiaticHerbalist.getYRot();
                    asiaticHerbalist.yBodyRot = asiaticHerbalist.getYRot();
                    asiaticHerbalist.finalizeSpawn(world, world.getCurrentDifficultyAt(asiaticHerbalist.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                    world.addFreshEntityWithPassengers(asiaticHerbalist);
                }
                break;
        }
    }

    public static void start(StructureManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random)
    {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new PagodaPiece(templateManager, blockpos, rotation));
    }*/
}
