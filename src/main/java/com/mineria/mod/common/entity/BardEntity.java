package com.mineria.mod.common.entity;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.init.MineriaItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.common.BasicTrade;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BardEntity extends AbstractDruidEntity
{
    private static final Supplier<Int2ObjectMap<List<VillagerTrades.ITrade>>> BARD_TRADES = () -> Util.make(new Int2ObjectOpenHashMap<>(), map ->
    {
        map.put(1, ImmutableList.of(
                new BasicTrade(1, new ItemStack(Blocks.JUKEBOX), 16, 1, 0.2F),
                new BasicTrade(new ItemStack(Items.ITEM_FRAME, 8), new ItemStack(Items.EMERALD), 16, 2, 0.2F)
        ));
        map.put(2, ImmutableList.of(
                new BasicTrade(new ItemStack(Items.PAINTING, 6), new ItemStack(Items.EMERALD), 16, 5, 0.2F)
        ));
        map.put(3, ImmutableList.of(
                new BasicTrade(4, new ItemStack(MineriaItems.MYSTERY_DISC), 12, 20, 0.4F),
                new BasicTrade(6, new ItemStack(MineriaItems.MUSIC_DISC_PIPPIN_THE_HUNCHBACK), 8, 30, 0.5F)
        ));
        map.put(4, ImmutableList.of(
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_13), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_CAT), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_PIGSTEP), 8, 30, 0.5F)
        ));
        map.put(5, ImmutableList.of(
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_BLOCKS), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_CHIRP), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_FAR), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_MALL), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_MELLOHI), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_STAL), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_STRAD), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_WARD), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_11), 8, 30, 0.5F),
                new BasicTrade(6, new ItemStack(Items.MUSIC_DISC_WAIT), 8, 30, 0.5F)
        ));
    });

    private PerformRitualGoal performRitualGoal;

    public BardEntity(EntityType<? extends MonsterEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AbstractDruidEntity.CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, player -> player.equals(this.getTarget()), 8.0F, 0.6D, 1.0D, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test));
        this.goalSelector.addGoal(3, new BardEntity.WeakenSpellGoal());
        this.goalSelector.addGoal(4, performRitualGoal = new PerformRitualGoal());
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractDruidEntity.class)).setAlertOthers(AbstractDruidEntity.class));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::isAngryAt)).setUnseenMemoryTicks(300));
    }

    @Override
    protected SoundEvent getCastingSoundEvent()
    {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    protected Int2ObjectMap<List<VillagerTrades.ITrade>> getTrades()
    {
        return BARD_TRADES.get();
    }

    @Override
    public void callForRitual(BlockPattern.PatternHelper patternHelper, int index)
    {
        super.callForRitual(patternHelper, index);
        if(performRitualGoal != null)
            performRitualGoal.goToRitualPosition();
    }

    class WeakenSpellGoal extends UseSpellGoal
    {
        @Override
        protected void performSpellCasting()
        {
            LivingEntity target = BardEntity.this.getTarget();
            World world = BardEntity.this.getLevel();

            if (!world.isClientSide())
            {
                target.addEffect(new EffectInstance(Effects.WEAKNESS, 3 * 60 * 20));
                target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 3 * 60 * 20));
                target.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 3 * 60 * 20));
            }
        }

        @Override
        protected int getCastingTime()
        {
            return 25;
        }

        @Override
        protected int getCastingInterval()
        {
            return 260;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpell()
        {
            return SpellType.WEAKEN;
        }
    }
}
