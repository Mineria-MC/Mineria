package com.mineria.mod.common.world.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.AbstractDruidEntity;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaStructures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class RitualStructurePiece extends TemplateStructurePiece
{
    public RitualStructurePiece(TemplateManager templateManagerIn, BlockPos pos)
    {
        super(MineriaStructures.RSP, 0);
        this.templatePosition = pos;
        this.setupPiece(templateManagerIn);
    }

    public RitualStructurePiece(TemplateManager templateManagerIn, CompoundNBT nbt)
    {
        super(MineriaStructures.RSP, nbt);
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(TemplateManager templateManager)
    {
        this.template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "ritual_structure"));
        this.setOrientation(Direction.NORTH);
        this.placeSettings = new PlacementSettings();
        this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition.above());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox boundingBox)
    {
        final ImmutableList<EntityType<? extends AbstractDruidEntity>> druids = ImmutableList.of(MineriaEntities.DRUID.get(), MineriaEntities.OVATE.get(), MineriaEntities.BARD.get());

        if(function.equalsIgnoreCase("druid"))
        {
            world.removeBlock(pos, false);
            AbstractDruidEntity druid = druids.get(rand.nextInt(druids.size())).create(world.getLevel());
            if(druid != null)
            {
                druid.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(rand.nextFloat() * 360F), 0);
                druid.yHeadRot = druid.yRot;
                druid.yBodyRot = druid.yRot;
                druid.finalizeSpawn(world, world.getCurrentDifficultyAt(druid.blockPosition()), SpawnReason.STRUCTURE, null, null);
                world.addFreshEntityWithPassengers(druid);
            }
        }
    }

    public static void start(TemplateManager templateManager, BlockPos pos, List<StructurePiece> pieceList, Random random)
    {
        pieceList.add(new RitualStructurePiece(templateManager, pos));
    }
}
