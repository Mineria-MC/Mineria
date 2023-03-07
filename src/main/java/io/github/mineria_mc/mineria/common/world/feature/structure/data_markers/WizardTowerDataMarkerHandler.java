package io.github.mineria_mc.mineria.common.world.feature.structure.data_markers;

import io.github.mineria_mc.mineria.common.data.MineriaLootTables;
import io.github.mineria_mc.mineria.common.entity.WizardEntity;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.RandomStringUtils;

public class WizardTowerDataMarkerHandler extends DataMarkerHandlerBase {
    @Override
    public void handleDataMarker(ServerLevelAccessor level, String function, BlockPos pos, StructureTemplate.StructureBlockInfo info, BlockPos seedPos, BoundingBox box, StructurePoolElement spe, Rotation rotation, RandomSource random) {
        switch (function) {
            case "chest" -> {
                createChest(level, box, random, pos.below(), MineriaLootTables.WIZARD_TOWER_CHEST, null);
                level.removeBlock(pos, false);
            }
            case "flower_pot" -> {
                BlockState state = level.getBlockState(pos.below());
                if (state.is(Blocks.FLOWER_POT)) {
                    level.setBlock(pos.below(), random.nextBoolean() ? MineriaBlocks.POTTED_SPRUCE_YEW_SAPLING.get().defaultBlockState() : MineriaBlocks.POTTED_SAKURA_SAPLING.get().defaultBlockState(), 2);
                }
                level.removeBlock(pos, false);
            }
            case "lectern" -> {
                BlockEntity tile = level.getBlockEntity(pos.below());
                if (tile instanceof LecternBlockEntity lectern) {
                    lectern.setBook(Util.make(new ItemStack(Items.WRITTEN_BOOK), stack -> {
                        CompoundTag nbt = new CompoundTag();
                        nbt.putString("title", ChatFormatting.ITALIC + "*No Title*");
                        nbt.putString("author", ChatFormatting.ITALIC + "Unknown");
                        nbt.put("pages", generateBook(random));
                        nbt.putBoolean("resolved", true);
                        stack.setTag(nbt);
                    }));
                }
                level.removeBlock(pos, false);
            }
            case "wizard" -> {
                level.removeBlock(pos, false);
                if (random.nextInt(3) == 0) {
                    WizardEntity entity = MineriaEntities.WIZARD.get().create(level.getLevel());
                    if (entity != null) {
                        entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(random.nextFloat() * 360F), 0);
                        entity.yHeadRot = entity.yBodyRot = entity.getYRot();
                        entity.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                        level.addFreshEntity(entity);
                    }
                }
            }
        }
    }

    public static ListTag generateBook(RandomSource random) {
        ListTag nbt = new ListTag();
        for (int pageIndex = 0; pageIndex < 2 + random.nextInt(10); pageIndex++) {
            String format = random.nextInt(14) == 0 ? ChatFormatting.GOLD.toString().concat(ChatFormatting.OBFUSCATED.toString()) : ChatFormatting.OBFUSCATED.toString();
            nbt.add(StringTag.valueOf(format.concat(RandomStringUtils.randomAlphabetic(random.nextInt(600) + 40))));
        }
        return nbt;
    }
}
