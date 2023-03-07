package io.github.mineria_mc.mineria.common.world.feature.structure.data_markers;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.data.MineriaLootTables;
import io.github.mineria_mc.mineria.common.entity.TemporaryItemFrameEntity;
import io.github.mineria_mc.mineria.common.entity.WizardEntity;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import io.github.mineria_mc.mineria.common.items.MineriaPotionItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class WizardLaboratoryDataMarkerHandler extends DataMarkerHandlerBase {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handleDataMarker(ServerLevelAccessor level, String function, BlockPos pos, StructureTemplate.StructureBlockInfo info, BlockPos seedPos, BoundingBox box, StructurePoolElement spe, Rotation rotation, RandomSource random) {
        List<Pair<Potion, Boolean>> potions = ImmutableList.of(Pair.of(Potions.HEALING, false), Pair.of(MineriaPotions.CLASS_2_POISON.get(), true),
                Pair.of(Potions.INVISIBILITY, false), Pair.of(Potions.SLOWNESS, true), Pair.of(Potions.NIGHT_VISION, false),
                Pair.of(Potions.STRENGTH, false));

        switch (function) {
            case "chest" -> {
                createChest(level, box, random, pos.below(), MineriaLootTables.WIZARD_LABORATORY_CHEST, null);
                level.removeBlock(pos, false);
            }
            case "cauldron" -> {
                BlockPos cauldronPos = pos.below();
                BlockState cauldronState = level.getBlockState(cauldronPos);
                if (cauldronState.is(Blocks.WATER_CAULDRON) && box.isInside(cauldronPos)) {
                    int randomLevel = random.nextInt(4);
                    level.setBlock(cauldronPos, cauldronState.setValue(LayeredCauldronBlock.LEVEL, randomLevel), 2);
                }
                level.removeBlock(pos, false);
            }
            case "brewing_stand" -> {
                BlockPos brewingStandPos = pos.below();
                BlockEntity tile = level.getBlockEntity(brewingStandPos);
                if (tile instanceof BrewingStandBlockEntity brewingStand && box.isInside(brewingStandPos)) {
                    int netherWartCount = random.nextInt(6);
                    brewingStand.setItem(3, new ItemStack(Items.NETHER_WART, netherWartCount));

                    int potionCount = random.nextInt(4);

                    for (int i = 0; i < potionCount; i++) {
                        Pair<Potion, Boolean> pair = potions.get(random.nextInt(potions.size()));
                        brewingStand.setItem(i, PotionUtils.setPotion(new ItemStack(MineriaPotionItem.getItemForPotion(pair.getSecond() ? Items.SPLASH_POTION : Items.POTION, pair.getFirst())), pair.getFirst()));
                    }
                }
                level.removeBlock(pos, false);
            }
            case "potions" -> {
                level.removeBlock(pos, false);
                placeItemFrame(level, random, potions, pos);
                placeItemFrame(level, random, potions, pos.below());
            }
            case "wizard" -> {
                level.removeBlock(pos, false);
                if (random.nextBoolean()) {
                    WizardEntity entity = MineriaEntities.WIZARD.get().create(level.getLevel());
                    if (entity != null) {
                        entity.moveTo(pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5, Mth.wrapDegrees(random.nextFloat() * 360F), 0);
                        entity.yHeadRot = entity.yBodyRot = entity.getYRot();
                        entity.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                        level.addFreshEntityWithPassengers(entity);
                    }
                }
            }
        }
    }

    private void placeItemFrame(ServerLevelAccessor level, RandomSource random, List<Pair<Potion, Boolean>> potions, BlockPos itemFramePos) {
        if (random.nextInt(4) == 0) {
            Pair<Potion, Boolean> pair = potions.get(random.nextInt(potions.size()));
            TemporaryItemFrameEntity itemFrame = new TemporaryItemFrameEntity(level.getLevel(), itemFramePos, Direction.UP, PotionUtils.setPotion(new ItemStack(MineriaPotionItem.getItemForPotion(pair.getSecond() ? Items.SPLASH_POTION : Items.POTION, pair.getFirst())), pair.getFirst()), false);
            setItemFrameRotation(itemFrame, random.nextInt(8));
            itemFrame.setInvisible(true);
            level.addFreshEntity(itemFrame);
        }
    }

    private static Method SET_ROTATION;

    private static void setItemFrameRotation(ItemFrame itemFrame, int value) {
        try {
            if (SET_ROTATION == null)
                SET_ROTATION = ObfuscationReflectionHelper.findMethod(ItemFrame.class, "m_31772_", int.class, boolean.class);
            SET_ROTATION.invoke(itemFrame, value, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Failed to apply rotation to item frame due to reflection error!", e);
        }
    }
}
