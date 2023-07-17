package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.entity.goal.AlertTeamHurtByTargetGoal;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DirtGolemEntity extends ElementaryGolemEntity {
    public DirtGolemEntity(EntityType<? extends DirtGolemEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.6D, 240, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.tickCount % 600 == 0) {
            int radius = 4;
            BlockPos pos = this.blockPosition();
            boolean fertilizedDirt = false;

            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))) {
                BlockState state = this.level().getBlockState(blockPos);

                if (state.getBlock() instanceof BonemealableBlock growable) {
                    if (growable.isValidBonemealTarget(this.level(), blockPos, state, this.level().isClientSide)) {
                        if (this.level() instanceof ServerLevel && growable.isBonemealSuccess(this.level(), this.level().random, blockPos, state)) {
                            growable.performBonemeal((ServerLevel) this.level(), this.level().random, blockPos, state);
                        }

                        fertilizedDirt = true;
                    }
                }
            }

            if (fertilizedDirt) {
                this.level().playSound(null, pos, SoundEvents.COMPOSTER_READY, SoundSource.HOSTILE, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public TextColor getCharacteristicColor() {
        return TextColor.parseColor("#4F7E2E");
    }

    @Override
    public float getBlastProtectionValue() {
        return 8;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return MineriaSounds.DIRT_GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MineriaSounds.DIRT_GOLEM_DEATH.get();
    }

    @Override
    public float getMinAttackDamage() {
        return 11;
    }

    @Override
    public float getMaxAttackDamage() {
        return 21;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 16.0D).add(Attributes.ATTACK_KNOCKBACK, 2);
    }
}
