package com.mineria.mod.common.world.feature.structure;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.WizardEntity;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaStructures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Random;

public class WizardTowerPiece extends TemplateStructurePiece
{
    private Rotation rotation;

    public WizardTowerPiece(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn)
    {
        super(MineriaStructures.WTP, 0);
        this.templatePosition = pos.below();
        this.rotation = rotationIn;
        this.setupPiece(templateManagerIn);
    }

    public WizardTowerPiece(TemplateManager templateManagerIn, CompoundNBT nbt)
    {
        super(MineriaStructures.WTP, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(TemplateManager templateManager)
    {
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
        this.template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "wizard_tower"));
        this.setOrientation(Direction.NORTH);
        this.placeSettings = placementsettings;
        this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition.above());
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT tagCompound)
    {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString("Rot", this.rotation.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb)
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
                TileEntity tile = worldIn.getBlockEntity(pos.below());
                if(tile instanceof LecternTileEntity)
                {
                    LecternTileEntity lectern = (LecternTileEntity) tile;
                    worldIn.setBlock(pos.below(), worldIn.getBlockState(pos.below()).setValue(LecternBlock.HAS_BOOK, true), 2);
                    lectern.setBook(Util.make(new ItemStack(Items.WRITTEN_BOOK), stack -> {
                        CompoundNBT nbt = new CompoundNBT();
                        nbt.putString("title", TextFormatting.ITALIC + "*No Title*");
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
                        entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(rand.nextFloat() * 360F), 0);
                        entity.yHeadRot = entity.yRot;
                        entity.yBodyRot = entity.yRot;
                        entity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.STRUCTURE, null, null);
                        worldIn.addFreshEntityWithPassengers(entity);
                    }
                }
                break;
        }
    }

    public static ListNBT generateBook(Random random)
    {
        ListNBT nbt = new ListNBT();
        for(int pageIndex = 0; pageIndex < 2 + random.nextInt(10); pageIndex++)
        {
            String format = random.nextInt(14) == 0 ? TextFormatting.GOLD.toString().concat(TextFormatting.OBFUSCATED.toString()) : TextFormatting.OBFUSCATED.toString();
            nbt.add(StringNBT.valueOf(format.concat(RandomStringUtils.random(random.nextInt(600) + 40))));
        }
        return nbt;
    }

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random)
    {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new WizardTowerPiece(templateManager, blockpos, rotation));
    }
}
