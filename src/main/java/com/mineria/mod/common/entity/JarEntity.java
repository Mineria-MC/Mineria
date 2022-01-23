package com.mineria.mod.common.entity;

import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.items.JarItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class JarEntity extends ProjectileItemEntity implements IRendersAsItem
{
    public JarEntity(EntityType<? extends JarEntity> type, World world)
    {
        super(type, world);
    }

    @OnlyIn(Dist.CLIENT)
    public JarEntity(double x, double y, double z, World world)
    {
        super(MineriaEntities.JAR.get(), x, y, z, world);
    }

    public JarEntity(LivingEntity living, World world)
    {
        super(MineriaEntities.JAR.get(), living, world);
    }

    @Override
    protected Item getDefaultItem()
    {
        return MineriaItems.JAR;
    }

    @Override
    protected float getGravity()
    {
        return 0.05F;
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult)
    {
        super.onHit(rayTraceResult);

        if (!this.level.isClientSide)
        {
            ItemStack stack = this.getItem();
            PoisonSource poisonSource = JarItem.getPoisonSourceFromStack(stack);

            if (this.isLingering())
            {
//                this.makeAreaOfEffectCloud(stack, potion);
            } else
            {
                this.applySplash(poisonSource, rayTraceResult.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult) rayTraceResult).getEntity() : null);
            }

            this.level.levelEvent(2002, this.blockPosition(), poisonSource.getColor());
            this.remove();
        }
    }

    private void applySplash(PoisonSource poisonSource, @Nullable Entity target)
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

                        poisonSource.poison(living);
                    }
                }
            }
        }
    }

    /*private void makeAreaOfEffectCloud(ItemStack stack, Potion potion)
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
    }*/

    private boolean isLingering()
    {
        return JarItem.isLingering(this.getItem());
    }
}
