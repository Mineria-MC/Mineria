package com.mineria.mod.entity;

import com.mineria.mod.blocks.GoldenSilverfishBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn)
    {
        return GoldenSilverfishBlock.canContainGoldenSilverfish(worldIn.getBlockState(pos.down())) ? 10.0F : super.getBlockPathWeight(pos, worldIn);
    }

    static class HideInStoneGoal extends RandomWalkingGoal
    {
        private Direction facing;
        private boolean doMerge;

        public HideInStoneGoal(SilverfishEntity silverfishIn)
        {
            super(silverfishIn, 1.0D, 10);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute()
        {
            if (this.creature.getAttackTarget() != null)
            {
                return false;
            }
            else if (!this.creature.getNavigator().noPath())
            {
                return false;
            }
            else {
                Random random = this.creature.getRNG();

                if (ForgeEventFactory.getMobGriefingEvent(this.creature.world, this.creature) && random.nextInt(10) == 0)
                {
                    this.facing = Direction.getRandomDirection(random);
                    BlockPos pos = (new BlockPos(this.creature.getPosX(), this.creature.getPosY() + 0.5D, this.creature.getPosZ())).offset(this.facing);
                    BlockState state = this.creature.world.getBlockState(pos);

                    if (GoldenSilverfishBlock.canContainGoldenSilverfish(state))
                    {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.shouldExecute();
            }
        }

        public boolean shouldContinueExecuting()
        {
            return !this.doMerge && super.shouldContinueExecuting();
        }

        public void startExecuting()
        {
            if (!this.doMerge)
            {
                super.startExecuting();
            }
            else {
                IWorld world = this.creature.world;
                BlockPos pos = (new BlockPos(this.creature.getPosX(), this.creature.getPosY() + 0.5D, this.creature.getPosZ())).offset(this.facing);
                BlockState state = world.getBlockState(pos);

                if (GoldenSilverfishBlock.canContainGoldenSilverfish(state))
                {
                    world.setBlockState(pos, GoldenSilverfishBlock.infest(state.getBlock()), 3);
                    this.creature.spawnExplosionParticle();
                    this.creature.remove();
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

        public boolean shouldExecute()
        {
            return this.lookForFriends > 0;
        }

        public void tick()
        {
            --this.lookForFriends;
            if (this.lookForFriends <= 0)
            {
                World world = this.goldenSilverfish.world;
                Random random = this.goldenSilverfish.getRNG();
                BlockPos pos = this.goldenSilverfish.getPosition();

                for(int y = 0; y <= 5 && y >= -5; y = (y <= 0 ? 1 : 0) - y)
                {
                    for(int x = 0; x <= 10 && x >= -10; x = (x <= 0 ? 1 : 0) - x)
                    {
                        for(int z = 0; z <= 10 && z >= -10; z = (z <= 0 ? 1 : 0) - z)
                        {
                            BlockPos adjacentPos = pos.add(x, y, z);
                            BlockState adjacentState = world.getBlockState(adjacentPos);
                            Block adjacentBlock = adjacentState.getBlock();

                            if (adjacentBlock instanceof GoldenSilverfishBlock)
                            {
                                if (ForgeEventFactory.getMobGriefingEvent(world, this.goldenSilverfish))
                                {
                                    world.destroyBlock(adjacentPos, true, this.goldenSilverfish);
                                }
                                else {
                                    world.setBlockState(adjacentPos, ((GoldenSilverfishBlock)adjacentBlock).getMimickedBlock().getDefaultState(), 3);
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

    /*
	private AISummonGoldenFish summonGoldenFish;
	
	public EntityGoldenFish(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootTableHandler.GOLDEN_FISH;
	}
	
	@Override
	protected void initEntityAI()
    {
        this.summonGoldenFish = new AISummonGoldenFish(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, this.summonGoldenFish);
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new AIHideInNetherrack(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
	
	static class AIHideInNetherrack extends EntityAIWander
    {
        private EnumFacing facing;
        private boolean doMerge;

        public AIHideInNetherrack(EntityGoldenFish goldenfishIn)
        {
            super(goldenfishIn, 1.0D, 10);
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute()
        {
            if (this.entity.getAttackTarget() != null)
            {
                return false;
            }
            else if (!this.entity.getNavigator().noPath())
            {
                return false;
            }
            else
            {
                Random random = this.entity.getRNG();

                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity) && random.nextInt(10) == 0)
                {
                    this.facing = EnumFacing.random(random);
                    BlockPos blockpos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                    IBlockState iblockstate = this.entity.world.getBlockState(blockpos);

                    if (BlockInfestedNetherrack.canContainSilverfish(iblockstate))
                    {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.shouldExecute();
            }
        }

        
        public boolean shouldContinueExecuting()
        {
            return this.doMerge ? false : super.shouldContinueExecuting();
        }

        @Override
        public void startExecuting()
        {
            if (!this.doMerge)
            {
                super.startExecuting();
            }
            else
            {
                World world = this.entity.world;
                BlockPos blockpos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                IBlockState iblockstate = world.getBlockState(blockpos);

                if (BlockInfestedNetherrack.canContainSilverfish(iblockstate))
                {
                    world.setBlockState(blockpos, BlocksInit.infested_netherrack.getDefaultState(), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
            }
        }
    }
	
	static class AISummonGoldenFish extends EntityAIBase
    {
        private final EntityGoldenFish goldenfish;
        private int lookForFriends;

        public AISummonGoldenFish(EntityGoldenFish goldenfishIn)
        {
            this.goldenfish = goldenfishIn;
        }

        public void notifyHurt()
        {
            if (this.lookForFriends == 0)
            {
                this.lookForFriends = 20;
            }
        }

        public boolean shouldExecute()
        {
            return this.lookForFriends > 0;
        }

        public void updateTask()
        {
            --this.lookForFriends;

            if (this.lookForFriends <= 0)
            {
                World world = this.goldenfish.world;
                Random random = this.goldenfish.getRNG();
                BlockPos blockpos = new BlockPos(this.goldenfish);

                for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i)
                {
                    for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j)
                    {
                        for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k)
                        {
                            BlockPos blockpos1 = blockpos.add(j, i, k);
                            IBlockState iblockstate = world.getBlockState(blockpos1);

                            if (iblockstate.getBlock() == BlocksInit.infested_netherrack)
                            {
                                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, this.goldenfish))
                                {
                                    world.destroyBlock(blockpos1, true);
                                }
                                else
                                {
                                    world.setBlockState(blockpos1, BlockInfestedNetherrack.getStateById(1), 3);
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
    */
}
