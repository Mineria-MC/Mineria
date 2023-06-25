package io.github.mineria_mc.mineria.common.blocks;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.util.MineriaUtils;
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

import javax.annotation.Nonnull;

public class PuffballBlock extends MineriaMushroomBlock {
    public PuffballBlock() {
        super(MaterialColor.COLOR_LIGHT_GRAY);
    }

    private void bounceUp(Entity entity) {
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, 1, motion.z);
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, Entity entity) {
        Vec3 motion = entity.getDeltaMovement();
        if (motion.y < -0.5) {
            if (!entity.isSuppressingBounce()) {
                entity.causeFallDamage(0, 0, world.damageSources().fall());
                bounceUp(entity);
                MineriaUtils.addParticles(world, ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.2, 0.1, 0.2, 0.2, 15, false);
                world.setBlock(pos, MineriaBlocks.PUFFBALL_POWDER.get().defaultBlockState(), Block.UPDATE_ALL);
                world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1, MineriaUtils.randomPitch());
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (world.random.nextInt(3) == 0) {
                        BlockPos adjacentPos = pos.relative(direction);
                        BlockState adjacent = world.getBlockState(adjacentPos);
                        if (adjacent.getMaterial().isReplaceable() && MineriaBlocks.PUFFBALL_POWDER.get().defaultBlockState().canSurvive(world, adjacentPos))
                            world.setBlock(adjacentPos, MineriaBlocks.PUFFBALL_POWDER.get().defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }
}
