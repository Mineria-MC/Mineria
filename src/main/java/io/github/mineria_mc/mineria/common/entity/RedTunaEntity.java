package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

// TODO: Spawn
public class RedTunaEntity extends AbstractFish {
    public RedTunaEntity(EntityType<? extends RedTunaEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(MineriaItems.RED_TUNA_BUCKET.get());
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }
}
