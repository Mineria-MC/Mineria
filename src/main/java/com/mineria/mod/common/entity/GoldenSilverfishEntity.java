package com.mineria.mod.common.entity;

import com.mineria.mod.common.blocks.GoldenSilverfishBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.EnumSet;
import java.util.Random;

public class GoldenSilverfishEntity extends SilverfishEntity
{
    private SummonGoldenSilverfishGoal summonGoldenSilverfish;

    public GoldenSilverfishEntity(EntityType<? extends SilverfishEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals()
    {
        this.summonGoldenSilverfish = new SummonGoldenSilverfishGoal(this);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, this.summonGoldenSilverfish);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new HideInStoneGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, IWorldReader worldIn)
    {
        return GoldenSilverfishBlock.canContainGoldenSilverfish(worldIn.getBlockState(pos.below())) ? 10.0F : super.getWalkTargetValue(pos, worldIn);
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        if (this.isInvulnerableTo(source))
        {
            return false;
        } else
        {
            if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonGoldenSilverfish != null)
            {
                this.summonGoldenSilverfish.notifyHurt();
            }

            return super.hurt(source, amount);
        }
    }

    static class HideInStoneGoal extends RandomWalkingGoal
    {
        private Direction facing;
        private boolean doMerge;

        public HideInStoneGoal(SilverfishEntity silverfishIn)
        {
            super(silverfishIn, 1.0D, 10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse()
        {
            if (this.mob.getTarget() != null)
            {
                return false;
            }
            else if (!this.mob.getNavigation().isDone())
            {
                return false;
            }
            else {
                Random random = this.mob.getRandom();

                if (ForgeEventFactory.getMobGriefingEvent(this.mob.level, this.mob) && random.nextInt(10) == 0)
                {
                    this.facing = Direction.getRandom(random);
                    BlockPos pos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
                    BlockState state = this.mob.level.getBlockState(pos);

                    if (GoldenSilverfishBlock.canContainGoldenSilverfish(state))
                    {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.canUse();
            }
        }

        public boolean canContinueToUse()
        {
            return !this.doMerge && super.canContinueToUse();
        }

        public void start()
        {
            if (!this.doMerge)
            {
                super.start();
            }
            else {
                IWorld world = this.mob.level;
                BlockPos pos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
                BlockState state = world.getBlockState(pos);

                if (GoldenSilverfishBlock.canContainGoldenSilverfish(state))
                {
                    world.setBlock(pos, GoldenSilverfishBlock.infest(state.getBlock()), 3);
                    this.mob.spawnAnim();
                    this.mob.remove();
                }
            }
        }
    }

    static class SummonGoldenSilverfishGoal extends Goal
    {
        private final GoldenSilverfishEntity goldenSilverfish;
        private int lookForFriends;

        public SummonGoldenSilverfishGoal(GoldenSilverfishEntity silverfishIn)
        {
            this.goldenSilverfish = silverfishIn;
        }

        public void notifyHurt()
        {
            if (this.lookForFriends == 0)
            {
                this.lookForFriends = 20;
            }

        }

        public boolean canUse()
        {
            return this.lookForFriends > 0;
        }

        public void tick()
        {
            --this.lookForFriends;
            if (this.lookForFriends <= 0)
            {
                World world = this.goldenSilverfish.level;
                Random random = this.goldenSilverfish.getRandom();
                BlockPos pos = this.goldenSilverfish.blockPosition();

                for(int y = 0; y <= 5 && y >= -5; y = (y <= 0 ? 1 : 0) - y)
                {
                    for(int x = 0; x <= 10 && x >= -10; x = (x <= 0 ? 1 : 0) - x)
                    {
                        for(int z = 0; z <= 10 && z >= -10; z = (z <= 0 ? 1 : 0) - z)
                        {
                            BlockPos adjacentPos = pos.offset(x, y, z);
                            BlockState adjacentState = world.getBlockState(adjacentPos);
                            Block adjacentBlock = adjacentState.getBlock();

                            if (adjacentBlock instanceof GoldenSilverfishBlock)
                            {
                                if (ForgeEventFactory.getMobGriefingEvent(world, this.goldenSilverfish))
                                {
                                    world.destroyBlock(adjacentPos, true, this.goldenSilverfish);
                                }
                                else {
                                    world.setBlock(adjacentPos, ((GoldenSilverfishBlock)adjacentBlock).getMimickedBlock().defaultBlockState(), 3);
                                }

                                if (random.nextBoolean())
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
