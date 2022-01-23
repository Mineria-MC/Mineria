package com.mineria.mod.common.entity;

import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class MineriaPotionEntity extends ProjectileItemEntity implements IRendersAsItem
{
    public MineriaPotionEntity(EntityType<? extends MineriaPotionEntity> type, World world)
    {
        super(type, world);
    }

    public MineriaPotionEntity(World world, LivingEntity living)
    {
        super(MineriaEntities.MINERIA_POTION.get(), living, world);
    }

    @OnlyIn(Dist.CLIENT)
    public MineriaPotionEntity(World world, double x, double y, double z)
    {
        super(MineriaEntities.MINERIA_POTION.get(), x, y, z, world);
    }

    @Override
    protected Item getDefaultItem()
    {
        return MineriaItems.MINERIA_SPLASH_POTION;
    }

    @Override
    protected float getGravity()
    {
        return 0.05F;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult rayTraceResult)
    {
        super.onHitBlock(rayTraceResult);
        if (!this.level.isClientSide)
        {
            ItemStack itemstack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
            boolean flag = potion == Potions.WATER && list.isEmpty();
            Direction direction = rayTraceResult.getDirection();
            BlockPos blockpos = rayTraceResult.getBlockPos();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (flag)
            {
                this.dowseFire(blockpos1, direction);
                this.dowseFire(blockpos1.relative(direction.getOpposite()), direction);

                for (Direction direction1 : Direction.Plane.HORIZONTAL)
                {
                    this.dowseFire(blockpos1.relative(direction1), direction1);
                }
            }

        }
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult)
    {
        super.onHit(rayTraceResult);
        if (!this.level.isClientSide)
        {
            ItemStack itemstack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
            boolean water = potion == Potions.WATER && list.isEmpty();
            if (water)
            {
                this.applyWater();
            } else if (!list.isEmpty())
            {
                if (this.isLingering())
                {
                    this.makeAreaOfEffectCloud(itemstack, potion);
                } else
                {
                    this.applySplash(list, rayTraceResult.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult) rayTraceResult).getEntity() : null);
                }
            }

            int i = potion.hasInstantEffects() ? 2007 : 2002;
            this.level.levelEvent(i, this.blockPosition(), PotionUtils.getColor(itemstack));
            this.remove();
        }
    }

    private void applyWater()
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb, LivingEntity::isSensitiveToWater);
        if (!list.isEmpty())
        {
            for (LivingEntity livingentity : list)
            {
                double d0 = this.distanceToSqr(livingentity);
                if (d0 < 16.0D && livingentity.isSensitiveToWater())
                {
                    livingentity.hurt(DamageSource.indirectMagic(livingentity, this.getOwner()), 1.0F);
                }
            }
        }
    }

    private void applySplash(List<EffectInstance> effects, @Nullable Entity target)
    {
        AxisAlignedBB boundingBox = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, boundingBox);

        if (!entities.isEmpty())
        {
            for (LivingEntity living : entities)
            {
                if (living.isAffectedByPotions())
                {
                    double distanceSqrt = this.distanceToSqr(living);
                    if (distanceSqrt < 16.0D)
                    {
                        double distance = 1.0D - Math.sqrt(distanceSqrt) / 4.0D;
                        if (living == target)
                        {
                            distance = 1.0D;
                        }

                        for (EffectInstance instance : effects)
                        {
                            Effect effect = instance.getEffect();
                            if (effect.isInstantenous())
                            {
                                effect.applyInstantenousEffect(this, this.getOwner(), living, instance.getAmplifier(), distance);
                            } else
                            {
                                int newDuration = (int) (distance * (double) instance.getDuration() + 0.5D);
                                if (newDuration > 20)
                                {
                                    if(instance instanceof CustomEffectInstance)
                                    {
                                        ((CustomEffectInstance) instance).setDuration(newDuration);
                                        living.addEffect(instance);
                                    }
                                    else
                                        living.addEffect(new EffectInstance(effect, newDuration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible()));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void makeAreaOfEffectCloud(ItemStack stack, Potion potion)
    {
        MineriaAreaEffectCloudEntity effectCloud = new MineriaAreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
        Entity owner = this.getOwner();

        if (owner instanceof LivingEntity)
        {
            effectCloud.setOwner((LivingEntity) owner);
        }

        effectCloud.setRadius(3.0F);
        effectCloud.setRadiusOnUse(-0.5F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(-effectCloud.getRadius() / (float) effectCloud.getDuration());
        effectCloud.setPotion(potion);

        for (EffectInstance effect : PotionUtils.getCustomEffects(stack))
        {
            effectCloud.addEffect(CustomEffectInstance.copyEffect(effect));
        }

        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("CustomPotionColor", 99))
        {
            effectCloud.setFixedColor(nbt.getInt("CustomPotionColor"));
        }

        this.level.addFreshEntity(effectCloud);
    }

    private boolean isLingering()
    {
        return this.getItem().getItem() == MineriaItems.MINERIA_LINGERING_POTION;
    }

    private void dowseFire(BlockPos pos, Direction direction)
    {
        BlockState state = this.level.getBlockState(pos);
        if (state.is(BlockTags.FIRE))
        {
            this.level.removeBlock(pos, false);
        } else if (CampfireBlock.isLitCampfire(state))
        {
            this.level.levelEvent(null, 1009, pos, 0);
            CampfireBlock.dowse(this.level, pos, state);
            this.level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
        }
    }
}
