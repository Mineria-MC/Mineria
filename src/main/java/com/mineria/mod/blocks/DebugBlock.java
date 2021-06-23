package com.mineria.mod.blocks;

import com.google.common.collect.Lists;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

public class DebugBlock extends Block
{
    private static final int ZONE_WIDTH = 32;
    private static final int ZONE_HEIGHT = 16;

    public DebugBlock()
    {
        super(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(-1, 3600000).sound(SoundType.ANCIENT_DEBRIS).noDrops().setAllowsSpawn((a, b, c, d) -> false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!world.isRemote)
        {
            pos = pos.add(-ZONE_WIDTH / 2, 0, -ZONE_WIDTH / 2);

            setAir(world, pos);
            setAllBlocks(world, pos);
            player.addItemStackToInventory(enchantItemStack(ItemsInit.LONSDALEITE_PICKAXE));
            player.addItemStackToInventory(enchantItemStack(ItemsInit.LONSDALEITE_AXE));
            player.addItemStackToInventory(enchantItemStack(ItemsInit.LONSDALEITE_SHOVEL));

            return ActionResultType.func_233537_a_(false);
        }
        return ActionResultType.PASS;
    }

    private void setAir(World world, BlockPos pos)
    {
        for(int y = 0; y < ZONE_HEIGHT; y++)
        {
            for(int x = 0; x < ZONE_WIDTH; x++)
            {
                for(int z = 0; z < ZONE_WIDTH; z++)
                {
                    world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 2);
                    world.setBlockState(pos.add(x, -1, z), Blocks.STONE.getDefaultState(), 2);
                }
            }
        }
    }

    private void setAllBlocks(World world, BlockPos pos)
    {
        List<BlockState> states = BlocksInit.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block != this).map(Block::getDefaultState).collect(Collectors.toList());
        List<BlockPos> posList = Lists.newArrayList();

        for(int y = 0; y < ZONE_HEIGHT; y += 4)
            for(int z = 0; z < ZONE_WIDTH; z += 2)
                for(int x = 0; x < ZONE_WIDTH; x += 2)
                    posList.add(pos.add(x, y, z));

        for(int index = 0; index < posList.size(); index++)
        {
            if(index >= states.size())
                break;
            world.setBlockState(posList.get(index), states.get(index));
        }
    }

    private ItemStack enchantItemStack(Item item)
    {
        ItemStack stack = new ItemStack(item);
        stack.setTag(Util.make(new CompoundNBT(), nbt -> nbt.putBoolean("Unbreakable", true)));
        stack.addEnchantment(Enchantments.EFFICIENCY, 200);
        return stack;
    }
}
