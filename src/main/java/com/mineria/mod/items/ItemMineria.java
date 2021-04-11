package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class ItemMineria extends Item
{
	private final boolean hasEffect;

	public ItemMineria()
	{
		this(new Builder());
	}

	public ItemMineria(Builder properties)
	{
		setCreativeTab(properties.tab);
		setMaxStackSize(properties.maxStackSize);
		setHarvestLevel(properties.harvestTool, properties.harvestLevel);
		setMaxDamage(properties.maxDamage);
		this.hasEffect = properties.hasEffect;
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return hasEffect;
	}

	public static class Builder
	{
		private CreativeTabs tab = Mineria.MINERIA_TAB;
		private int maxStackSize = 64;
		private int harvestLevel = -1;
		private String harvestTool;
		private int maxDamage;
		private boolean hasEffect;

		public Builder setCreativeTab(CreativeTabs tab)
		{
			this.tab = tab;
			return this;
		}

		public Builder setMaxStackSize(int maxStackSize)
		{
			this.maxStackSize = maxStackSize;
			return this;
		}

		public Builder setHarvestLevel(String harvestTool, int harvestLevel)
		{
			this.harvestLevel = harvestLevel;
			this.harvestTool = harvestTool;
			return this;
		}

		public Builder setMaxDamage(int maxDamage)
		{
			this.maxDamage = maxDamage;
			return this;
		}

		public Builder addEffect()
		{
			this.hasEffect = true;
			return this;
		}

		public static <T extends Item> T create(T instance, Builder builder)
		{
			instance.setCreativeTab(builder.tab).setMaxStackSize(builder.maxStackSize).setMaxDamage(builder.maxDamage);
			instance.setHarvestLevel(builder.harvestTool, builder.harvestLevel);
			return instance;
		}
	}

	public static class Axe extends ItemAxe
	{
		public Axe(ToolMaterial material, float damage)
		{
			super(material, damage, -3.0F);
			setCreativeTab(Mineria.MINERIA_TAB);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
		{
			return repair.getItem().equals(ItemsInit.getToolRepairItems().get(this.toolMaterial)) || super.getIsRepairable(toRepair, repair);
		}
	}

	public static class Pickaxe extends ItemPickaxe
	{
		public Pickaxe(ToolMaterial material)
		{
			super(material);
			setCreativeTab(Mineria.MINERIA_TAB);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
		{
			return repair.getItem().equals(ItemsInit.getToolRepairItems().get(this.toolMaterial)) || super.getIsRepairable(toRepair, repair);
		}
	}

	public static class Shovel extends ItemSpade
	{
		public Shovel(ToolMaterial material)
		{
			super(material);
			setCreativeTab(Mineria.MINERIA_TAB);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
		{
			return repair.getItem().equals(ItemsInit.getToolRepairItems().get(this.toolMaterial)) || super.getIsRepairable(toRepair, repair);
		}
	}

	public static class Sword extends ItemSword
	{
		private final ToolMaterial material;

		public Sword(ToolMaterial material)
		{
			super(material);
			this.material = material;
			setCreativeTab(Mineria.MINERIA_TAB);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
		{
			return repair.getItem().equals(ItemsInit.getToolRepairItems().get(this.material)) || super.getIsRepairable(toRepair, repair);
		}
	}

	public static class Hoe extends ItemHoe
	{
		public Hoe(ToolMaterial material)
		{
			super(material);
			setCreativeTab(Mineria.MINERIA_TAB);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
		{
			return repair.getItem().equals(ItemsInit.getToolRepairItems().get(this.toolMaterial)) || super.getIsRepairable(toRepair, repair);
		}
	}
}
