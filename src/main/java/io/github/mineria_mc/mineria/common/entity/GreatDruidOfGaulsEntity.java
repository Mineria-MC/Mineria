package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
public class GreatDruidOfGaulsEntity extends Monster {
    private static final Supplier<Int2ObjectMap<Object2IntMap<EntityType<? extends Mob>>>> ENTITIES_BY_WAVE = () -> Util.make(new Int2ObjectOpenHashMap<>(), map -> {
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
    private static final EntityDataAccessor<Integer> WAVE_DATA = SynchedEntityData.defineId(GreatDruidOfGaulsEntity.class, EntityDataSerializers.INT);

    private final Int2ObjectMap<Object2IntMap<EntityType<? extends Mob>>> entitiesByWave = ENTITIES_BY_WAVE.get();
    private final List<UUID> summonedEntitiesUUIDs = new ArrayList<>();
    private final NonNullList<Mob> summonedEntities = NonNullList.create();
    private boolean doTrigger;
    private int triggerCooldown;
    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public GreatDruidOfGaulsEntity(EntityType<? extends GreatDruidOfGaulsEntity> type, Level world) {
        super(type, world);
        setNoGravity(true);
        setInvulnerable(true);
        setPersistenceRequired();
        this.xpReward = 80;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SummonEntitiesGoal());
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new NearestAttackableTargetGoal<>(this, Player.class, true)));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WAVE_DATA, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("CurrentWave", this.getCurrentWave());
        nbt.putBoolean("DoTrigger", this.doTrigger);
        nbt.putInt("TriggerCooldown", this.triggerCooldown);
        ListTag list = new ListTag();
        for (Mob entity : this.summonedEntities)
            list.add(NbtUtils.createUUID(entity.getUUID()));
        nbt.put("SummonedEntities", list);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setCurrentWave(nbt.getInt("CurrentWave"));
        this.doTrigger = nbt.getBoolean("DoTrigger");
        this.triggerCooldown = nbt.getInt("TriggerCooldown");
        ListTag list = nbt.getList("SummonedEntities", 11);
        list.stream().map(NbtUtils::loadUUID).forEach(this.summonedEntitiesUUIDs::add);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void tick() {
        if (this.summonedEntities.isEmpty()) {
            if (this.level instanceof ServerLevel) {
                this.summonedEntitiesUUIDs.stream().map(id -> ((ServerLevel) this.level).getEntity(id)).map(Mob.class::cast).filter(Objects::nonNull).forEach(this.summonedEntities::add);
            }
        }
        summonedEntities.removeIf(entity -> !entity.isAlive());
        summonedEntities.forEach(this::tickEntity);
        super.tick();
        if (isCurrentWaveOver() && getCurrentWave() >= 5) {
            this.level.getNearbyPlayers(TargetingConditions.forNonCombat(), this, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE), 8, this.getAttributeValue(Attributes.FOLLOW_RANGE)))
                    .forEach(playerEntity -> playerEntity.awardKillScore(this, this.deathScore, damageSources().magic()));
            this.kill();
        }
        if (triggerCooldown > 0) --triggerCooldown;
        if (doTrigger && getTarget() != null) {
            LivingEntity target = getTarget();
            if (!level.isClientSide()) {
                ServerLevel world = (ServerLevel) level;
                MineriaLightningBoltEntity.create(world, BlockPos.containing(target.position()), MobSpawnType.EVENT, false, 0, target::equals).ifPresent(world::addFreshEntityWithPassengers);
            }
            target.addEffect(new MobEffectInstance(MineriaEffects.HALLUCINATIONS.get(), 600));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200));
            doTrigger = false;
            triggerCooldown = 800;
        }
    }

    private void tickEntity(Mob summonedEntity) {
        summonedEntity.setTarget(this.getTarget());
        if (!isInRange(summonedEntity)) {
            Vec3 targetPos = isInRange(this.getTarget()) ? getTarget().position() : this.position();
            summonedEntity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
        }
        if (PoisonMobEffectInstance.isEntityAffected(summonedEntity) && triggerCooldown <= 0) {
            summonedEntity.removeEffect(MobEffects.POISON);
            doTrigger = true;
        }
    }

    private boolean isInRange(Entity entity) {
        return entity != null && this.distanceToSqr(entity) <= this.getAttributeValue(Attributes.FOLLOW_RANGE) * this.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Nullable
    @Override
    public LivingEntity getKillCredit() {
        return null;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(Math.min(5, (6 - this.getCurrentWave())) / 5.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return super.getHurtSound(source);
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 vec3) {
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public boolean addEffect(MobEffectInstance effect, @Nullable Entity entity) {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        setInvulnerable(true);
        setNoGravity(true);
        setPersistenceRequired();
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }

    private int getCurrentWave() {
        return this.entityData.get(WAVE_DATA);
    }

    private void setCurrentWave(int waveIndex) {
        this.entityData.set(WAVE_DATA, waveIndex);
    }

    private boolean isCurrentWaveOver() {
        for (Entity entity : this.summonedEntities) {
            if (entity.isAlive()) {
                return false;
            }
        }

        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.FOLLOW_RANGE, 45);
    }

    @SubscribeEvent
    public static void modifyDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof GreatDruidOfGaulsEntity) {
            event.getDrops().forEach(item -> {
                item.setNoGravity(true);
                item.setInvulnerable(true);
                item.setGlowingTag(true);
            });
        }
    }

    class SummonEntitiesGoal extends Goal {
        @Override
        public boolean canUse() {
            return isCurrentWaveOver() && getCurrentWave() != 5;
        }

        @Override
        public void start() {
            if (summonEntities()) {
                setCurrentWave(getCurrentWave() + 1);
                stop();
            }
        }

        @Override
        public void tick() {
            start();
        }

        public boolean summonEntities() {
            if (isCurrentWaveOver()) {
                if (level instanceof ServerLevel) {
                    List<Mob> toAdd = new ArrayList<>();
                    ServerLevel world = (ServerLevel) level;
                    List<BlockPos> spawnRange = getPoses(blockPosition());

                    for (Object2IntMap.Entry<EntityType<? extends Mob>> entry : entitiesByWave.get(getCurrentWave() + 1).object2IntEntrySet()) {
                        SpawnPlacements.Type type = SpawnPlacements.getPlacementType(entry.getKey());
                        List<BlockPos> spawnPositions = spawnRange.stream().filter(position -> NaturalSpawner.isSpawnPositionOk(type, world, position, entry.getKey())).collect(Collectors.toList());

                        if (spawnPositions.isEmpty())
                            return false;

                        for (int i = 0; i < entry.getIntValue(); i++) {
                            BlockPos entityPos = spawnPositions.get(random.nextInt(spawnPositions.size()));
                            toAdd.add(entry.getKey().create(world, null, null, entityPos, MobSpawnType.MOB_SUMMONED, false, false));
                        }
                    }

                    toAdd.stream().filter(Objects::nonNull).forEach(entity -> {
                        LivingEntity target = getTarget();
                        if (target != null) {
                            if (target instanceof Player && !((Player) target).getAbilities().instabuild) {
                                if (entity instanceof NeutralMob)
                                    ((NeutralMob) entity).setPersistentAngerTarget(target.getUUID());
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

        private List<BlockPos> getPoses(BlockPos pos) {
            List<BlockPos> result = new ArrayList<>();

            for (int y = pos.getY() - 5; y < pos.getY() + 5; y++) {
                for (int z = pos.getZ() - 10; z < pos.getZ() + 10; z++) {
                    for (int x = pos.getX() - 10; x < pos.getX() + 10; x++) {
                        result.add(new BlockPos(x, y, z));
                    }
                }
            }

            return result;
        }
    }
}
