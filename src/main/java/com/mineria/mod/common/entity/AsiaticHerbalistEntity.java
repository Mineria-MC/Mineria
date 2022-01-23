package com.mineria.mod.common.entity;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.util.IngredientTrade;
import com.mineria.mod.util.MineriaUtils;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AsiaticHerbalistEntity extends AbstractVillagerEntity
{
    public static final Supplier<Int2ObjectMap<List<VillagerTrades.ITrade>>> TRADES = () -> Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.put(1, ImmutableList.of(
                new IngredientTrade(Pair.of(Ingredient.of(Tags.Items.MUSHROOMS), 16), new ItemStack(Items.EMERALD), 16, 1, 0.05F),
                new BasicTrade(2, Util.make(new ItemStack(Items.SUSPICIOUS_STEW), stack -> SuspiciousStewItem.saveMobEffect(stack, MineriaUtils.getRandomElement(ForgeRegistries.POTIONS.getValues()), 20 * 5)), 12, 2, 0.2F),
                new BasicTrade(new ItemStack(MineriaItems.ELDERBERRY, 3), new ItemStack(MineriaItems.BLACK_ELDERBERRY), 16, 1, 0.05F),
                new BasicTrade(4, new ItemStack(MineriaItems.ANTI_POISON), 12, 2, 0.2F)
        ));
        map.put(2, ImmutableList.of(
                new BasicTrade(2, new ItemStack(MineriaItems.GOJI), 16, 5, 0.2F),
                new BasicTrade(2, new ItemStack(MineriaItems.SYRUP), 16, 5, 0.2F)
        ));
        map.put(3, ImmutableList.of(
                new BasicTrade(3, new ItemStack(MineriaItems.PULSATILLA_CHINENSIS_ROOT), 12, 10, 0.2F),
                new BasicTrade(3, new ItemStack(MineriaItems.SAUSSUREA_COSTUS_ROOT), 12, 10, 0.2F)
        ));
        map.put(4, ImmutableList.of(
                new BasicTrade(6, new ItemStack(MineriaItems.MIRACLE_ANTI_POISON), 12, 30, 0.35F)
        ));
        map.put(5, ImmutableList.of(
                new BasicTrade(4, MineriaItems.KUNAI.getDefaultInstance(), 12, 30, 0.2F)
//                new BasicTrade(6, /Poisonous Kunai Instance/, 12, 30, 0.4F) TODOLTR will be implemented later
        ));
    });

    private int updateMerchantTimer;
    private boolean increaseProfessionLevelOnUpdate;
    private int tradeXp;
    private int tradeLevel = 1;

    public AsiaticHerbalistEntity(EntityType<? extends AsiaticHerbalistEntity> type, World world)
    {
        super(type, world);

    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZombieEntity.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, EvokerEntity.class, 12.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, VindicatorEntity.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, VexEntity.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PillagerEntity.class, 15.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IllusionerEntity.class, 12.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ZoglinEntity.class, 10.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
        this.goalSelector.addGoal(1, new LookAtCustomerGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.35D));
        this.goalSelector.addGoal(9, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("TradeXp", this.tradeXp);
        nbt.putInt("TradeLevel", this.tradeLevel);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        if(nbt.contains("TradeXp", 3))
            this.tradeXp = nbt.getInt("TradeXp");
        if(nbt.contains("TradeLevel", 3))
            this.tradeLevel = nbt.getInt("TradeLevel");
    }

    @Override
    protected void customServerAiStep()
    {
        if (!this.isTrading() && this.updateMerchantTimer > 0)
        {
            --this.updateMerchantTimer;
            if (this.updateMerchantTimer <= 0) {
                if (this.increaseProfessionLevelOnUpdate) {
                    this.increaseTradeLevel();
                    this.increaseProfessionLevelOnUpdate = false;
                }

                this.addEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            }
        }

        super.customServerAiStep();
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer)
    {
        int xp = 3 + this.random.nextInt(4);
        this.tradeXp += offer.getXp();
        if (this.shouldIncreaseLevel())
        {
            this.updateMerchantTimer = 40;
            this.increaseProfessionLevelOnUpdate = true;
            xp += 5;
        }

        if (offer.shouldRewardExp())
        {
            this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), xp));
        }
    }

    @Override
    protected void updateTrades()
    {
        List<VillagerTrades.ITrade> trades = TRADES.get().get(this.tradeLevel);
        if(trades != null)
        {
            this.addOffersFromItemListings(this.getOffers(), trades.toArray(new VillagerTrades.ITrade[0]), 4);
        }
    }

    protected int getTradeLevel()
    {
        return this.tradeLevel;
    }

    private void increaseTradeLevel()
    {
        this.tradeLevel++;
        updateTrades();
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.isTrading() && !this.isBaby())
        {
            if (hand == Hand.MAIN_HAND)
                player.awardStat(Stats.TALKED_TO_VILLAGER);

            if (!this.getOffers().isEmpty())
            {
                if (!this.level.isClientSide)
                {
                    this.setTradingPlayer(player);
                    this.openTradingScreen(player, this.getDisplayName(), tradeLevel);
                }
            }

            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else
            return super.mobInteract(player, hand);
    }

    private boolean shouldIncreaseLevel()
    {
        int level = this.getTradeLevel();
        return VillagerData.canLevelUp(level) && this.tradeXp >= VillagerData.getMaxXpPerLevel(level);
    }

    @Override
    public boolean removeWhenFarAway(double distance)
    {
        return false;
    }

    @Override
    public int getVillagerXp()
    {
        return tradeXp;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity entity)
    {
        return null;
    }
}
