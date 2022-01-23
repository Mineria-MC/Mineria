package com.mineria.mod.common.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.BlowgunRefillEntity;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BlowgunItem extends ShootableItem
{
    public BlowgunItem()
    {
        super(new Properties().stacksTo(1).durability(16).tab(Mineria.MINERIA_GROUP));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity living)
    {
        if (living instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) living;
            ItemStack ammo = player.getProjectile(stack);

            if (!ammo.isEmpty() || player.abilities.instabuild)
            {
                if (ammo.isEmpty())
                    ammo = new ItemStack(MineriaItems.BLOWGUN_REFILL);

                if(player instanceof ServerPlayerEntity)
                {
                    MineriaCriteriaTriggers.SHOT_BLOWGUN.trigger((ServerPlayerEntity) player, stack, ammo);
                }

                if (!world.isClientSide)
                {
                    stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(living.getUsedItemHand()));
                    BlowgunRefillEntity dart = new BlowgunRefillEntity(world, player, JarItem.getPoisonSourceFromStack(ammo));
                    dart.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);

                    world.addFreshEntity(dart);
                }

                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) * 0.5F);
                if (!player.abilities.instabuild)
                {
                    ammo.shrink(1);
                    if (ammo.isEmpty())
                    {
                        player.inventory.removeItem(ammo);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }

        return stack;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 10;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack blowgun = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.consume(blowgun);
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
