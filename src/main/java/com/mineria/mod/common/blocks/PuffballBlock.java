package com.mineria.mod.common.blocks;

import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class PuffballBlock extends MineriaMushroomBlock
{
    public PuffballBlock()
    {
        super(MaterialColor.COLOR_LIGHT_GRAY);
    }

    private void bounceUp(Entity entity)
    {
        Vector3d motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, 1, motion.z);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
    {
        Vector3d motion = entity.getDeltaMovement();
        if (motion.y < -0.5)
        {
            if (!entity.isSuppressingBounce())
            {
                entity.causeFallDamage(0, 0);
                bounceUp(entity);
                for (int i = 0; i < 15; i++)
                {
                    double posX = world.random.nextGaussian() * 0.2;
                    double posY = world.random.nextGaussian() * 0.1;
                    double posZ = world.random.nextGaussian() * 0.2;
                    double dx = world.random.nextGaussian() * 0.2;
                    double dy = world.random.nextGaussian() * 0.2;
                    double dz = world.random.nextGaussian() * 0.2;

                    world.addParticle(ParticleTypes.POOF, false, pos.getX() + 0.5 + posX, pos.getY() + 0.5 + posY, pos.getZ() + 0.5 + posZ, dx, dy, dz);
                }
                world.setBlock(pos, MineriaBlocks.PUFFBALL_POWDER.defaultBlockState(), Constants.BlockFlags.DEFAULT);
                world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundCategory.BLOCKS, 1, MineriaUtils.randomPitch());
                for(Direction direction : Direction.Plane.HORIZONTAL)
                {
                    if(world.random.nextInt(3) == 0)
                    {
                        BlockPos adjacentPos = pos.relative(direction);
                        BlockState adjacent = world.getBlockState(adjacentPos);
                        if(adjacent.getMaterial().isReplaceable())
                            world.setBlock(adjacentPos, MineriaBlocks.PUFFBALL_POWDER.defaultBlockState(), Constants.BlockFlags.DEFAULT);
                    }
                }
            }
        }
    }
}
