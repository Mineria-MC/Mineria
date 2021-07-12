package com.mineria.mod.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;
import java.util.function.Supplier;

public class FruitPlantBlock extends PlantBlock implements IGrowable
{
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_1;
    private final Supplier<Item> fruit;

    public FruitPlantBlock(Supplier<Item> fruit)
    {
        super(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.PLANT).harvestLevel(0).harvestTool(ToolType.HOE).setSuffocates((a, b, c) -> false).setBlocksVision((a, b, c) -> false), true);
        this.fruit = fruit;
        setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(state.get(AGE) == 1)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(fruit.get()));
            worldIn.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
            worldIn.setBlockState(pos, state.with(AGE, 0), 2);
            return ActionResultType.func_233537_a_(worldIn.isRemote);
        }
        else if(player.getHeldItem(handIn).getItem().equals(Items.BONE_MEAL))
            return ActionResultType.PASS;
        else
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        return state.get(AGE) < 1;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
        return true;
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
        worldIn.setBlockState(pos, state.with(AGE, 1), 2);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(AGE);
    }
}
