package com.mineria.mod.common.entity;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.init.MineriaEffects;
import com.mineria.mod.common.init.MineriaEntities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GreatDruidOfGaulsEntity extends MonsterEntity
{
    private static final Supplier<Int2ObjectMap<Object2IntMap<EntityType<? extends MobEntity>>>> ENTITIES_BY_WAVE = () -> Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(1, Object2IntMaps.singleton(EntityType.WOLF, 2));
        map.put(2, Util.make(new Object2IntArrayMap<>(), map2 -> {
            map2.put(EntityType.WOLF, 6);
            map2.put(MineriaEntities.DRUIDIC_WOLF.get(), 1);
        }));
        map.put(3, Util.make(new Object2IntArrayMap<>(), map2 -> {
            map2.put(EntityType.WOLF, 8);
            map2.put(MineriaEntities.BROWN_BEAR.get(), 2);
            map2.put(MineriaEntities.DRUIDIC_WOLF.get(), 2);
        }));
        map.put(4, Util.make(new Object2IntArrayMap<>(), map2 -> {
            map2.put(EntityType.WOLF, 10);
            map2.put(MineriaEntities.BROWN_BEAR.get(), 4);
            map2.put(MineriaEntities.DRUIDIC_WOLF.get(), 2);
        }));
        map.put(5, Util.make(new Object2IntArrayMap<>(), map2 -> {
            map2.put(MineriaEntities.FIRE_GOLEM.get(), 1);
            map2.put(MineriaEntities.DIRT_GOLEM.get(), 1);
            map2.put(MineriaEntities.AIR_SPIRIT.get(), 1);
            map2.put(MineriaEntities.WATER_SPIRIT.get(), 1);
        }));
    });
    private static final DataParameter<Integer> WAVE_DATA = EntityDataManager.defineId(GreatDruidOfGaulsEntity.class, DataSerializers.INT);

    private final Int2ObjectMap<Object2IntMap<EntityType<? extends MobEntity>>> entitiesByWave = ENTITIES_BY_WAVE.get();
    private final List<UUID> summonedEntitiesUUIDs = new ArrayList<>();
    private final NonNullList<MobEntity> summonedEntities = NonNullList.create();
    private boolean doTrigger;
    private int triggerCooldown;
    private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);

    public GreatDruidOfGaulsEntity(EntityType<? extends GreatDruidOfGaulsEntity> type, World world)
    {
        super(type, world);
        setNoGravity(true);
        setInvulnerable(true);
        setPersistenceRequired();
        this.xpReward = 80;
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SummonEntitiesGoal());
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)));
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(WAVE_DATA, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("CurrentWave", this.getCurrentWave());
        nbt.putBoolean("DoTrigger", this.doTrigger);
        nbt.putInt("TriggerCooldown", this.triggerCooldown);
        ListNBT list = new ListNBT();
        for(MobEntity entity : this.summonedEntities)
            list.add(NBTUtil.createUUID(entity.getUUID()));
        nbt.put("SummonedEntities", list);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        setCurrentWave(nbt.getInt("CurrentWave"));
        this.doTrigger = nbt.getBoolean("DoTrigger");
        this.triggerCooldown = nbt.getInt("TriggerCooldown");
        ListNBT list = nbt.getList("SummonedEntities", 11);
        list.stream().map(NBTUtil::loadUUID).forEach(this.summonedEntitiesUUIDs::add);
        if (this.hasCustomName())
        {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name)
    {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void tick()
    {
        if(this.summonedEntities.isEmpty())
        {
            if(this.level instanceof ServerWorld)
            {
                this.summonedEntitiesUUIDs.stream().map(id -> ((ServerWorld) this.level).getEntity(id)).map(MobEntity.class::cast).filter(Objects::nonNull).forEach(this.summonedEntities::add);
            }
        }
        summonedEntities.removeIf(entity -> !entity.isAlive());
        summonedEntities.forEach(this::tickEntity);
        super.tick();
        if(isCurrentWaveOver() && getCurrentWave() >= 5)
        {
            this.level.getNearbyPlayers(EntityPredicate.DEFAULT, this, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE), 8, this.getAttributeValue(Attributes.FOLLOW_RANGE)))
                    .forEach(playerEntity -> playerEntity.awardKillScore(this, this.deathScore, DamageSource.MAGIC));
            this.kill();
        }
        if(triggerCooldown > 0) --triggerCooldown;
        if(doTrigger && getTarget() != null)
        {
            LivingEntity target = getTarget();
            if(!level.isClientSide())
            {
                ServerWorld world = (ServerWorld) level;
                MineriaLightningBoltEntity.create(world, new BlockPos(target.position()), SpawnReason.EVENT, false, 0, target::equals).ifPresent(world::addFreshEntityWithPassengers);
            }
            target.addEffect(new EffectInstance(MineriaEffects.HALLUCINATIONS.get(), 600));
            target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
            doTrigger = false;
            triggerCooldown = 800;
        }
    }

    private void tickEntity(MobEntity summonedEntity)
    {
        summonedEntity.setTarget(this.getTarget());
        if(!isInRange(summonedEntity))
        {
            Vector3d targetPos = isInRange(this.getTarget()) ? getTarget().position() : this.position();
            summonedEntity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
        }
        if(PoisonEffectInstance.isEntityAffected(summonedEntity) && triggerCooldown <= 0)
        {
            summonedEntity.removeEffect(Effects.POISON);
            doTrigger = true;
        }
    }

    private boolean isInRange(Entity entity)
    {
        return entity != null && this.distanceToSqr(entity) <= this.getAttributeValue(Attributes.FOLLOW_RANGE) * this.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Nullable
    @Override
    public LivingEntity getKillCredit()
    {
        return null;
    }

    @Override
    protected void customServerAiStep()
    {
        super.customServerAiStep();
        this.bossEvent.setPercent(Math.min(5, (6 - this.getCurrentWave())) / 5.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return super.getDeathSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return super.getHurtSound(source);
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vector3d vec3)
    {
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player)
    {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player)
    {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean canChangeDimensions()
    {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity)
    {
        return false;
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_)
    {
        return false;
    }

    @Override
    public boolean addEffect(EffectInstance effect)
    {
        return false;
    }

    @Override
    public void push(double x, double y, double z)
    {
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt)
    {
        setInvulnerable(true);
        setNoGravity(true);
        setPersistenceRequired();
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }

    private int getCurrentWave()
    {
        return this.entityData.get(WAVE_DATA);
    }

    private void setCurrentWave(int waveIndex)
    {
        this.entityData.set(WAVE_DATA, waveIndex);
    }

    private boolean isCurrentWaveOver()
    {
        for(Entity entity : this.summonedEntities)
        {
            if(entity.isAlive())
            {
                return false;
            }
        }

        return true;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.FOLLOW_RANGE, 45);
    }

    @SubscribeEvent
    public static void modifyDrops(LivingDropsEvent event)
    {
        if(event.getEntity() instanceof GreatDruidOfGaulsEntity)
        {
            event.getDrops().forEach(item -> {
                item.setNoGravity(true);
                item.setInvulnerable(true);
                item.setGlowing(true);
            });
        }
    }

    class SummonEntitiesGoal extends Goal
    {
        @Override
        public boolean canUse()
        {
            return isCurrentWaveOver() && getCurrentWave() != 5;
        }

        @Override
        public void start()
        {
            if(summonEntities())
            {
                setCurrentWave(getCurrentWave() + 1);
                stop();
            }
        }

        @Override
        public void tick()
        {
            start();
        }

        public boolean summonEntities()
        {
            if(isCurrentWaveOver())
            {
                if(level instanceof ServerWorld)
                {
                    List<MobEntity> toAdd = new ArrayList<>();
                    ServerWorld world = (ServerWorld) level;
                    List<BlockPos> spawnRange = getPoses(blockPosition());

                    for(Object2IntMap.Entry<EntityType<? extends MobEntity>> entry : entitiesByWave.get(getCurrentWave() + 1).object2IntEntrySet())
                    {
                        EntitySpawnPlacementRegistry.PlacementType type = EntitySpawnPlacementRegistry.getPlacementType(entry.getKey());
                        List<BlockPos> spawnPositions = spawnRange.stream().filter(position -> WorldEntitySpawner.isSpawnPositionOk(type, world, position, entry.getKey())).collect(Collectors.toList());

                        if(spawnPositions.isEmpty())
                            return false;

                        for(int i = 0; i < entry.getIntValue(); i++)
                        {
                            BlockPos entityPos = spawnPositions.get(random.nextInt(spawnPositions.size()));
                            toAdd.add(entry.getKey().create(world, null, null, null, entityPos, SpawnReason.MOB_SUMMONED, false, false));
                        }
                    }

                    toAdd.stream().filter(Objects::nonNull).forEach(entity -> {
                        LivingEntity target = getTarget();
                        if(target != null)
                        {
                            if(target instanceof PlayerEntity && !((PlayerEntity) target).abilities.instabuild)
                            {
                                if(entity instanceof IAngerable)
                                    ((IAngerable) entity).setPersistentAngerTarget(target.getUUID());
                                entity.setTarget(target);
                            }
                        }
                        entity.setPersistenceRequired();
                        world.addFreshEntityWithPassengers(entity);
                        summonedEntities.add(entity);
                    });
                    return true;
                }
            }

            return false;
        }

        private List<BlockPos> getPoses(BlockPos pos)
        {
            List<BlockPos> result = new ArrayList<>();

            for(int y = pos.getY() - 5; y < pos.getY() + 5; y++)
            {
                for(int z = pos.getZ() - 10; z < pos.getZ() + 10; z++)
                {
                    for(int x = pos.getX() - 10; x < pos.getX() + 10; x++)
                    {
                        result.add(new BlockPos(x, y ,z));
                    }
                }
            }

            return result;
        }
    }
}
