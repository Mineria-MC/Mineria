package com.mineria.mod.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class StrychnosPlantBlock extends VineBlock
{
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;

    public StrychnosPlantBlock()
    {
        super(AbstractBlock.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.VINE));
        setDefaultState(this.stateContainer.getBaseState().with(UP, false).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(AGE, 0));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
    {
        super.randomTick(state, worldIn, pos, random);
        if(worldIn.rand.nextInt(5) == 0 && worldIn.isAreaLoaded(pos, 4))
        {
            int age = state.get(AGE);
            if(age < 2)
                worldIn.setBlockState(pos, state.with(AGE, age + 1), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder.add(AGE));
    }
}
