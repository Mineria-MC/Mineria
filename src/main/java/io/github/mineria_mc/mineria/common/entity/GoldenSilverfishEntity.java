package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.blocks.GoldenSilverfishBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.EnumSet;

public class GoldenSilverfishEntity extends Silverfish {
    private SummonGoldenSilverfishGoal summonGoldenSilverfish;

    public GoldenSilverfishEntity(EntityType<? extends Silverfish> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.summonGoldenSilverfish = new SummonGoldenSilverfishGoal(this);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, this.summonGoldenSilverfish);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new HideInStoneGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return GoldenSilverfishBlock.canContainGoldenSilverfish(worldIn.getBlockState(pos.below())) ? 10.0F : super.getWalkTargetValue(pos, worldIn);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonGoldenSilverfish != null) {
                this.summonGoldenSilverfish.notifyHurt();
            }

            return super.hurt(source, amount);
        }
    }

    static class HideInStoneGoal extends RandomStrollGoal {
        private Direction facing;
        private boolean doMerge;

        public HideInStoneGoal(Silverfish silverfishIn) {
            super(silverfishIn, 1.0D, 10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.getNavigation().isDone()) {
                return false;
            } else {
                RandomSource random = this.mob.getRandom();

                if (ForgeEventFactory.getMobGriefingEvent(this.mob.level, this.mob) && random.nextInt(10) == 0) {
                    this.facing = Direction.getRandom(random);
                    BlockPos pos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
                    BlockState state = this.mob.level.getBlockState(pos);

                    if (GoldenSilverfishBlock.canContainGoldenSilverfish(state)) {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.canUse();
            }
        }

        public boolean canContinueToUse() {
            return !this.doMerge && super.canContinueToUse();
        }

        public void start() {
            if (!this.doMerge) {
                super.start();
            } else {
                LevelAccessor world = this.mob.level;
                BlockPos pos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
                BlockState state = world.getBlockState(pos);

                if (GoldenSilverfishBlock.canContainGoldenSilverfish(state)) {
                    world.setBlock(pos, GoldenSilverfishBlock.infest(state.getBlock()), 3);
                    this.mob.spawnAnim();
                    this.mob.discard();
                }
            }
        }
    }

    static class SummonGoldenSilverfishGoal extends Goal {
        private final GoldenSilverfishEntity goldenSilverfish;
        private int lookForFriends;

        public SummonGoldenSilverfishGoal(GoldenSilverfishEntity silverfishIn) {
            this.goldenSilverfish = silverfishIn;
        }

        public void notifyHurt() {
            if (this.lookForFriends == 0) {
                this.lookForFriends = 20;
            }

        }

        public boolean canUse() {
            return this.lookForFriends > 0;
        }

        public void tick() {
            --this.lookForFriends;
            if (this.lookForFriends <= 0) {
                Level world = this.goldenSilverfish.level;
                RandomSource random = this.goldenSilverfish.getRandom();
                BlockPos pos = this.goldenSilverfish.blockPosition();

                for (int y = 0; y <= 5 && y >= -5; y = (y <= 0 ? 1 : 0) - y) {
                    for (int x = 0; x <= 10 && x >= -10; x = (x <= 0 ? 1 : 0) - x) {
                        for (int z = 0; z <= 10 && z >= -10; z = (z <= 0 ? 1 : 0) - z) {
                            BlockPos adjacentPos = pos.offset(x, y, z);
                            BlockState adjacentState = world.getBlockState(adjacentPos);
                            Block adjacentBlock = adjacentState.getBlock();

                            if (adjacentBlock instanceof GoldenSilverfishBlock) {
                                if (ForgeEventFactory.getMobGriefingEvent(world, this.goldenSilverfish)) {
                                    world.destroyBlock(adjacentPos, true, this.goldenSilverfish);
                                } else {
                                    world.setBlock(adjacentPos, ((GoldenSilverfishBlock) adjacentBlock).getMimickedBlock().defaultBlockState(), 3);
                                }

                                if (random.nextBoolean()) {
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
