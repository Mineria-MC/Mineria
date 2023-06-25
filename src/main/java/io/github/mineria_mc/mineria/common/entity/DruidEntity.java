package io.github.mineria_mc.mineria.common.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.mineria_mc.mineria.common.entity.goal.AlertTeamHurtByTargetGoal;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.util.IngredientItemListing;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class DruidEntity extends AbstractDruidEntity {
    private static final Lazy<Int2ObjectMap<List<VillagerTrades.ItemListing>>> DRUID_TRADES = Lazy.of(() -> Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(1, ImmutableList.of(
                new IngredientItemListing(Pair.of(Ingredient.of(Tags.Items.MUSHROOMS), 16), new ItemStack(Items.EMERALD), 16, 1, 0.05F),
                new BasicItemListing(2, Util.make(new ItemStack(Items.SUSPICIOUS_STEW), stack -> SuspiciousStewItem.saveMobEffect(stack, MineriaUtils.getRandomElement(ForgeRegistries.MOB_EFFECTS.getValues()), 20 * 5)), 12, 2, 0.2F),
                new BasicItemListing(new ItemStack(MineriaItems.ELDERBERRY.get(), 3), new ItemStack(MineriaItems.BLACK_ELDERBERRY.get()), 16, 1, 0.05F)
        ));
        map.put(2, ImmutableList.of(
                new BasicItemListing(new ItemStack(MineriaItems.CUP.get(), 16), new ItemStack(Items.EMERALD), 16, 8, 0.2F),
                new BasicItemListing(new ItemStack(MineriaBlocks.SPRUCE_YEW_SAPLING.get(), 6), new ItemStack(Items.EMERALD), 12, 4, 0.2F)
        ));
        map.put(3, ImmutableList.of(
                new BasicItemListing(3, new ItemStack(MineriaItems.THYME_TEA.get()), 12, 10, 0.2F),
                new BasicItemListing(3, new ItemStack(MineriaItems.MINT_TEA.get()), 12, 10, 0.2F),
                new BasicItemListing(3, new ItemStack(MineriaItems.PULMONARY_TEA.get()), 12, 10, 0.2F),
                new BasicItemListing(3, new ItemStack(MineriaItems.NETTLE_TEA.get()), 12, 10, 0.2F),
                new BasicItemListing(3, new ItemStack(MineriaItems.PLANTAIN_TEA.get()), 12, 10, 0.2F),
                new BasicItemListing(2, new ItemStack(MineriaItems.BLACK_ELDERBERRY_TEA.get()), 16, 8, 0.2F)
        ));
        map.put(4, ImmutableList.of(
                new BasicItemListing(2, new ItemStack(MineriaItems.ELDERBERRY_TEA.get()), 16, 8, 0.2F),
                new BasicItemListing(4, new ItemStack(MineriaItems.MANDRAKE_TEA.get()), 12, 20, 0.2F),
                new BasicItemListing(4, new ItemStack(MineriaItems.BELLADONNA_TEA.get()), 12, 20, 0.2F)
        ));
        map.put(5, ImmutableList.of(
                new BasicItemListing(4, new ItemStack(MineriaItems.CATHOLICON.get()), 12, 20, 0.25F),
                new BasicItemListing(6, new ItemStack(MineriaItems.ANTI_POISON.get()), 12, 20, 0.3F),
                new BasicItemListing(8, new ItemStack(MineriaItems.MIRACLE_ANTI_POISON.get()), 12, 30, 0.4F)
        ));
    }));

    private PerformRitualGoal performRitualGoal;

    public DruidEntity(EntityType<? extends AbstractDruidEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AbstractDruidEntity.CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, player -> player.equals(this.getTarget()), 8.0F, 0.6D, 1.0D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        this.goalSelector.addGoal(3, new DruidEntity.StrikeSpellGoal());
        this.goalSelector.addGoal(4, performRitualGoal = new PerformRitualGoal());
        this.goalSelector.addGoal(5, new DruidRandomWalkingGoal(0.6D));
        this.goalSelector.addGoal(6, new DruidLookAtGoal(Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new DruidLookAtGoal(Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class).setAlertEntities(AbstractDruidEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    protected Int2ObjectMap<List<VillagerTrades.ItemListing>> getTrades() {
        return DRUID_TRADES.get();
    }

    @Override
    public void callForRitual(BlockPattern.BlockPatternMatch patternHelper, int index) {
        super.callForRitual(patternHelper, index);
        if (performRitualGoal != null)
            performRitualGoal.goToRitualPosition();
    }

    @Override
    public boolean isClientSide() {
        return level.isClientSide();
    }

    class StrikeSpellGoal extends UseSpellGoal {
        @Override
        public void tick() {
            Level world = DruidEntity.this.getLevel();
            if (!world.isClientSide()) {
                ((ServerLevel) world).setWeatherParameters(0, 600, true, true);
            }

            super.tick();
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = DruidEntity.this.getTarget();
            if (!level.isClientSide() && target != null) {
                ServerLevel world = (ServerLevel) level;
                MineriaLightningBoltEntity.create(world, BlockPos.containing(target.position()), MobSpawnType.EVENT, false, 0, target::equals).ifPresent(world::addFreshEntityWithPassengers);
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 90 + random.nextInt(20);
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpell() {
            return SpellType.SUMMON_LIGHTNING_BOLT;
        }
    }
}
