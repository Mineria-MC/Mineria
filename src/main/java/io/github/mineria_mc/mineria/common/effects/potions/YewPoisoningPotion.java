package io.github.mineria_mc.mineria.common.effects.potions;

import io.github.mineria_mc.mineria.common.blocks.ritual_table.RitualTableTileEntity;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class YewPoisoningPotion extends MineriaPotion {
    public YewPoisoningPotion() {
        super("yew_poisoning", PoisonSource.YEW);
    }

    @Override
    public boolean showInItemGroup(CreativeModeTab group, Item potionItem) {
        return potionItem != MineriaItems.MINERIA_SPLASH_POTION.get() && potionItem != MineriaItems.MINERIA_LINGERING_POTION.get();
    }

    @Override
    public void applyEffects(ItemStack stack, Level world, @Nullable Player player, LivingEntity living) {
        super.applyEffects(stack, world, player, living);
        if (player != null) {
            findRitualTable(world, player.blockPosition()).ifPresent(pair -> pair.getSecond().setPotionDrank(world, pair.getFirst()));
        }
    }

    private static Optional<Pair<BlockPos, RitualTableTileEntity>> findRitualTable(Level world, BlockPos playerPos) {
        for (BlockPos pos : BlockPos.betweenClosed(playerPos.offset(-30, -10, -30), playerPos.offset(30, 10, 30))) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof RitualTableTileEntity table) {
                return Optional.of(Pair.of(pos, table));
            }
        }

        return Optional.empty();
    }
}
