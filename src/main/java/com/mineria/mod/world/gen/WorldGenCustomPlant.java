package com.mineria.mod.world.gen;

import com.mineria.mod.blocks.PlantBase;
import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenCustomPlant extends WorldGenerator
{
    private IBlockState state;
    private PlantBase plant;

    public WorldGenCustomPlant(PlantBase plant)
    {
        this.setGeneratedPlant(plant);
    }

    public void setGeneratedPlant(PlantBase plant)
    {
        this.state = plant.getDefaultState();
        this.plant = plant;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int i = 0; i < this.getPlantRarity(); ++i)
        {
            int b1 = this.getPlantRarity() / 4;
            int b2 = this.getPlantRarity() / 8;

            if(b1 <= 0) b1 = 1;
            if(b2 <= 0) b2 = 1;

            BlockPos blockpos = position.add(rand.nextInt(b1) - rand.nextInt(b1), rand.nextInt(b2) - rand.nextInt(b2), rand.nextInt(b1) - rand.nextInt(b1));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < 255) && this.plant.canBlockStay(worldIn, blockpos, this.state))
            {
                worldIn.setBlockState(blockpos, this.state, 2);
            }
        }
        return true;
    }

    public PlantBase getPlant()
    {
        return this.plant;
    }

    private int getPlantRarity()
    {
        if(plant == BlocksInit.plantain)
        {
            return 16;
        }
        if(plant == BlocksInit.mint)
        {
            return 12;
        }
        if(plant == BlocksInit.thyme)
        {
            return 10;
        }
        if(plant == BlocksInit.nettle)
        {
            return 8;
        }
        if(plant == BlocksInit.pulmonary)
        {
            return 12;
        }
        return 1;
    }
}
