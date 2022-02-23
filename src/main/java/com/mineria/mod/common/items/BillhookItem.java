package com.mineria.mod.common.items;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class BillhookItem extends Item
{
    public BillhookItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if(!worldIn.isClientSide && state.getDestroySpeed(worldIn, pos) != 0.0F)
        {
            // TODOLTR maybe replace with a tag
            List<Block> blocks = Lists.newArrayList(Blocks.OAK_LEAVES, MineriaBlocks.MANDRAKE, MineriaBlocks.PULSATILLA_CHINENSIS, MineriaBlocks.SAUSSUREA_COSTUS);
            if(blocks.contains(state.getBlock()))
            {
                stack.hurtAndBreak(1, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return super.mineBlock(stack, worldIn, state, pos, entityLiving);
    }
}
