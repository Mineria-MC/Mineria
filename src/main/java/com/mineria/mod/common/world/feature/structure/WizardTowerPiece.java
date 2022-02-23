package com.mineria.mod.common.world.feature.structure;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.WizardEntity;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaStructures;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Random;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public class WizardTowerPiece /*extends TemplateStructurePiece*/
{
    /*private Rotation rotation;

    public WizardTowerPiece(StructureManager templateManagerIn, BlockPos pos, Rotation rotationIn)
    {
        super(MineriaStructures.WTP, 0);
        this.templatePosition = pos.below();
        this.rotation = rotationIn;
        this.setupPiece(templateManagerIn);
    }

    public WizardTowerPiece(StructureManager templateManagerIn, CompoundTag nbt)
    {
        super(MineriaStructures.WTP, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(StructureManager templateManager)
    {
        StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
        this.template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "wizard_tower"));
        this.setOrientation(Direction.NORTH);
        this.placeSettings = placementsettings;
        this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition.above());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tagCompound)
    {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString("Rot", this.rotation.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb)
    {
        switch (function)
        {
            case "chest":
                createChest(worldIn, sbb, rand, pos.below(), new ResourceLocation(Mineria.MODID, "chests/wizard_tower_chest"), null);
                worldIn.removeBlock(pos, false);
                break;
            case "flower_pot":
                BlockState state = worldIn.getBlockState(pos.below());
                if(state.is(Blocks.FLOWER_POT))
                    worldIn.setBlock(pos.below(), rand.nextBoolean() ? MineriaBlocks.POTTED_SPRUCE_YEW_SAPLING.defaultBlockState() : MineriaBlocks.POTTED_SAKURA_SAPLING.defaultBlockState(), 2);
                worldIn.removeBlock(pos, false);
                break;
            case "lectern":
                BlockEntity tile = worldIn.getBlockEntity(pos.below());
                if(tile instanceof LecternBlockEntity)
                {
                    LecternBlockEntity lectern = (LecternBlockEntity) tile;
                    worldIn.setBlock(pos.below(), worldIn.getBlockState(pos.below()).setValue(LecternBlock.HAS_BOOK, true), 2);
                    lectern.setBook(Util.make(new ItemStack(Items.WRITTEN_BOOK), stack -> {
                        CompoundTag nbt = new CompoundTag();
                        nbt.putString("title", ChatFormatting.ITALIC + "*No Title*");
                        nbt.putString("author", "A Random Wizard");
                        nbt.put("pages", generateBook(rand));
                        nbt.putBoolean("resolved", true);
                        stack.setTag(nbt);
                    }));
                }
                worldIn.removeBlock(pos, false);
                break;
            case "wizard":
                worldIn.removeBlock(pos, false);
                if(rand.nextInt(3) == 0)
                {
                    WizardEntity entity = MineriaEntities.WIZARD.get().create(worldIn.getLevel());
                    if(entity != null)
                    {
                        entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(rand.nextFloat() * 360F), 0);
                        entity.yHeadRot = entity.yRot;
                        entity.yBodyRot = entity.yRot;
                        entity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                        worldIn.addFreshEntityWithPassengers(entity);
                    }
                }
                break;
        }
    }

    public static ListTag generateBook(Random random)
    {
        ListTag nbt = new ListTag();
        for(int pageIndex = 0; pageIndex < 2 + random.nextInt(10); pageIndex++)
        {
            String format = random.nextInt(14) == 0 ? ChatFormatting.GOLD.toString().concat(ChatFormatting.OBFUSCATED.toString()) : ChatFormatting.OBFUSCATED.toString();
            nbt.add(StringTag.valueOf(format.concat(RandomStringUtils.random(random.nextInt(600) + 40))));
        }
        return nbt;
    }

    public static void start(StructureManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random)
    {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new WizardTowerPiece(templateManager, blockpos, rotation));
    }*/
}
