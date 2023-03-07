package io.github.mineria_mc.mineria.common.entity;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;

public class BardEntity extends AbstractDruidEntity {
    private static final Lazy<Int2ObjectMap<List<VillagerTrades.ItemListing>>> BARD_TRADES = Lazy.of(() -> Util.make(new Int2ObjectOpenHashMap<>(), map ->
    {
        map.put(1, ImmutableList.of(
                new BasicItemListing(1, new ItemStack(Blocks.JUKEBOX), 16, 1, 0.2F),
                new BasicItemListing(new ItemStack(Items.ITEM_FRAME, 8), new ItemStack(Items.EMERALD), 16, 2, 0.2F)
        ));
        map.put(2, ImmutableList.of(
                new BasicItemListing(new ItemStack(Items.PAINTING, 6), new ItemStack(Items.EMERALD), 16, 5, 0.2F)
        ));
        map.put(3, ImmutableList.of(
                new BasicItemListing(4, new ItemStack(MineriaItems.MYSTERY_DISC.get()), 12, 20, 0.4F),
                new BasicItemListing(6, new ItemStack(MineriaItems.MUSIC_DISC_PIPPIN_THE_HUNCHBACK.get()), 8, 30, 0.5F)
        ));
        map.put(4, ImmutableList.of(
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_13), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_CAT), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_PIGSTEP), 8, 30, 0.5F)
        ));
        map.put(5, ImmutableList.of(
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_BLOCKS), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_CHIRP), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_FAR), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_MALL), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_MELLOHI), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_STAL), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_STRAD), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_WARD), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_11), 8, 30, 0.5F),
                new BasicItemListing(6, new ItemStack(Items.MUSIC_DISC_WAIT), 8, 30, 0.5F)
        ));
    }));

    private PerformRitualGoal performRitualGoal;

    public BardEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AbstractDruidEntity.CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, player -> player.equals(this.getTarget()), 8.0F, 0.6D, 1.0D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        this.goalSelector.addGoal(3, new BardEntity.WeakenSpellGoal());
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
        return BARD_TRADES.get();
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

    class WeakenSpellGoal extends UseSpellGoal {
        @Override
        protected void performSpellCasting() {
            LivingEntity target = BardEntity.this.getTarget();
            Level world = BardEntity.this.getLevel();

            if (target != null && !world.isClientSide()) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 3 * 60 * 20));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 60 * 20));
                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 3 * 60 * 20));
            }
        }

        @Override
        protected int getCastingTime() {
            return 25;
        }

        @Override
        protected int getCastingInterval() {
            return 260;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpell() {
            return SpellType.WEAKEN;
        }
    }
}
