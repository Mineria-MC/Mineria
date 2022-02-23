package com.mineria.mod.common.blocks;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.List;

public class DebugBlock extends Block
{
    private static final int ZONE_WIDTH = 32;
    private static final int ZONE_HEIGHT = 16;

    public DebugBlock()
    {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(-1, 3600000).sound(SoundType.ANCIENT_DEBRIS).noDrops().isValidSpawn((a, b, c, d) -> false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!world.isClientSide && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
        {
            BlockPos startPos = pos.offset(-ZONE_WIDTH / 2, 0, -ZONE_WIDTH / 2);

            setAir(world, startPos);
            setAllBlocks(world, startPos);
            world.setBlock(pos.below(), MineriaBlocks.LONSDALEITE_BLOCK.defaultBlockState(), 2);
            player.addItem(enchantItemStack(MineriaItems.LONSDALEITE_PICKAXE));
            player.addItem(enchantItemStack(MineriaItems.LONSDALEITE_AXE));
            player.addItem(enchantItemStack(MineriaItems.LONSDALEITE_SHOVEL));

            return InteractionResult.sidedSuccess(false);
        }

        return InteractionResult.PASS;
    }

    private void setAir(Level world, BlockPos pos)
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

    private void setAllBlocks(Level world, BlockPos pos)
    {
        List<BlockState> states = MineriaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block != this).map(Block::defaultBlockState).toList();
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
        stack.setTag(Util.make(new CompoundTag(), nbt -> nbt.putBoolean("Unbreakable", true)));
        stack.enchant(Enchantments.BLOCK_EFFICIENCY, 200);
        return stack;
    }
}
