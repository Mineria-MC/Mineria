package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

public class ScalpelItem extends Item {
    public ScalpelItem() {
        super(new Properties().stacksTo(1).durability(26));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack scalpel = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(scalpel);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living) {
        if (living instanceof Player player && living.isCrouching()) {
            world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundSource.PLAYERS, 1.0F, 1.5F);

            if (player instanceof ServerPlayer) {
                MineriaCriteriaTriggers.USED_SCALPEL.trigger((ServerPlayer) player, player, player);
            }

            if (!world.isClientSide()) {
                int chance = world.getRandom().nextInt(1000);

                if (chance < 75) {
                    PoisonMobEffectInstance.getEffects(3, 0, 0, PoisonSource.UNKNOWN).stream().map(MobEffectInstance::getEffect).forEach(player::removeEffect);
                } else if (chance < 150) {
                    PoisonMobEffectInstance.getEffects(2, 0, 0, PoisonSource.UNKNOWN).stream().map(MobEffectInstance::getEffect).forEach(player::removeEffect);
                } else if (chance < 300) {
                    PoisonMobEffectInstance.getEffects(1, 0, 0, PoisonSource.UNKNOWN).stream().map(MobEffectInstance::getEffect).forEach(player::removeEffect);
                }

                int chance2 = world.getRandom().nextInt(100);

                if (chance2 < 20) {
                    player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 90));
                } if (chance2 < 40) {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 60));
                }
            }

            stack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return stack;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Player && target.isAlive() && target.isCrouching()) {
            Player targetPlayer = (Player) target;
            Level world = player.getCommandSenderWorld();
            world.playSound(null, targetPlayer.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundSource.PLAYERS, 1.0F, 1.5F);

            if (player instanceof ServerPlayer) {
                MineriaCriteriaTriggers.USED_SCALPEL.trigger((ServerPlayer) player, player, target);
            }

            if (!world.isClientSide()) {
                int chance = world.getRandom().nextInt(1000);

                if (chance < 75)
                    PoisonMobEffectInstance.getEffects(3, 0, 0, PoisonSource.UNKNOWN).stream().map(MobEffectInstance::getEffect).forEach(targetPlayer::removeEffect);
                else if (chance < 150)
                    PoisonMobEffectInstance.getEffects(2, 0, 0, PoisonSource.UNKNOWN).stream().map(MobEffectInstance::getEffect).forEach(targetPlayer::removeEffect);
                else if (chance < 300)
                    PoisonMobEffectInstance.getEffects(1, 0, 0, PoisonSource.UNKNOWN).stream().map(MobEffectInstance::getEffect).forEach(targetPlayer::removeEffect);

                int chance2 = world.getRandom().nextInt(100);

                if (chance2 < 20)
                    targetPlayer.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 90));
                if (chance2 < 40)
                    targetPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 60));
            }

            stack.hurtAndBreak(1, targetPlayer, player2 -> player2.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 60;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }
}
