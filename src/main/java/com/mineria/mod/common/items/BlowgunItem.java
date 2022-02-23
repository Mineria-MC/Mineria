package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.BlowgunRefillEntity;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

import net.minecraft.world.item.Item.Properties;

public class BlowgunItem extends ProjectileWeaponItem
{
    public BlowgunItem()
    {
        super(new Properties().stacksTo(1).durability(16).tab(Mineria.MINERIA_GROUP));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living)
    {
        if (living instanceof Player player)
        {
            ItemStack ammo = player.getProjectile(stack);

            if (!ammo.isEmpty() || player.getAbilities().instabuild)
            {
                if (ammo.isEmpty())
                    ammo = new ItemStack(MineriaItems.BLOWGUN_REFILL);

                if(player instanceof ServerPlayer)
                {
                    MineriaCriteriaTriggers.SHOT_BLOWGUN.trigger((ServerPlayer) player, stack, ammo);
                }

                if (!world.isClientSide)
                {
                    stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(living.getUsedItemHand()));
                    BlowgunRefillEntity dart = new BlowgunRefillEntity(world, player, JarItem.getPoisonSourceFromStack(ammo));
                    dart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);

                    world.addFreshEntity(dart);
                }

                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) * 0.5F);
                if (!player.getAbilities().instabuild)
                {
                    ammo.shrink(1);
                    if (ammo.isEmpty())
                    {
                        player.getInventory().removeItem(ammo);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }

        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 10;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack blowgun = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(blowgun);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles()
    {
        return stack -> stack.getItem().equals(MineriaItems.BLOWGUN_REFILL);
    }

    @Override
    public int getDefaultProjectileRange()
    {
        return 15;
    }
}
