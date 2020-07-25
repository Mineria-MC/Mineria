package com.mineria.mod.entity;

import java.util.Random;

import com.mineria.mod.blocks.BlockInfestedNetherrack;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.handler.LootTableHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoldenFish extends EntitySilverfish
{
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

}
