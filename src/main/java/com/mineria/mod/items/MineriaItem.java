package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

public class MineriaItem extends Item
{
	public MineriaItem()
	{
		super(new Item.Properties().group(Mineria.MINERIA_GROUP));
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return this == ItemsInit.LONSDALEITE;
	}

	public enum ItemTier implements IItemTier
	{
		COPPER(187, 3.5F, 1.5F, 2, 4, () -> Ingredient.fromItems(ItemsInit.COPPER_INGOT)),
		LEAD(294, 6.25F, 2.2F, 2, 12, () -> Ingredient.fromItems(ItemsInit.LEAD_INGOT)),
		COMPRESSED_LEAD(576, 6.25F, 3.0F, 2, 12, () -> Ingredient.fromItems(ItemsInit.COMPRESSED_LEAD_INGOT)),
		LONSDALEITE(3460, 20.0F, 7.0F, 3,8, () -> Ingredient.fromItems(ItemsInit.LONSDALEITE)),
		SILVER(621, 7.0F, 2.6F, 2, 16, () -> Ingredient.fromItems(ItemsInit.SILVER_INGOT)),
		TITANE(2048, 12.0F, 4.0F, 3, 12, () -> Ingredient.fromItems(ItemsInit.TITANE_INGOT))
		;

		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int harvestLevel;
		private final int enchantability;
		private final Ingredient repairItem;

		ItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Ingredient> ingredientSupplier)
		{
			this.maxUses = maxUses;
			this.efficiency = efficiency;
			this.attackDamage = attackDamage;
			this.harvestLevel = harvestLevel;
			this.enchantability = enchantability;
			this.repairItem = ingredientSupplier.get();
		}

		@Override
		public int getMaxUses()
		{
			return maxUses;
		}

		@Override
		public float getEfficiency()
		{
			return efficiency;
		}

		@Override
		public float getAttackDamage()
		{
			return attackDamage;
		}

		@Override
		public int getHarvestLevel()
		{
			return harvestLevel;
		}

		@Override
		public int getEnchantability()
		{
			return enchantability;
		}

		@Override
		public Ingredient getRepairMaterial()
		{
			return repairItem;
		}
	}

	public enum ArmorMaterial implements IArmorMaterial
	{
		LONSDALEITE("lonsdaleite", 62, new int[]{6, 9, 11, 7}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Ingredient.fromItems(ItemsInit.LONSDALEITE), 4.0F, 0.3F),
		SILVER("silver", 17, new int[]{2, 6, 6, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, Ingredient.fromItems(ItemsInit.SILVER_INGOT), 0.5F, 0F),
		TITANE("titane", 45, new int[]{4, 7, 9, 4}, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Ingredient.fromItems(ItemsInit.TITANE_INGOT), 1.0F, 0.05F),
		VANADIUM("vanadium", 37, new int[]{0, 0, 0, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, Ingredient.fromItems(ItemsInit.VANADIUM_INGOT), 0.0F, 0F)
		;

		private final String name;
		private final int durability;
		private final int[] damageReductionAmount;
		private final int enchantability;
		private final SoundEvent equipmentSound;
		private final Ingredient repaireMaterial;
		private final float toughness;
		private final float knockbackResistance;

		ArmorMaterial(String name, int durability, int[] damageReductionAmount, int enchantability, SoundEvent equipmentSound, Ingredient repaireMaterial, float toughness, float knockbackResistance)
		{
			this.name = References.MODID + ":" + name;
			this.durability = durability;
			this.damageReductionAmount = damageReductionAmount;
			this.enchantability = enchantability;
			this.equipmentSound = equipmentSound;
			this.repaireMaterial = repaireMaterial;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
		}

		@Override
		public int getDurability(EquipmentSlotType slotIn)
		{
			return durability;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn)
		{
			return this.damageReductionAmount[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability()
		{
			return enchantability;
		}

		@Override
		public SoundEvent getSoundEvent()
		{
			return equipmentSound;
		}

		@Override
		public Ingredient getRepairMaterial()
		{
			return repaireMaterial;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public float getToughness()
		{
			return toughness;
		}

		@Override
		public float getKnockbackResistance()
		{
			return knockbackResistance;
		}
	}
}
