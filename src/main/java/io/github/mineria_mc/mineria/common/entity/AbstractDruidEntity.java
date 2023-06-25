package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.ritual_table.RitualTableBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.TradeTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractDruidEntity extends Monster implements Merchant, NeutralMob {
    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(AbstractDruidEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<BlockPos>> RITUAL_POSITION = SynchedEntityData.defineId(AbstractDruidEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Optional<BlockPos>> RITUAL_TABLE_POSITION = SynchedEntityData.defineId(AbstractDruidEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Boolean> INVOKING = SynchedEntityData.defineId(AbstractDruidEntity.class, EntityDataSerializers.BOOLEAN);
    protected int spellCastingTickCount;
    private SpellType currentSpell = SpellType.NONE;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int tradeXp;
    private int updateMerchantTimer;
    private boolean increaseProfessionLevelOnUpdate;
    private int remainingPersistentAngerTime;
    private int tradeLevel = 1;
    private UUID persistentAngerTarget;
    @Nullable
    private Player tradingPlayer;
    @Nullable
    private MerchantOffers offers;
    private long lastRestockGameTime;

    public AbstractDruidEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.xpReward = 15;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        MerchantOffers offers = this.getOffers();
        if (!offers.isEmpty())
            nbt.put("Offers", offers.createTag());
        nbt.putInt("TradeXp", this.tradeXp);
        nbt.putInt("TradeLevel", this.tradeLevel);
        nbt.putInt("SpellTicks", this.spellCastingTickCount);
        nbt.putLong("LastRestock", this.lastRestockGameTime);
        if (getRitualPosition().isPresent()) {
            CompoundTag ritualPosNbt = new CompoundTag();
            BlockPos ritualPos = getRitualPosition().get();
            ritualPosNbt.putInt("X", ritualPos.getX());
            ritualPosNbt.putInt("Y", ritualPos.getY());
            ritualPosNbt.putInt("Z", ritualPos.getZ());
            nbt.put("RitualPosition", ritualPosNbt);
        }
        if (getRitualTablePosition().isPresent()) {
            CompoundTag ritualTablePosNbt = new CompoundTag();
            BlockPos ritualTablePos = getRitualTablePosition().get();
            ritualTablePosNbt.putInt("X", ritualTablePos.getX());
            ritualTablePosNbt.putInt("Y", ritualTablePos.getY());
            ritualTablePosNbt.putInt("Z", ritualTablePos.getZ());
            nbt.put("RitualTablePosition", ritualTablePosNbt);
        }

        addPersistentAngerSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Offers", 10))
            this.offers = new MerchantOffers(nbt.getCompound("Offers"));
        if (nbt.contains("TradeXp", 3))
            this.tradeXp = nbt.getInt("TradeXp");
        if (nbt.contains("TradeLevel", 3))
            this.tradeLevel = nbt.getInt("TradeLevel");
        this.spellCastingTickCount = nbt.getInt("SpellTicks");
        this.lastRestockGameTime = nbt.getLong("LastRestock");
        if (nbt.contains("RitualPosition")) {
            CompoundTag ritualPosNbt = nbt.getCompound("RitualPosition");
            setRitualPosition(new BlockPos(ritualPosNbt.getInt("X"), ritualPosNbt.getInt("Y"), ritualPosNbt.getInt("Z")));
        }
        if (nbt.contains("RitualTablePosition")) {
            CompoundTag ritualTablePosNbt = nbt.getCompound("RitualTablePosition");
            setRitualTablePosition(new BlockPos(ritualTablePosNbt.getInt("X"), ritualTablePosNbt.getInt("Y"), ritualTablePosNbt.getInt("Z")));
        }

        if (!level.isClientSide) readPersistentAngerSaveData((ServerLevel) level, nbt);
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isTrading() && this.updateMerchantTimer > 0) {
            --this.updateMerchantTimer;
            if (this.updateMerchantTimer <= 0) {
                if (this.increaseProfessionLevelOnUpdate) {
                    this.increaseTradeLevel();
                    this.increaseProfessionLevelOnUpdate = false;
                }

                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
            }
        }

        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
        }

        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (shouldRestock()) restock();

        for (LivingEntity living : this.level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, this, this.getBoundingBox().inflate(20, 8, 20))) {
            if (living.isInWater() && !living.equals(this.getTarget())) {
                living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50));
            }
        }

        if (getRitualTablePosition().isPresent()) {
            BlockEntity tile = this.level.getBlockEntity(this.getRitualTablePosition().get());
            if (!(tile instanceof RitualTableBlockEntity)) {
                setRitualTablePosition(null);
                setRitualPosition(null);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.isCastingSpell()) {
            SpellType spell = this.getCurrentSpell();
            double red = spell.spellColor[0];
            double green = spell.spellColor[1];
            double blue = spell.spellColor[2];
            float f = this.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
            float dx = Mth.cos(f);
            float dz = Mth.sin(f);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double) dx * 0.6D, this.getY() + 1.8D, this.getZ() + (double) dz * 0.6D, red, green, blue);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double) dx * 0.6D, this.getY() + 1.8D, this.getZ() - (double) dz * 0.6D, red, green, blue);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.getRitualPosition().ifPresent(pos -> {
            SoundEvent sound = getRitualSound();
            if (tickCount % 25 == 0 && sound != null) {
                playSound(sound, 0.2F, getVoicePitch());
            }
        });
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SPELL_CASTING_ID, (byte) 0);
        this.entityData.define(RITUAL_POSITION, Optional.empty());
        this.entityData.define(RITUAL_TABLE_POSITION, Optional.empty());
        this.entityData.define(INVOKING, false);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return entity instanceof AbstractDruidEntity || super.isAlliedTo(entity);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MineriaSounds.DRUID_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return getRitualPosition().isPresent() ? getRitualSound() : MineriaSounds.DRUID_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MineriaSounds.DRUID_DEATH.get();
    }

    @Nullable
    protected SoundEvent getRitualSound() {
        return MineriaSounds.DRUID_NO.get();
    }

    public boolean isCastingSpell() {
        if (this.level.isClientSide) {
            return this.entityData.get(DATA_SPELL_CASTING_ID) > 0;
        } else {
            return this.spellCastingTickCount > 0;
        }
    }

    public void castSpell(SpellType spell) {
        this.currentSpell = spell;
        this.entityData.set(DATA_SPELL_CASTING_ID, (byte) spell.id);
    }

    public SpellType getCurrentSpell() {
        return level.isClientSide ? SpellType.byId(this.entityData.get(DATA_SPELL_CASTING_ID)) : this.currentSpell;
    }

    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }

    protected abstract SoundEvent getCastingSoundEvent();

    public ArmPose getArmPose() {
        if (remainingPersistentAngerTime > 0) {
            return ArmPose.ATTACKING;
        }
        if (isCastingSpell() || isInvoking()) {
            return ArmPose.SPELLCASTING;
        }
        return ArmPose.CROSSED;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        stopTrading();
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel p_241206_1_, ITeleporter teleporter) {
        stopTrading();
        return getRitualPosition().isPresent() ? null : super.changeDimension(p_241206_1_, teleporter);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isAlive() && !isTrading() && !player.isSecondaryUseActive() && !player.equals(this.getTarget()) && !getRitualPosition().isPresent()) {
            if (!this.getOffers().isEmpty()) {
                if (!this.level.isClientSide) {
                    this.setTradingPlayer(player);
                    this.openTradingScreen(player, this.getDisplayName(), this.tradeLevel);
                }

            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypeTags.IS_LIGHTNING) || super.isInvulnerableTo(source);
    }

    protected abstract Int2ObjectMap<List<VillagerTrades.ItemListing>> getTrades();

    protected void rewardTradeXp(MerchantOffer offer) {
        int xp = 3 + this.random.nextInt(4);
        this.tradeXp += offer.getXp();
        if (this.shouldIncreaseLevel()) {
            this.updateMerchantTimer = 40;
            this.increaseProfessionLevelOnUpdate = true;
            xp += 5;
        }

        if (offer.shouldRewardExp()) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), xp));
        }
    }

    protected void stopTrading() {
        setTradingPlayer(null);
    }

    protected void updateTrades() {
        List<VillagerTrades.ItemListing> trades = getTrades().get(tradeLevel);
        if (trades != null) {
            this.addOffersFromItemListings(this.getOffers(), trades, 10);
        }
    }

    protected void addOffersFromItemListings(MerchantOffers offers, List<VillagerTrades.ItemListing> trades, int tradeAmount) {
        List<VillagerTrades.ItemListing> copy = new ArrayList<>(trades);
        if (trades.size() > tradeAmount) {
            for (int i = 0; i < tradeAmount; i++) {
                VillagerTrades.ItemListing trade = copy.remove(random.nextInt(tradeAmount));
                MerchantOffer offer = trade.getOffer(this, this.random);
                if (offer != null)
                    offers.add(offer);
            }
        } else {
            for (VillagerTrades.ItemListing trade : trades) {
                MerchantOffer offer = trade.getOffer(this, this.random);
                if (offer != null)
                    offers.add(offer);
            }
        }
    }

    protected int getTradeLevel() {
        return this.tradeLevel;
    }

    private void increaseTradeLevel() {
        this.tradeLevel++;
        updateTrades();
    }

    protected boolean isTrading() {
        return this.tradingPlayer != null;
    }

    private boolean shouldIncreaseLevel() {
        int level = this.getTradeLevel();
        return VillagerData.canLevelUp(level) && this.tradeXp >= VillagerData.getMaxXpPerLevel(level);
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (offers == null) {
            offers = new MerchantOffers();
            updateTrades();
        }

        return offers;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers offers) {
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        rewardTradeXp(offer);
        if (this.tradingPlayer instanceof ServerPlayer) {
            triggerTrade((ServerPlayer) this.tradingPlayer, this, offer.getResult());
        }
        giveBonusRewards(offer.getResult());
    }

    private static void triggerTrade(ServerPlayer player, Entity entity, ItemStack stack) {
        LootContext ctx = net.minecraft.advancements.critereon.EntityPredicate.createContext(player, entity);
        Method triggerMethod = ObfuscationReflectionHelper.findMethod(SimpleCriterionTrigger.class, "m_66234_", ServerPlayer.class, Predicate.class);
        Predicate<TradeTrigger.TriggerInstance> condition = instance -> instance.matches(ctx, stack);
        try {
            triggerMethod.invoke(CriteriaTriggers.TRADE, player, condition);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Mineria.LOGGER.error("Failed to trigger VillagerTradeTrigger from AbstractDruidEntity#triggerTrade. Some advancements may not work !", e);
        }
    }

    private void giveBonusRewards(ItemStack result) {
        Player player = getTradingPlayer();
        if (player != null) {
            Item heldItem = player.getItemInHand(InteractionHand.OFF_HAND).getItem();
            boolean bonus = false;
            if (heldItem.equals(MineriaItems.MISTLETOE.get()))
                bonus = random.nextFloat() < 0.4F;
            else if (heldItem.equals(MineriaItems.YEW_BERRIES.get()))
                bonus = random.nextFloat() < 0.1F;
            else if (heldItem.equals(Items.APPLE))
                bonus = random.nextFloat() < 0.1F;
            else if (heldItem.equals(MineriaItems.BILLHOOK.get()))
                bonus = random.nextFloat() < 0.5F;

            if (bonus) {
                BehaviorUtils.throwItem(this, result, player.position());
                if (player instanceof ServerPlayer) {
                    MineriaCriteriaTriggers.OBTAINED_TRADE_BONUS_REWARDS.trigger((ServerPlayer) player, this, result);
                }
            }
        }
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {
        if (!this.level.isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!stack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public int getVillagerXp() {
        return this.tradeXp;
    }

    @Override
    public void overrideXp(int xp) {
    }

    @Override
    public boolean showProgressBar() {
        return true;
    }

    @Override
    public boolean canRestock() {
        return true;
    }

    public void restock() {
        for (MerchantOffer offer : this.getOffers()) {
            offer.resetUses();
        }

        this.lastRestockGameTime = this.level.getGameTime();
    }

    public boolean shouldRestock() {
        return needsToRestock() && this.level.getGameTime() > this.lastRestockGameTime + 60000L;
    }

    private boolean needsToRestock() {
        for (MerchantOffer offer : this.getOffers())
            if (offer.needsRestock())
                return true;

        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return MineriaSounds.DRUID_YES.get();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
        if (getRitualPosition().isEmpty()) {
            super.push(x, y, z);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.restrictTo(this.blockPosition(), 16);
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }

    protected SoundEvent getTradeUpdatedSound(boolean trade) {
        return trade ? MineriaSounds.DRUID_YES.get() : MineriaSounds.DRUID_NO.get();
    }

    public void callForRitual(BlockPattern.BlockPatternMatch patternHelper, int index) {
        BlockPos topFrontLeft = patternHelper.getFrontTopLeft();
        setRitualTablePosition(topFrontLeft.offset(3, -1, 3));
        switch (index) {
            case 0:
//                navigation.moveTo(topFrontLeft.getX() + 3.5, topFrontLeft.getY() - 1, topFrontLeft.getZ() + 1.5, 1.2F);
                setRitualPosition(topFrontLeft.offset(3, -1, 1));
                break;
            case 1:
//                navigation.moveTo(topFrontLeft.getX() + 5.5, topFrontLeft.getY() - 1, topFrontLeft.getZ() + 2.5, 1.2F);
                setRitualPosition(topFrontLeft.offset(5, -1, 2));
                break;
            case 2:
//                navigation.moveTo(topFrontLeft.getX() + 4.5, topFrontLeft.getY() - 1, topFrontLeft.getZ() + 5.5, 1.2F);
                setRitualPosition(topFrontLeft.offset(4, -1, 5));
                break;
            case 3:
//                navigation.moveTo(topFrontLeft.getX() + 2.5, topFrontLeft.getY() - 1, topFrontLeft.getZ() + 5.5, 1.2F);
                setRitualPosition(topFrontLeft.offset(2, -1, 5));
                break;
            case 4:
//                navigation.moveTo(topFrontLeft.getX() + 1.5, topFrontLeft.getY() - 1, topFrontLeft.getZ() + 2.5, 1.2F);
                setRitualPosition(topFrontLeft.offset(1, -1, 2));
                break;
        }
//        navigation.moveTo(this.ritualPosition.getX() + 0.5, this.ritualPosition.getY() + 0.5, this.ritualPosition.getZ() + 0.5, 1.2F);
    }

    public Optional<BlockPos> getRitualPosition() {
        return this.entityData.get(RITUAL_POSITION);
    }

    public void setRitualPosition(@Nullable BlockPos pos) {
        this.entityData.set(RITUAL_POSITION, Optional.ofNullable(pos));
    }

    public Optional<BlockPos> getRitualTablePosition() {
        return this.entityData.get(RITUAL_TABLE_POSITION);
    }

    public void setRitualTablePosition(@Nullable BlockPos pos) {
        this.entityData.set(RITUAL_TABLE_POSITION, Optional.ofNullable(pos));
    }

    public boolean isInvoking() {
        return this.entityData.get(INVOKING);
    }

    public void setInvoking(boolean value) {
        this.entityData.set(INVOKING, value);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public enum ArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        NEUTRAL
    }

    public enum SpellType {
        NONE(0, 0D, 0D, 0D),
        SUMMON_LIGHTNING_BOLT(1, 29 / 255D, 42 / 255D, 65 / 255D),
        POISON(2, 11 / 255D, 96 / 255D, 48 / 255D),
        WEAKEN(3, 11 / 255D, 15 / 255D, 38 / 255D);

        private final int id;
        private final double[] spellColor;

        SpellType(int id, double red, double green, double blue) {
            this.id = id;
            this.spellColor = new double[]{red, green, blue};
        }

        public static AbstractDruidEntity.SpellType byId(int id) {
            for (AbstractDruidEntity.SpellType type : values()) {
                if (id == type.id) {
                    return type;
                }
            }

            return NONE;
        }
    }

    public class CastingASpellGoal extends Goal {
        public CastingASpellGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return AbstractDruidEntity.this.getSpellCastingTime() > 0;
        }

        public void start() {
            super.start();
            AbstractDruidEntity.this.navigation.stop();
        }

        public void stop() {
            super.stop();
            AbstractDruidEntity.this.castSpell(AbstractDruidEntity.SpellType.NONE);
        }

        public void tick() {
            if (AbstractDruidEntity.this.getTarget() != null) {
                AbstractDruidEntity.this.getLookControl().setLookAt(AbstractDruidEntity.this.getTarget(), (float) AbstractDruidEntity.this.getMaxHeadYRot(), (float) AbstractDruidEntity.this.getMaxHeadXRot());
            }
        }
    }

    public abstract class UseSpellGoal extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        protected UseSpellGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            if(target == null || !target.isAlive()) {
                return false;
            }
            return !isCastingSpell() && tickCount >= this.nextAttackTickCount;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = getTarget();
            return target != null && target.isAlive() && !(target.isSpectator() || target instanceof Player player && player.isCreative()) && this.attackWarmupDelay > 0;
        }

        @Override
        public void start() {
            this.attackWarmupDelay = this.getCastWarmupTime();
            AbstractDruidEntity.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = AbstractDruidEntity.this.tickCount + this.getCastingInterval();
            SoundEvent prepareSound = this.getSpellPrepareSound();
            if (prepareSound != null) {
                AbstractDruidEntity.this.playSound(prepareSound, 1.0F, 1.0F);
            }

            AbstractDruidEntity.this.castSpell(this.getSpell());
        }

        @Override
        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                AbstractDruidEntity.this.playSound(AbstractDruidEntity.this.getCastingSoundEvent(), 1.0F, 1.0F);
            }
            LivingEntity target = getTarget();
            if(target != null && !canAttack(target)) {
                setTarget(null);
            }
        }

        protected abstract void performSpellCasting();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract AbstractDruidEntity.SpellType getSpell();
    }

    public class DruidRandomWalkingGoal extends RandomStrollGoal {
        public DruidRandomWalkingGoal(double speedModifier) {
            super(AbstractDruidEntity.this, speedModifier);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !getRitualPosition().isPresent();
        }
    }

    public class DruidLookAtGoal extends LookAtPlayerGoal {
        public DruidLookAtGoal(Class<? extends LivingEntity> targetClasses, float lookDistance) {
            super(AbstractDruidEntity.this, targetClasses, lookDistance);
        }

        public DruidLookAtGoal(Class<? extends LivingEntity> targetClasses, float lookDistance, float probability) {
            super(AbstractDruidEntity.this, targetClasses, lookDistance, probability);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !getRitualPosition().isPresent();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !getRitualPosition().isPresent();
        }
    }

    public class PerformRitualGoal extends Goal {
        public PerformRitualGoal() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return getTarget() == null && !isCastingSpell() && getRitualPosition().isPresent() && getRitualTablePosition().isPresent();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void start() {
            goToRitualPosition();
        }

        public void goToRitualPosition() {
            if (getRitualPosition().isPresent()) {
                BlockPos pos = getRitualPosition().get();
                navigation.moveTo(navigation.createPath(pos, 0), 1.2F);
            }
        }

        @Override
        public void tick() {
            if (getRitualTablePosition().isPresent())
                lookControl.setLookAt(Vec3.atCenterOf(getRitualTablePosition().get().above()));
        }
    }
}
