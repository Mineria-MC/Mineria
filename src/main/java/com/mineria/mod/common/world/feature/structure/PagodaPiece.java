package com.mineria.mod.common.world.feature.structure;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.AsiaticHerbalistEntity;
import com.mineria.mod.common.entity.BuddhistEntity;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaStructures;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class PagodaPiece extends TemplateStructurePiece
{
    private Rotation rotation;

    public PagodaPiece(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn)
    {
        super(MineriaStructures.PMP, 0);
        this.templatePosition = pos;
        this.rotation = rotationIn;
        this.setupPiece(templateManagerIn);
    }

    public PagodaPiece(TemplateManager templateManagerIn, CompoundNBT nbt)
    {
        super(MineriaStructures.PMP, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(TemplateManager templateManager)
    {
        Template template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "pagoda"));
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
        this.setup(template, this.templatePosition, placementsettings);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT tagCompound)
    {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString("Rot", this.rotation.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox boundingBox)
    {
        switch(function)
        {
            case "chest":
                world.removeBlock(pos, false);
                createChest(world, boundingBox, rand, pos, new ResourceLocation(Mineria.MODID, "chests/pagoda_chest"), null);
                break;
            case "flower_pot":
                world.removeBlock(pos, false);
                WeightedList<Block> list = Util.make(new WeightedList<>(), lst -> {
                    lst.add(Blocks.POTTED_JUNGLE_SAPLING, 20);
                    lst.add(Blocks.POTTED_BAMBOO, 20);
                    lst.add(Blocks.POTTED_DARK_OAK_SAPLING, 15);
                    lst.add(Blocks.POTTED_LILY_OF_THE_VALLEY, 10);
                    lst.add(MineriaBlocks.POTTED_SAKURA_SAPLING, 10);
                    lst.add(MineriaBlocks.POTTED_NETTLE, 5);
                    lst.add(MineriaBlocks.POTTED_PLANTAIN, 5);
                    lst.add(MineriaBlocks.POTTED_THYME, 5);
                    lst.add(Blocks.POTTED_WITHER_ROSE, 1);
                });
                world.setBlock(pos, list.getOne(rand).defaultBlockState(), 2);
                break;
            case "buddhist":
                world.removeBlock(pos, false);
                BuddhistEntity buddhist = MineriaEntities.BUDDHIST.get().create(world.getLevel());
                if(buddhist != null)
                {
                    buddhist.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(rand.nextFloat() * 360F), 0);
                    buddhist.yHeadRot = buddhist.yRot;
                    buddhist.yBodyRot = buddhist.yRot;
                    buddhist.finalizeSpawn(world, world.getCurrentDifficultyAt(buddhist.blockPosition()), SpawnReason.STRUCTURE, null, null);
                    world.addFreshEntityWithPassengers(buddhist);
                }
                break;
            case "asiatic_herbalist":
                world.removeBlock(pos, false);
                AsiaticHerbalistEntity asiaticHerbalist = MineriaEntities.ASIATIC_HERBALIST.get().create(world.getLevel());
                if(asiaticHerbalist != null)
                {
                    asiaticHerbalist.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(rand.nextFloat() * 360F), 0);
                    asiaticHerbalist.yHeadRot = asiaticHerbalist.yRot;
                    asiaticHerbalist.yBodyRot = asiaticHerbalist.yRot;
                    asiaticHerbalist.finalizeSpawn(world, world.getCurrentDifficultyAt(asiaticHerbalist.blockPosition()), SpawnReason.STRUCTURE, null, null);
                    world.addFreshEntityWithPassengers(asiaticHerbalist);
                }
                break;
        }
    }

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random)
    {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new PagodaPiece(templateManager, blockpos, rotation));
    }
}
