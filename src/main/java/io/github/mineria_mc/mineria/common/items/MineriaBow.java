package io.github.mineria_mc.mineria.common.items;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class MineriaBow extends BowItem {
    private final float damage;
    private final int knockBack;
    private final float velocityMultiplier;

    public MineriaBow(float damage /* default : 0.5F */, Properties properties) {
        this(damage, 1, 0, properties);
    }

    public MineriaBow(float damage, float velocityMultiplier, int knockBack, Properties properties) {
        super(properties);
        this.damage = damage;
        this.velocityMultiplier = velocityMultiplier;
        this.knockBack = knockBack;
    }

    @Override
    public void releaseUsing(ItemStack bowStack, Level world, LivingEntity living, int timeLeft) {
        if (living instanceof Player) {
            Player player = (Player) living;
            boolean infiniteArrows = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowStack) > 0;
            ItemStack ammo = player.getProjectile(bowStack);

            int currentUseDuration = this.getUseDuration(bowStack) - timeLeft;
            currentUseDuration = ForgeEventFactory.onArrowLoose(bowStack, world, player, currentUseDuration, !ammo.isEmpty() || infiniteArrows);
            if (currentUseDuration < 0) return;

            if (!ammo.isEmpty() || infiniteArrows) {
                if (ammo.isEmpty()) {
                    ammo = new ItemStack(Items.ARROW);
                }

                float velocity = getPowerForTime(currentUseDuration) * this.velocityMultiplier;
                if (velocity >= (0.1D * this.velocityMultiplier)) {
                    boolean infiniteAmmo = player.getAbilities().instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem) ammo.getItem()).isInfinite(ammo, bowStack, player));

                    if (!world.isClientSide) {
                        ArrowItem arrow = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                        AbstractArrow arrowEntity = arrow.createArrow(world, ammo, player);
                        // arrowEntity = customArrow(arrowEntity);	Useless

                        arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);

                        if (velocity == this.velocityMultiplier) {
                            arrowEntity.setCritArrow(true);
                        }

                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
                        if (powerLevel > 0) {
                            arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double) powerLevel * 0.5D + this.damage);
                        }

                        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
                        if (punchLevel > 0) {
                            arrowEntity.setKnockback(punchLevel + knockBack);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0) {
                            arrowEntity.setSecondsOnFire(100);
                        }

                        bowStack.hurtAndBreak(1, player, (playerE) -> playerE.broadcastBreakEvent(player.getUsedItemHand()));

                        if (infiniteAmmo || player.getAbilities().instabuild && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW)) {
                            arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        world.addFreshEntity(arrowEntity);
                    }

                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + velocity / velocityMultiplier * 0.5F);

                    if (!infiniteAmmo && !player.getAbilities().instabuild) {
                        ammo.shrink(1);
                        if (ammo.isEmpty()) {
                            player.getInventory().removeItem(ammo);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
}
