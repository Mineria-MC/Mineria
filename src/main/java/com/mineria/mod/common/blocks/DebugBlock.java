package com.mineria.mod.common.blocks;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
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
        super(AbstractBlock.Properties.of(Material.STONE).strength(-1, 3600000).sound(SoundType.ANCIENT_DEBRIS).noDrops().isValidSpawn((a, b, c, d) -> false));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!world.isClientSide && player.getItemInHand(Hand.MAIN_HAND).isEmpty())
        {
            BlockPos startPos = pos.offset(-ZONE_WIDTH / 2, 0, -ZONE_WIDTH / 2);

            setAir(world, startPos);
            setAllBlocks(world, startPos);
            world.setBlock(pos.below(), MineriaBlocks.LONSDALEITE_BLOCK.defaultBlockState(), 2);
            player.addItem(enchantItemStack(MineriaItems.LONSDALEITE_PICKAXE));
            player.addItem(enchantItemStack(MineriaItems.LONSDALEITE_AXE));
            player.addItem(enchantItemStack(MineriaItems.LONSDALEITE_SHOVEL));

            return ActionResultType.sidedSuccess(false);
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
                    world.setBlock(pos.offset(x, y, z), Blocks.AIR.defaultBlockState(), 2);
                    world.setBlock(pos.offset(x, -1, z), Blocks.STONE.defaultBlockState(), 2);
                    world.setBlock(pos.below(), Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
                }
            }
        }
    }

    private void setAllBlocks(World world, BlockPos pos)
    {
        List<BlockState> states = MineriaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block != this).map(Block::defaultBlockState).collect(Collectors.toList());
        List<BlockPos> posList = Lists.newArrayList();

        for(int y = 0; y < ZONE_HEIGHT; y += 4)
            for(int z = 0; z < ZONE_WIDTH; z += 2)
                for(int x = 0; x < ZONE_WIDTH; x += 2)
                    posList.add(pos.offset(x, y, z));

        for(int index = 0; index < posList.size(); index++)
        {
            if(index >= states.size())
                break;
            world.setBlockAndUpdate(posList.get(index), states.get(index));
        }
    }

    private ItemStack enchantItemStack(Item item)
    {
        ItemStack stack = new ItemStack(item);
        stack.setTag(Util.make(new CompoundNBT(), nbt -> nbt.putBoolean("Unbreakable", true)));
        stack.enchant(Enchantments.BLOCK_EFFICIENCY, 200);
        return stack;
    }
}
