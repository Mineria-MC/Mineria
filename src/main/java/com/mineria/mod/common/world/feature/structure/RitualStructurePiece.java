package com.mineria.mod.common.world.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.AbstractDruidEntity;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaStructures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;
import java.util.Random;

public class RitualStructurePiece /*extends TemplateStructurePiece*/
{
    /*public RitualStructurePiece(StructureManager templateManagerIn, BlockPos pos)
    {
        super(MineriaStructures.RSP, 0);
        this.templatePosition = pos;
        this.setupPiece(templateManagerIn);
    }

    public RitualStructurePiece(StructureManager templateManagerIn, CompoundTag nbt)
    {
        super(MineriaStructures.RSP, nbt);
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(StructureManager templateManager)
    {
        this.template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "ritual_structure"));
        this.setOrientation(Direction.NORTH);
        this.placeSettings = new StructurePlaceSettings();
        this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition.above());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor world, Random rand, BoundingBox boundingBox)
    {
        final ImmutableList<EntityType<? extends AbstractDruidEntity>> druids = ImmutableList.of(MineriaEntities.DRUID.get(), MineriaEntities.OVATE.get(), MineriaEntities.BARD.get());

        if(function.equalsIgnoreCase("druid"))
        {
            world.removeBlock(pos, false);
            AbstractDruidEntity druid = druids.get(rand.nextInt(druids.size())).create(world.getLevel());
            if(druid != null)
            {
                druid.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(rand.nextFloat() * 360F), 0);
                druid.yHeadRot = druid.yRot;
                druid.yBodyRot = druid.yRot;
                druid.finalizeSpawn(world, world.getCurrentDifficultyAt(druid.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                world.addFreshEntityWithPassengers(druid);
            }
        }
    }

    public static void start(StructureManager templateManager, BlockPos pos, List<StructurePiece> pieceList, Random random)
    {
        pieceList.add(new RitualStructurePiece(templateManager, pos));
    }*/
}
