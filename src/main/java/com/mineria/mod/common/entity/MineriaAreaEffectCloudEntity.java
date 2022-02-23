package com.mineria.mod.common.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

public class MineriaAreaEffectCloudEntity extends AreaEffectCloud
{
    private final Map<Entity, Integer> victims = Maps.newHashMap();

    public MineriaAreaEffectCloudEntity(EntityType<? extends MineriaAreaEffectCloudEntity> type, Level world)
    {
        super(type, world);
    }

    public MineriaAreaEffectCloudEntity(Level world, double x, double y, double z)
    {
        this(MineriaEntities.MINERIA_AREA_EFFECT_CLOUD.get(), world);
        setPos(x, y, z);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide) {
            this.setSharedFlag(6, this.isCurrentlyGlowing());
        }

        this.baseTick();
        boolean waiting = this.isWaiting();
        float radius = this.getRadius();

        if (this.level.isClientSide)
        {
            ParticleOptions particle = this.getParticle();
            if (waiting)
            {
                if (this.random.nextBoolean())
                {
                    for (int i = 0; i < 2; ++i)
                    {
                        float angle = this.random.nextFloat() * ((float) Math.PI * 2F);
                        float factor = Mth.sqrt(this.random.nextFloat()) * 0.2F;
                        float dx = Mth.cos(angle) * factor;
                        float dz = Mth.sin(angle) * factor;
                        if (particle.getType() == ParticleTypes.ENTITY_EFFECT)
                        {
                            int color = this.random.nextBoolean() ? 16777215 : this.getColor();
                            int red = color >> 16 & 255;
                            int green = color >> 8 & 255;
                            int blue = color & 255;
                            this.level.addAlwaysVisibleParticle(particle, this.getX() + (double) dx, this.getY(), this.getZ() + (double) dz, (double) ((float) red / 255.0F), (double) ((float) green / 255.0F), (double) ((float) blue / 255.0F));
                        } else
                        {
                            this.level.addAlwaysVisibleParticle(particle, this.getX() + (double) dx, this.getY(), this.getZ() + (double) dz, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            } else
            {
                float circleArea = (float) Math.PI * radius * radius;

                for (int pos = 0; (float) pos < circleArea; ++pos)
                {
                    float angle = this.random.nextFloat() * ((float) Math.PI * 2F);
                    float factor = Mth.sqrt(this.random.nextFloat()) * radius;
                    float dx = Mth.cos(angle) * factor;
                    float dz = Mth.sin(angle) * factor;
                    if (particle.getType() == ParticleTypes.ENTITY_EFFECT)
                    {
                        int color = this.getColor();
                        int red = color >> 16 & 255;
                        int green = color >> 8 & 255;
                        int blue = color & 255;
                        this.level.addAlwaysVisibleParticle(particle, this.getX() + (double) dx, this.getY(), this.getZ() + (double) dz, (double) ((float) red / 255.0F), (double) ((float) green / 255.0F), (double) ((float) blue / 255.0F));
                    } else
                    {
                        this.level.addAlwaysVisibleParticle(particle, this.getX() + (double) dx, this.getY(), this.getZ() + (double) dz, (0.5D - this.random.nextDouble()) * 0.15D, (double) 0.01F, (0.5D - this.random.nextDouble()) * 0.15D);
                    }
                }
            }
        } else
        {
            if (this.tickCount >= this.waitTime + this.getDuration())
            {
                this.discard();
                return;
            }

            boolean flag1 = this.tickCount < this.waitTime;
            if (waiting != flag1)
            {
                this.setWaiting(flag1);
            }

            if (flag1)
            {
                return;
            }

            if (this.radiusPerTick != 0.0F)
            {
                radius += this.radiusPerTick;
                if (radius < 0.5F)
                {
                    this.discard();
                    return;
                }

                this.setRadius(radius);
            }

            if (this.tickCount % 5 == 0)
            {
                this.victims.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());

                List<MobEffectInstance> effects = Lists.newArrayList();

                for (MobEffectInstance effectInstance : this.potion.getEffects())
                {
                    if(effectInstance instanceof CustomEffectInstance)
                    {
                        ((CustomEffectInstance) effectInstance).setDuration(effectInstance.getDuration() / 4);
                        effects.add(effectInstance);
                    }
                    else
                        effects.add(new MobEffectInstance(effectInstance.getEffect(), effectInstance.getDuration() / 4, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()));
                }

                effects.addAll(this.effects);
                if (effects.isEmpty())
                {
                    this.victims.clear();
                } else
                {
                    List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                    if (!entities.isEmpty())
                    {
                        for (LivingEntity living : entities)
                        {
                            if (!this.victims.containsKey(living) && living.isAffectedByPotions())
                            {
                                double distanceX = living.getX() - this.getX();
                                double distanceZ = living.getZ() - this.getZ();
                                double distance = distanceX * distanceX + distanceZ * distanceZ;
                                if (distance <= (double) (radius * radius))
                                {
                                    this.victims.put(living, this.tickCount + this.reapplicationDelay);

                                    for (MobEffectInstance effect : effects)
                                    {
                                        if (effect.getEffect().isInstantenous())
                                        {
                                            effect.getEffect().applyInstantenousEffect(this, this.getOwner(), living, effect.getAmplifier(), 0.5D);
                                        } else
                                        {
                                            living.addEffect(CustomEffectInstance.copyEffect(effect));
                                        }
                                    }

                                    if (this.radiusOnUse != 0.0F)
                                    {
                                        radius += this.radiusOnUse;
                                        if (radius < 0.5F)
                                        {
                                            this.discard();
                                            return;
                                        }

                                        this.setRadius(radius);
                                    }

                                    if (this.durationOnUse != 0)
                                    {
                                        this.setDuration(this.getDuration() + this.durationOnUse);
                                        if (this.getDuration() <= 0)
                                        {
                                            this.discard();
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
    }

    /*private static int getWaitTime(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getWaitTime();
    }

    private static float getRadiusPerTick(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getRadiusPerTick();
    }

    private static Potion getPotion(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getPotion();
    }

    private static List<EffectInstance> getEffects(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getEffects();
    }

    private static int getReapplicationDelay(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getReapplicationDelay();
    }

    private static float getRadiusOnUse(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getRadiusOnUse();
    }

    private static int getDurationOnUse(AreaEffectCloudEntity entity)
    {
        return ((AreaEffectCloudEntityAccessor) entity).getDurationOnUse();
    }*/
}
