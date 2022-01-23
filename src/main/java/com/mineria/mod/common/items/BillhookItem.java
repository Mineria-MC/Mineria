package com.mineria.mod.common.items;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BillhookItem extends Item
{
    public BillhookItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if(!worldIn.isClientSide && state.getDestroySpeed(worldIn, pos) != 0.0F)
        {
            // TODOLTR maybe replace with a tag
            List<Block> blocks = Lists.newArrayList(Blocks.OAK_LEAVES, MineriaBlocks.MANDRAKE, MineriaBlocks.PULSATILLA_CHINENSIS, MineriaBlocks.SAUSSUREA_COSTUS);
            if(blocks.contains(state.getBlock()))
            {
                stack.hurtAndBreak(1, entityLiving, (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            }
        }
        return super.mineBlock(stack, worldIn, state, pos, entityLiving);
    }
}
