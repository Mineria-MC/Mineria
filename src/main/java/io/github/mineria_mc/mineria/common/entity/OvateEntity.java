package io.github.mineria_mc.mineria.common.entity;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.util.IngredientItemListing;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
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

public class OvateEntity extends AbstractDruidEntity {
    private static final Lazy<Int2ObjectMap<List<VillagerTrades.ItemListing>>> OVATE_TRADES = Lazy.of(() -> Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(1, ImmutableList.of(
                new IngredientItemListing(Pair.of(Ingredient.of(Tags.Items.MUSHROOMS), 16), new ItemStack(Items.EMERALD), 16, 1, 0.05F),
                new BasicItemListing(2, Util.make(new ItemStack(Items.SUSPICIOUS_STEW), stack -> SuspiciousStewItem.saveMobEffect(stack, MineriaUtils.getRandomElement(ForgeRegistries.MOB_EFFECTS.getValues()), 20 * 5)), 12, 2, 0.2F),
                new BasicItemListing(new ItemStack(MineriaItems.ELDERBERRY.get(), 3), new ItemStack(MineriaItems.BLACK_ELDERBERRY.get()), 16, 1, 0.05F)
        ));
        map.put(2, ImmutableList.of(new IngredientItemListing(Pair.of(Ingredient.of(MineriaItems.Tags.PLANTS), 8), new ItemStack(Items.EMERALD), 16, 5, 0.2F)));
        map.put(3, ImmutableList.of(
                new BasicItemListing(2, new ItemStack(MineriaBlocks.THYME.get()), 12, 10, 0.2F),
                new BasicItemListing(2, new ItemStack(MineriaBlocks.MINT.get()), 12, 10, 0.2F),
                new BasicItemListing(2, new ItemStack(MineriaBlocks.PULMONARY.get()), 12, 10, 0.2F),
                new BasicItemListing(2, new ItemStack(MineriaBlocks.NETTLE.get()), 12, 10, 0.2F),
                new BasicItemListing(2, new ItemStack(MineriaBlocks.PLANTAIN.get()), 12, 10, 0.2F),
                new BasicItemListing(1, new ItemStack(MineriaItems.BLACK_ELDERBERRY.get()), 16, 8, 0.2F)
        ));
        map.put(4, ImmutableList.of(
                new BasicItemListing(1, new ItemStack(MineriaItems.ELDERBERRY.get()), 16, 8, 0.2F),
                new BasicItemListing(3, new ItemStack(MineriaBlocks.MANDRAKE.get()), 12, 20, 0.2F),
                new BasicItemListing(3, new ItemStack(MineriaBlocks.BELLADONNA.get()), 12, 20, 0.2F)
        ));
        map.put(5, ImmutableList.of(
                new BasicItemListing(4, new ItemStack(MineriaItems.CATHOLICON.get()), 12, 20, 0.2F),
                new BasicItemListing(6, new ItemStack(MineriaItems.ANTI_POISON.get()), 12, 20, 0.3F),
                new BasicItemListing(8, new ItemStack(MineriaItems.MIRACLE_ANTI_POISON.get()), 12, 30, 0.4F)
        ));
    }));

    private PerformRitualGoal performRitualGoal;

    public OvateEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AbstractDruidEntity.CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, player -> player.equals(this.getTarget()), 8.0F, 0.6D, 1.0D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        this.goalSelector.addGoal(3, new PoisonSpellGoal());
        this.goalSelector.addGoal(4, performRitualGoal = new PerformRitualGoal());
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractDruidEntity.class)).setAlertOthers(AbstractDruidEntity.class));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt)).setUnseenMemoryTicks(300));
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    protected Int2ObjectMap<List<VillagerTrades.ItemListing>> getTrades() {
        return OVATE_TRADES.get();
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

    class PoisonSpellGoal extends UseSpellGoal {
        @Override
        public boolean canUse() {
            LivingEntity target = OvateEntity.this.getTarget();
            return super.canUse() && !target.hasEffect(MobEffects.POISON);
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = OvateEntity.this.getTarget();

            if (target != null && !target.hasEffect(MobEffects.POISON)) {
                PoisonMobEffectInstance.applyPoisonEffect(target, 1, 3 * 60 * 20, 0, PoisonSource.UNKNOWN);
            }
        }

        @Override
        protected int getCastingTime() {
            return 15;
        }

        @Override
        protected int getCastingInterval() {
            return 80;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpell() {
            return SpellType.POISON;
        }
    }
}
