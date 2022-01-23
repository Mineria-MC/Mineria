package com.mineria.mod.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    protected final Supplier<Item> fruit;
    private final int chance;

    public FruitPlantBlock(Supplier<Item> fruit, int chance, boolean isBushBlock)
    {
        super(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(isBushBlock ? 0.5F : 0).sound(SoundType.GRASS).harvestLevel(0).harvestTool(ToolType.HOE).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false), isBushBlock);
        this.fruit = fruit;
        this.chance = chance;
        registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(state.getValue(AGE) == 1)
        {
            popResource(worldIn, pos, new ItemStack(fruit.get()));
            worldIn.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
            worldIn.setBlock(pos, state.setValue(AGE, 0), 2);
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
        return ActionResultType.PASS;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        if(world.random.nextInt(this.chance) == 0 && world.isAreaLoaded(pos, 4))
        {
            int age = state.getValue(AGE);
            if(age == 0)
                world.setBlock(pos, state.setValue(AGE, 1), 2);
        }
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        return state.getValue(AGE) < 1;
    }

    @Override
    public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
        return true;
    }

    @Override
    public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
        worldIn.setBlock(pos, state.setValue(AGE, 1), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
}
