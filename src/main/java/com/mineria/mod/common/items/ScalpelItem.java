package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ScalpelItem extends Item
{
    public ScalpelItem()
    {
        super(new Properties().stacksTo(1).durability(26).tab(Mineria.MINERIA_GROUP));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack scalpel = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.consume(scalpel);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity living)
    {
        if(living instanceof PlayerEntity && living.isCrouching())
        {
            PlayerEntity player = (PlayerEntity) living;
            world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.PLAYERS, 1.0F, 1.5F);

            if(player instanceof ServerPlayerEntity)
            {
                MineriaCriteriaTriggers.USED_SCALPEL.trigger((ServerPlayerEntity) player, player, player);
            }

            if(!world.isClientSide())
            {
                int chance = random.nextInt(1000);

                if(chance < 75)
                    PoisonEffectInstance.getEffects(3, 0, 0, PoisonSource.UNKNOWN).stream().map(EffectInstance::getEffect).forEach(player::removeEffect);
                else if(chance < 150)
                    PoisonEffectInstance.getEffects(2, 0, 0, PoisonSource.UNKNOWN).stream().map(EffectInstance::getEffect).forEach(player::removeEffect);
                else if(chance < 300)
                    PoisonEffectInstance.getEffects(1, 0, 0, PoisonSource.UNKNOWN).stream().map(EffectInstance::getEffect).forEach(player::removeEffect);

                int chance2 = random.nextInt(100);

                if(chance2 < 20)
                    player.addEffect(new EffectInstance(Effects.HUNGER, 20 * 90));
                if(chance2 < 40)
                    player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20 * 60));
            }

            stack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }
        return stack;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        if(target instanceof PlayerEntity && target.isAlive() && target.isCrouching())
        {
            PlayerEntity targetPlayer = (PlayerEntity) target;
            World world = player.getCommandSenderWorld();
            world.playSound(null, targetPlayer.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.PLAYERS, 1.0F, 1.5F);

            if(player instanceof ServerPlayerEntity)
            {
                MineriaCriteriaTriggers.USED_SCALPEL.trigger((ServerPlayerEntity) player, player, target);
            }

            if(!world.isClientSide())
            {
                int chance = random.nextInt(1000);

                if(chance < 75)
                    PoisonEffectInstance.getEffects(3, 0, 0, PoisonSource.UNKNOWN).stream().map(EffectInstance::getEffect).forEach(targetPlayer::removeEffect);
                else if(chance < 150)
                    PoisonEffectInstance.getEffects(2, 0, 0, PoisonSource.UNKNOWN).stream().map(EffectInstance::getEffect).forEach(targetPlayer::removeEffect);
                else if(chance < 300)
                    PoisonEffectInstance.getEffects(1, 0, 0, PoisonSource.UNKNOWN).stream().map(EffectInstance::getEffect).forEach(targetPlayer::removeEffect);

                int chance2 = random.nextInt(100);

                if(chance2 < 20)
                    targetPlayer.addEffect(new EffectInstance(Effects.HUNGER, 20 * 90));
                if(chance2 < 40)
                    targetPlayer.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20 * 60));
            }

            stack.hurtAndBreak(1, targetPlayer, player2 -> player2.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            return ActionResultType.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 60;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.CROSSBOW;
    }
}
