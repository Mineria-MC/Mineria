package com.mineria.mod.entity;

import com.mineria.mod.blocks.BlockInfestedNetherrack;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.LootTableHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

public class EntityGoldenSilverfish extends EntitySilverfish
{
    private AISummonGoldenSilverfish summonGoldenSilverfish;

    public EntityGoldenSilverfish(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootTableHandler.GOLDEN_SILVERFISH;
	}
	
	@Override
	protected void initEntityAI()
    {
        this.summonGoldenSilverfish = new AISummonGoldenSilverfish(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, summonGoldenSilverfish);
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new AIHideInNetherrack(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonGoldenSilverfish != null)
            {
                this.summonGoldenSilverfish.notifyHurt();
            }

            return super.attackEntityFrom(source, amount);
        }
    }
	
	private static class AIHideInNetherrack extends EntityAIWander
    {
        private EnumFacing facing;
        private boolean doMerge;

        public AIHideInNetherrack(EntityGoldenSilverfish goldenSilverfish)
        {
            super(goldenSilverfish, 1.0D, 10);
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

                if (ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity) && random.nextInt(10) == 0)
                {
                    this.facing = EnumFacing.random(random);
                    BlockPos pos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                    IBlockState state = this.entity.world.getBlockState(pos);

                    if (BlockInfestedNetherrack.canContainSilverfish(state))
                    {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.shouldExecute();
            }
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return !this.doMerge && super.shouldContinueExecuting();
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
                BlockPos pos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                IBlockState state = world.getBlockState(pos);

                if (BlockInfestedNetherrack.canContainSilverfish(state))
                {
                    world.setBlockState(pos, BlocksInit.INFESTED_NETHERRACK.getDefaultState(), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
            }
        }
    }
	
	private static class AISummonGoldenSilverfish extends EntityAIBase
    {
        private final EntityGoldenSilverfish goldenSilverfish;
        private int lookForFriends;

        public AISummonGoldenSilverfish(EntityGoldenSilverfish goldenSilverfish)
        {
            this.goldenSilverfish = goldenSilverfish;
        }

        public void notifyHurt()
        {
            if (this.lookForFriends == 0)
            {
                this.lookForFriends = 20;
            }
        }

        @Override
        public boolean shouldExecute()
        {
            return this.lookForFriends > 0;
        }

        @Override
        public void updateTask()
        {
            --this.lookForFriends;

            if (this.lookForFriends <= 0)
            {
                World world = this.goldenSilverfish.world;
                Random rand = this.goldenSilverfish.getRNG();
                BlockPos pos = new BlockPos(this.goldenSilverfish);

                for (int y = 0; y <= 5 && y >= -5; y = (y <= 0 ? 1 : 0) - y)
                {
                    for (int x = 0; x <= 10 && x >= -10; x = (x <= 0 ? 1 : 0) - x)
                    {
                        for (int z = 0; z <= 10 && z >= -10; z = (z <= 0 ? 1 : 0) - z)
                        {
                            BlockPos goldenSilverfishPos = pos.add(x, y, z);
                            IBlockState state = world.getBlockState(goldenSilverfishPos);

                            if (state.getBlock() == BlocksInit.INFESTED_NETHERRACK)
                            {
                                if (ForgeEventFactory.getMobGriefingEvent(world, this.goldenSilverfish))
                                {
                                    world.destroyBlock(goldenSilverfishPos, true);
                                }
                                else
                                {
                                    world.setBlockState(goldenSilverfishPos, BlockInfestedNetherrack.getStateById(1), 3);
                                }
                                if (rand.nextBoolean())
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
