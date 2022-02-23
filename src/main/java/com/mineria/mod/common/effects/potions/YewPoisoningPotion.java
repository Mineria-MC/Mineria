package com.mineria.mod.common.effects.potions;

import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.items.MineriaPotionItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class YewPoisoningPotion extends MineriaPotion
{
    public YewPoisoningPotion()
    {
        super("yew_poisoning", PoisonSource.YEW);
    }

    @Override
    public boolean showInItemGroup(CreativeModeTab group, MineriaPotionItem potionItem)
    {
        return potionItem != MineriaItems.MINERIA_SPLASH_POTION && potionItem != MineriaItems.MINERIA_LINGERING_POTION;
    }

    @Override
    public void applyEffects(ItemStack stack, Level world, @Nullable Player player, LivingEntity living)
    {
        super.applyEffects(stack, world, player, living);
        if(player != null)
            findRitualTable(world, player.blockPosition()).ifPresent(RitualTableTileEntity::setPotionDrank);
    }

    private static Optional<RitualTableTileEntity> findRitualTable(Level world, BlockPos playerPos)
    {
        for(BlockPos pos : BlockPos.betweenClosed(playerPos.offset(-30, -10, -30), playerPos.offset(30, 10, 30)))
        {
            BlockEntity te = world.getBlockEntity(pos);
            if(te instanceof RitualTableTileEntity)
            {
                return Optional.of((RitualTableTileEntity) te);
            }
        }

        return Optional.empty();
    }
}
