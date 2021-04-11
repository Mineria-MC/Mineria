package com.mineria.mod.world.gen;

import com.mineria.mod.blocks.MineriaBlockPlant;
import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class PlantWorldGenerator extends WorldGenerator
{
    private IBlockState state;
    private MineriaBlockPlant plant;

    public PlantWorldGenerator(MineriaBlockPlant plant)
    {
        this.setGeneratedPlant(plant);
    }

    public void setGeneratedPlant(MineriaBlockPlant plant)
    {
        this.state = plant.getDefaultState();
        this.plant = plant;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int count = 0; count < this.getPlantRarity(); ++count)
        {
            int horizontalRadius = this.getPlantRarity() / 4;
            int verticalRadius = this.getPlantRarity() / 8;

            if(horizontalRadius <= 0) horizontalRadius = 1;
            if(verticalRadius <= 0) verticalRadius = 1;

            BlockPos pos = position.add(rand.nextInt(horizontalRadius) - rand.nextInt(horizontalRadius), rand.nextInt(verticalRadius) - rand.nextInt(verticalRadius), rand.nextInt(horizontalRadius) - rand.nextInt(horizontalRadius));

            if (worldIn.isAirBlock(pos) && (!worldIn.provider.isNether() || pos.getY() < 255) && this.plant.canBlockStay(worldIn, pos, this.state))
            {
                worldIn.setBlockState(pos, this.state, 2);
            }
        }
        return true;
    }

    public MineriaBlockPlant getPlant()
    {
        return this.plant;
    }

    private int getPlantRarity()
    {
        if(plant == BlocksInit.PLANTAIN)
        {
            return 16;
        }
        if(plant == BlocksInit.MINT)
        {
            return 12;
        }
        if(plant == BlocksInit.THYME)
        {
            return 10;
        }
        if(plant == BlocksInit.NETTLE)
        {
            return 8;
        }
        if(plant == BlocksInit.PULMONARY)
        {
            return 12;
        }
        return 1;
    }
}
