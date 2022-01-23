package com.mineria.mod.common.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class MineriaBow extends BowItem
{
	private final float damage;
	private final int knockBack;
	private final float velocityMultiplier;

	public MineriaBow(float damage /* default : 0.5F */, Properties properties)
	{
		this(damage, 1, 0, properties);
	}

	public MineriaBow(float damage, float velocityMultiplier, int knockBack, Properties properties)
	{
		super(properties);
		this.damage = damage;
		this.velocityMultiplier = velocityMultiplier;
		this.knockBack = knockBack;
	}
	
	@Override
	public void releaseUsing(ItemStack bowStack, World world, LivingEntity living, int timeLeft)
	{
		if (living instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)living;
			boolean infiniteArrows = player.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowStack) > 0;
			ItemStack ammo = player.getProjectile(bowStack);

			int currentUseDuration = this.getUseDuration(bowStack) - timeLeft;
			currentUseDuration = ForgeEventFactory.onArrowLoose(bowStack, world, player, currentUseDuration, !ammo.isEmpty() || infiniteArrows);
			if (currentUseDuration < 0) return;

			if (!ammo.isEmpty() || infiniteArrows)
			{
				if (ammo.isEmpty())
				{
					ammo = new ItemStack(Items.ARROW);
				}

				float velocity = getPowerForTime(currentUseDuration) * this.velocityMultiplier;
				if (velocity >= (0.1D * this.velocityMultiplier))
				{
					boolean infiniteAmmo = player.abilities.instabuild || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, bowStack, player));

					if (!world.isClientSide)
					{
						ArrowItem arrow = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
						AbstractArrowEntity arrowEntity = arrow.createArrow(world, ammo, player);
						// arrowEntity = customArrow(arrowEntity);	Useless

						arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, velocity * 3.0F, 1.0F);

						if (velocity == this.velocityMultiplier) {
							arrowEntity.setCritArrow(true);
						}

						int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
						if (powerLevel > 0)
						{
							arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double)powerLevel * 0.5D + this.damage);
						}

						int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
						if (punchLevel > 0)
						{
							arrowEntity.setKnockback(punchLevel + knockBack);
						}

						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0)
						{
							arrowEntity.setSecondsOnFire(100);
						}

						bowStack.hurtAndBreak(1, player, (playerE) -> playerE.broadcastBreakEvent(player.getUsedItemHand()));

						if (infiniteAmmo || player.abilities.instabuild && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW))
						{
							arrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
						}

						world.addFreshEntity(arrowEntity);
					}

					world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + velocity / velocityMultiplier * 0.5F);

					if (!infiniteAmmo && !player.abilities.instabuild)
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
		}
	}
}
