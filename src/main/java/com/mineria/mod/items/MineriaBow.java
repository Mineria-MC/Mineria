package com.mineria.mod.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
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
		ItemModelsProperties.registerProperty(this, new ResourceLocation("pull"), (stack, world, living) -> {
			if (living == null)
				return 0.0F;
			else
				return living.getActiveItemStack() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getItemInUseCount()) / 20.0F;
		});
		ItemModelsProperties.registerProperty(this, new ResourceLocation("pulling"),
				(stack, world, living) -> living != null && living.isHandActive() && living.getActiveItemStack() == stack ? 1.0F : 0.0F);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack bowStack, World world, LivingEntity living, int timeLeft)
	{
		if (living instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)living;
			boolean infiniteArrows = player.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowStack) > 0;
			ItemStack ammo = player.findAmmo(bowStack);

			int currentUseDuration = this.getUseDuration(bowStack) - timeLeft;
			currentUseDuration = ForgeEventFactory.onArrowLoose(bowStack, world, player, currentUseDuration, !ammo.isEmpty() || infiniteArrows);
			if (currentUseDuration < 0) return;

			if (!ammo.isEmpty() || infiniteArrows)
			{
				if (ammo.isEmpty())
				{
					ammo = new ItemStack(Items.ARROW);
				}

				float velocity = getArrowVelocity(currentUseDuration) * this.velocityMultiplier;
				if (velocity >= (0.1D * this.velocityMultiplier))
				{
					boolean infiniteAmmo = player.abilities.isCreativeMode || (ammo.getItem() instanceof ArrowItem && ((ArrowItem)ammo.getItem()).isInfinite(ammo, bowStack, player));

					if (!world.isRemote)
					{
						ArrowItem arrow = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
						AbstractArrowEntity arrowEntity = arrow.createArrow(world, ammo, player);
						// arrowEntity = customArrow(arrowEntity);	Useless

						//shootArrow
						arrowEntity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);

						if (velocity == this.velocityMultiplier) {
							arrowEntity.setIsCritical(true);
						}

						int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bowStack);
						if (powerLevel > 0)
						{
							arrowEntity.setDamage(arrowEntity.getDamage() + (double)powerLevel * 0.5D + this.damage);
						}

						int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowStack);
						if (punchLevel > 0)
						{
							arrowEntity.setKnockbackStrength(punchLevel + knockBack);
						}

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowStack) > 0)
						{
							arrowEntity.setFire(100);
						}

						bowStack.damageItem(1, player, (playerE) -> playerE.sendBreakAnimation(player.getActiveHand()));

						if (infiniteAmmo || player.abilities.isCreativeMode && (ammo.getItem() == Items.SPECTRAL_ARROW || ammo.getItem() == Items.TIPPED_ARROW))
						{
							arrowEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
						}

						world.addEntity(arrowEntity);
					}

					world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + velocity / velocityMultiplier * 0.5F);

					if (!infiniteAmmo && !player.abilities.isCreativeMode)
					{
						ammo.shrink(1);
						if (ammo.isEmpty())
						{
							player.inventory.deleteStack(ammo);
						}
					}

					player.addStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}
}
