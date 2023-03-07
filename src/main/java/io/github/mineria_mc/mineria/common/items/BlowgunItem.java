package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.client.models.BlowgunItemClientExtension;
import io.github.mineria_mc.mineria.common.entity.BlowgunRefillEntity;
import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlowgunItem extends ProjectileWeaponItem implements IMineriaItem {
    public BlowgunItem() {
        super(new Properties().stacksTo(1).durability(16));
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity living) {
        if (living instanceof Player player) {
            ItemStack ammo = player.getProjectile(stack);

            if (!ammo.isEmpty() || player.getAbilities().instabuild) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(MineriaItems.BLOWGUN_REFILL.get());
                }

                if (player instanceof ServerPlayer) {
                    MineriaCriteriaTriggers.SHOT_BLOWGUN.trigger((ServerPlayer) player, stack, ammo);
                }

                if (!world.isClientSide) {
                    stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(living.getUsedItemHand()));
                    BlowgunRefillEntity dart = new BlowgunRefillEntity(world, player, JarItem.getPoisonSourceFromStack(ammo));
                    dart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);

                    world.addFreshEntity(dart);
                }

                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) * 0.5F);
                if (!player.getAbilities().instabuild) {
                    ammo.shrink(1);
                    if (ammo.isEmpty()) {
                        player.getInventory().removeItem(ammo);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }

        return stack;
    }

    @Override
    public boolean rendersOnHead() {
        return true;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 10;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack blowgun = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(blowgun);
    }

    @Nonnull
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.is(MineriaItems.BLOWGUN_REFILL.get());
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new BlowgunItemClientExtension());
    }
}
