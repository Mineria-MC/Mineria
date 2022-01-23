package com.mineria.mod.common.effects.potions;

import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.items.MineriaPotionItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class YewPoisoningPotion extends MineriaPotion
{
    public YewPoisoningPotion()
    {
        super("yew_poisoning", PoisonSource.YEW);
    }

    @Override
    public boolean showInItemGroup(ItemGroup group, MineriaPotionItem potionItem)
    {
        return potionItem != MineriaItems.MINERIA_SPLASH_POTION && potionItem != MineriaItems.MINERIA_LINGERING_POTION;
    }

    @Override
    public void applyEffects(ItemStack stack, World world, @Nullable PlayerEntity player, LivingEntity living)
    {
        super.applyEffects(stack, world, player, living);
        if(player != null)
            findRitualTable(world, player.blockPosition()).ifPresent(RitualTableTileEntity::setPotionDrank);
    }

    private static Optional<RitualTableTileEntity> findRitualTable(World world, BlockPos playerPos)
    {
        for(BlockPos pos : BlockPos.betweenClosed(playerPos.offset(-30, -10, -30), playerPos.offset(30, 10, 30)))
        {
            TileEntity te = world.getBlockEntity(pos);
            if(te instanceof RitualTableTileEntity)
            {
                return Optional.of((RitualTableTileEntity) te);
            }
        }

        return Optional.empty();
    }
}
