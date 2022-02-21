package com.mineria.mod.common.world.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.TemporaryItemFrameEntity;
import com.mineria.mod.common.entity.WizardEntity;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaPotions;
import com.mineria.mod.common.init.MineriaStructures;
import com.mineria.mod.common.items.MineriaPotionItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public class WizardLaboratoryPiece extends TemplateStructurePiece
{
    private static final Logger LOGGER = LogManager.getLogger();
    private Rotation rotation;

    public WizardLaboratoryPiece(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn)
    {
        super(MineriaStructures.WLP, 0);
        this.templatePosition = pos;
        this.rotation = rotationIn;
        this.setupPiece(templateManagerIn);
    }

    public WizardLaboratoryPiece(TemplateManager templateManagerIn, CompoundNBT nbt)
    {
        super(MineriaStructures.WLP, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.setupPiece(templateManagerIn);
    }

    private void setupPiece(TemplateManager templateManager)
    {
        Template template = templateManager.getOrCreate(new ResourceLocation(Mineria.MODID, "wizard_laboratory"));
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
    protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb)
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
                TileEntity tile = worldIn.getBlockEntity(brewingStandPos);

                if(tile instanceof BrewingStandTileEntity && sbb.isInside(brewingStandPos))
                {
                    BrewingStandTileEntity brewingStand = (BrewingStandTileEntity) tile;
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
                        entity.moveTo(pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(rand.nextFloat() * 360F), 0);
                        entity.yHeadRot = entity.yRot;
                        entity.yBodyRot = entity.yRot;
                        entity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.STRUCTURE, null, null);
                        worldIn.addFreshEntityWithPassengers(entity);
                    }
                }
                break;
        }
    }

    private static void setItemFrameRotation(ItemFrameEntity itemFrame, int value)
    {
        try
        {
            ObfuscationReflectionHelper.findMethod(ItemFrameEntity.class, "func_174865_a", int.class, boolean.class).invoke(itemFrame, value, false);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            LOGGER.error("Failed to apply rotation to item frame due to reflection error! Report this error to Mineria's authors!", e);
        }
    }

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random)
    {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.add(new WizardLaboratoryPiece(templateManager, blockpos, rotation));
    }
}
