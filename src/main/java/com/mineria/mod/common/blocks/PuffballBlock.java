package com.mineria.mod.common.blocks;

import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;

public class PuffballBlock extends MineriaMushroomBlock
{
    public PuffballBlock()
    {
        super(MaterialColor.COLOR_LIGHT_GRAY);
    }

    private void bounceUp(Entity entity)
    {
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, 1, motion.z);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
    {
        Vec3 motion = entity.getDeltaMovement();
        if (motion.y < -0.5)
        {
            if (!entity.isSuppressingBounce())
            {
                entity.causeFallDamage(0, 0, DamageSource.FALL);
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
                world.setBlock(pos, MineriaBlocks.PUFFBALL_POWDER.defaultBlockState(), Block.UPDATE_ALL);
                world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1, MineriaUtils.randomPitch());
                for(Direction direction : Direction.Plane.HORIZONTAL)
                {
                    if(world.random.nextInt(3) == 0)
                    {
                        BlockPos adjacentPos = pos.relative(direction);
                        BlockState adjacent = world.getBlockState(adjacentPos);
                        if(adjacent.getMaterial().isReplaceable())
                            world.setBlock(adjacentPos, MineriaBlocks.PUFFBALL_POWDER.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }
}
