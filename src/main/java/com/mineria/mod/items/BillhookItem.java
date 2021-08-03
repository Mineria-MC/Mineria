package com.mineria.mod.items;

import com.google.common.collect.Lists;
import com.mineria.mod.init.BlocksInit;
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if(!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F)
        {
            List<Block> blocks = Lists.newArrayList(Blocks.OAK_LEAVES, BlocksInit.MANDRAKE, BlocksInit.PULSATILLA_CHINENSIS, BlocksInit.SAUSSUREA_COSTUS);
            if(blocks.contains(state.getBlock()))
            {
                stack.damageItem(1, entityLiving, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            }
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
}
