package com.mineria.mod.common.entity;

import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.enchantments.FourElementsEnchantment;
import com.mineria.mod.common.init.MineriaEffects;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.util.DamageSourceUtil;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ElementalOrbEntity extends Entity
{
    private UUID ownerUUID;
    private int ownerNetworkId;
    private FourElementsEnchantment.ElementType elementType = FourElementsEnchantment.ElementType.NONE;
    private static final DataParameter<Integer> ELEMENT_TYPE_ID = EntityDataManager.defineId(ElementalOrbEntity.class, DataSerializers.INT);
    private final Map<LivingEntity, Integer> hitCount = new HashMap<>();
    private int delay;
    private int currentPosIndex;
    private static final DataParameter<Integer> CURRENT_POS_INDEX = EntityDataManager.defineId(ElementalOrbEntity.class, DataSerializers.INT);

    public ElementalOrbEntity(EntityType<?> type, World world)
    {
        super(type, world);
        this.noPhysics = true;
    }

    public ElementalOrbEntity(World world, Entity owner, FourElementsEnchantment.ElementType elementType, int startPosIndex)
    {
        this(MineriaEntities.ELEMENTAL_ORB.get(), world);
        setOwner(owner);
        setElementType(elementType);
        setCurrentPosIndex(startPosIndex);
    }

    public ElementalOrbEntity(World world, double x, double y, double z)
    {
        this(MineriaEntities.ELEMENTAL_ORB.get(), world);
        this.setPos(x, y, z);
    }

    @Override
    public void tick()
    {
        super.tick();

        Entity owner = this.getOwner();
        if(owner != null && isOwnerValid())
        {
            Vector3d nextPos = getNextPos(owner.position());
            setPos(nextPos.x, owner.getY() + 0.45, nextPos.z);

            if(delay > 0)
            {
                delay--;
            }
            else
            {
                List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()).stream().filter(living -> !living.getUUID().equals(ownerUUID)).collect(Collectors.toList());

                if(!entities.isEmpty())
                {
                    this.delay = getMaxDelay(this.getElementType());

                    for(LivingEntity living : entities)
                    {
                        if(hitCount.computeIfPresent(living, (living1, count) -> count + 1) == null)
                            hitCount.putIfAbsent(living, 1);

                        applyEffect(living);
                    }
                }
            }
        } else
            this.remove();
    }

    private boolean isOwnerValid()
    {
        Entity owner = this.getOwner();
        return owner != null && owner.isAlive() && (!(owner instanceof ServerPlayerEntity) || !owner.isSpectator());
    }

    private Vector3d getNextPos(Vector3d ownerPos)
    {
        int currentPosIndex = getCurrentPosIndex();
        Vector3d vec3 = POSITIONS.get(currentPosIndex);

        if(currentPosIndex + 1 >= POSITIONS.size())
            setCurrentPosIndex(0);
        else
            setCurrentPosIndex(currentPosIndex + 1);

        return ownerPos.add(vec3.x, 0, vec3.z);
    }

    private void applyEffect(LivingEntity living)
    {
        switch (this.getElementType())
        {
            case FIRE:
                if(!this.level.isClientSide)
                {
                    if(!living.fireImmune())
                    {
                        living.setSecondsOnFire(10);
                        living.hurt(DamageSourceUtil.elementalOrb(this, this.getOwner()), 2);
                    }
                }
                break;
            case WATER:
                if(hitCount.get(living) >= 6)
                {
                    hitCount.replace(living, 0);
                    living.setAirSupply(-650);
                    living.addEffect(new EffectInstance(MineriaEffects.DROWNING.get(), 32770));
                } else
                {
                    living.setAirSupply(living.getAirSupply() - 60);
                    living.hurt(DamageSource.DROWN, 1);
                }
                break;
            case AIR:
                living.setDeltaMovement(living.getDeltaMovement().add(0, 1, 0));
                living.hurt(DamageSourceUtil.elementalOrb(this, this.getOwner()), 1);
                break;
            case GROUND:
                if(!this.level.isClientSide)
                {
                    if(!PoisonEffectInstance.isEntityAffected(living))
                    {
                        PoisonEffectInstance.applyPoisonEffect(living, 0, 24000, 0, PoisonSource.UNKNOWN);
                    }
                    living.hurt(DamageSourceUtil.elementalOrb(this, this.getOwner()), 1);
                }
                break;
        }
    }

    private static int getMaxDelay(FourElementsEnchantment.ElementType type)
    {
        switch (type)
        {
            case FIRE:
                return 5;
            case WATER:
                return 30;
            case AIR:
                return 10;
            case GROUND:
                return 40;
        }
        return 0;
    }

    public void setOwner(@Nullable Entity owner)
    {
        if(owner != null)
        {
            this.ownerUUID = owner.getUUID();
            this.ownerNetworkId = owner.getId();
        }
    }

    @Nullable
    public Entity getOwner()
    {
        if (this.ownerUUID != null && this.level instanceof ServerWorld)
        {
            return ((ServerWorld) this.level).getEntity(this.ownerUUID);
        } else
        {
            return this.ownerNetworkId != 0 ? this.level.getEntity(this.ownerNetworkId) : null;
        }
    }

    public void setElementType(FourElementsEnchantment.ElementType elementType)
    {
        this.elementType = elementType;
        this.entityData.set(ELEMENT_TYPE_ID, elementType.getId());
    }

    public FourElementsEnchantment.ElementType getElementType()
    {
        return this.level.isClientSide ? FourElementsEnchantment.ElementType.byId(this.entityData.get(ELEMENT_TYPE_ID)) : this.elementType;
    }

    public void setCurrentPosIndex(int currentPosIndex)
    {
        this.currentPosIndex = currentPosIndex;
        this.entityData.set(CURRENT_POS_INDEX, currentPosIndex);
    }

    public int getCurrentPosIndex()
    {
        return this.level.isClientSide ? this.entityData.get(CURRENT_POS_INDEX) : currentPosIndex;
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(ELEMENT_TYPE_ID, 0);
        this.entityData.define(CURRENT_POS_INDEX, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt)
    {
        if (nbt.hasUUID("Owner"))
        {
            this.ownerUUID = nbt.getUUID("Owner");
        }
        this.setElementType(FourElementsEnchantment.ElementType.byId(nbt.getInt("ElementType")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt)
    {
        if (this.ownerUUID != null)
        {
            nbt.putUUID("Owner", this.ownerUUID);
        }
        nbt.putInt("ElementType", this.elementType.getId());
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return new SSpawnObjectPacket(this, this.ownerNetworkId);
    }

    @Override
    public PushReaction getPistonPushReaction()
    {
        return PushReaction.IGNORE;
    }

    private static final List<Vector3d> POSITIONS = calculatePositions(1.25);

    private static List<Vector3d> calculatePositions(double distance)
    {
        List<Vector3d> result = new ArrayList<>();

        for(int ang = 0; ang < 23; ang++)
        {
            double dx = distance * Math.cos(ang * 6);
            double dz = distance * Math.sin(ang * 6);
            result.add(new Vector3d(dx, 0, dz));
        }

        return result;
    }
}
