package com.mineria.mod.common.world.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.TemporaryItemFrameEntity;
import com.mineria.mod.common.entity.WizardEntity;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaPotions;
import com.mineria.mod.common.init.MineriaStructures;
import com.mineria.mod.common.items.MineriaPotionItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public class WizardLaboratoryPiece /*extends TemplateStructurePiece*/
{
    /*private static final Logger LOGGER = LogManager.getLogger();
    private Rotation rotation;

    public WizardLaboratoryPiece(StructureManager templateManagerIn, BlockPos pos, Rotation rotationIn)
    {
        super(MineriaStructures.WLP, 0);
        this.templatePosition = pos;
        this.rotation = rotationIn;
        this.setupPiece(templateManagerIn);
    }

    public WizardLaboratoryPiece(StructureManager templateManagerIn, CompoundTag nbt)
    {
        super(MineriaStructures.WLP, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(StructureManager templateManager)
    {
        StructureTemplate template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "wizard_laboratory"));
        StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
        this.setup(template, this.templatePosition, placementsettings);
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
        List<Pair<Potion, Boolean>> potions = ImmutableList.of(Pair.of(Potions.HEALING, false), Pair.of(MineriaPotions.CLASS_2_POISON.get(), true),
                Pair.of(Potions.INVISIBILITY, false), Pair.of(Potions.SLOWNESS, true), Pair.of(Potions.NIGHT_VISION, false),
                Pair.of(Potions.STRENGTH, false));

        switch (function)
        {
            case "chest":
                createChest(worldIn, sbb, rand, pos.below(), new ResourceLocation(Mineria.MODID, "chests/wizard_laboratory_chest"), null);
                worldIn.removeBlock(pos, false);
                break;
            case "cauldron":
                BlockPos cauldronPos = pos.below();
                BlockState cauldronState = worldIn.getBlockState(cauldronPos);

                if(cauldronState.is(Blocks.CAULDRON) && sbb.isInside(cauldronPos))
                {
                    int randLevel = rand.nextInt(4);
                    worldIn.setBlock(cauldronPos, cauldronState.setValue(CauldronBlock.LEVEL, randLevel), 2);
                }
                worldIn.removeBlock(pos, false);
                break;
            case "brewing_stand":
                BlockPos brewingStandPos = pos.below();
                BlockEntity tile = worldIn.getBlockEntity(brewingStandPos);

                if(tile instanceof BrewingStandBlockEntity && sbb.isInside(brewingStandPos))
                {
                    BrewingStandBlockEntity brewingStand = (BrewingStandBlockEntity) tile;
                    int netherWartCount = rand.nextInt(6);
                    brewingStand.setItem(3, new ItemStack(Items.NETHER_WART, netherWartCount));

                    int potionCount = rand.nextInt(4);

                    for (int i = 0; i < potionCount; i++)
                    {
                        Pair<Potion, Boolean> pair = potions.get(rand.nextInt(potions.size()));
                        brewingStand.setItem(i, PotionUtils.setPotion(new ItemStack(MineriaPotionItem.getItemForPotion(pair.getRight() ? Items.SPLASH_POTION : Items.POTION, pair.getLeft())), pair.getLeft()));
                    }
                }
                worldIn.removeBlock(pos, false);
                break;
            case "potions":
                BlockPos potionsPos = pos.below();
                BlockPos potionsPos2 = pos.below(2);
                BlockPos[] poses = new BlockPos[] { potionsPos, potionsPos.relative(rotation.rotate(Direction.EAST)), potionsPos.relative(rotation.rotate(Direction.WEST)), potionsPos2, potionsPos2.relative(rotation.rotate(Direction.EAST)), potionsPos2.relative(rotation.rotate(Direction.WEST)) };

                for(BlockPos itemFramePos : poses)
                {
                    if(rand.nextInt(4) == 0)
                    {
                        Pair<Potion, Boolean> pair = potions.get(rand.nextInt(potions.size()));
                        TemporaryItemFrameEntity itemFrame = new TemporaryItemFrameEntity(worldIn.getLevel(), itemFramePos, Direction.UP, PotionUtils.setPotion(new ItemStack(MineriaPotionItem.getItemForPotion(pair.getRight() ? Items.SPLASH_POTION : Items.POTION, pair.getLeft())), pair.getLeft()), false);
                        setItemFrameRotation(itemFrame, rand.nextInt(8));
                        itemFrame.setInvisible(true);
                        worldIn.addFreshEntity(itemFrame);
                    }
                }
                worldIn.removeBlock(pos, false);
                break;
            case "wizard":
                worldIn.removeBlock(pos, false);
                if(rand.nextBoolean())
                {
                    WizardEntity entity = MineriaEntities.WIZARD.get().create(worldIn.getLevel());
                    if(entity != null)
                    {
                        entity.moveTo(pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5, Mth.wrapDegrees(rand.nextFloat() * 360F), 0);
                        entity.yHeadRot = entity.yRot;
                        entity.yBodyRot = entity.yRot;
                        entity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                        worldIn.addFreshEntityWithPassengers(entity);
                    }
                }
                break;
        }
    }

    private static void setItemFrameRotation(ItemFrame itemFrame, int value)
    {
        try
        {
            ObfuscationReflectionHelper.findMethod(ItemFrame.class, "m_31772_", int.class, boolean.class).invoke(itemFrame, value, false);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            LOGGER.error("Failed to apply rotation to item frame due to reflection error!", e);
        }
    }

    public static void start(StructureManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random)
    {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new WizardLaboratoryPiece(templateManager, blockpos, rotation));
    }*/
}
