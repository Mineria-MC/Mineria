package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class ScalpelItem extends Item {
    private final List<MobEffect> class1Effects = Arrays.stream(PoisonMobEffectInstance.getPoisonEffects(1, 0, 0, PoisonSource.UNKNOWN)).map(MobEffectInstance::getEffect).toList();
    private final List<MobEffect> class2Effects = Arrays.stream(PoisonMobEffectInstance.getPoisonEffects(2, 0, 0, PoisonSource.UNKNOWN)).map(MobEffectInstance::getEffect).toList();
    private final List<MobEffect> class3Effects = Arrays.stream(PoisonMobEffectInstance.getPoisonEffects(3, 0, 0, PoisonSource.UNKNOWN)).map(MobEffectInstance::getEffect).toList();

    public ScalpelItem() {
        super(new Properties().stacksTo(1).durability(26));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(player.isCrouching()) {
            ItemStack scalpel = player.getItemInHand(hand);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(scalpel);
        }
        if(player.isUsingItem()) {
            player.stopUsingItem();
        }
        return super.use(world, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        super.onUseTick(level, living, stack, count);
        if(living.isUsingItem() && !living.isCrouching()) {
            living.stopUsingItem();
        }
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
                    class3Effects.forEach(player::removeEffect);
                } else if (chance < 150) {
                    class2Effects.forEach(player::removeEffect);
                } else if (chance < 300) {
                    class1Effects.forEach(player::removeEffect);
                }

                int chance2 = world.getRandom().nextInt(100);

                if (chance2 < 20) {
                    player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 90));
                }
                if (chance2 < 40) {
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
                    class3Effects.forEach(targetPlayer::removeEffect);
                else if (chance < 150)
                    class2Effects.forEach(targetPlayer::removeEffect);
                else if (chance < 300)
                    class1Effects.forEach(targetPlayer::removeEffect);

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
