package io.github.mineria_mc.mineria.common.item;

import java.util.function.Supplier;

import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class MineriaItem extends Item {

    public MineriaItem() {
        super(new Item.Properties());
    }

    public enum ItemTier implements Tier {
        COPPER(187, 3.5f, 1.5f, 2, 4, () -> Ingredient.of(Items.COPPER_INGOT)),
        LEAD(294, 6.25f, 2.2f, 2, 12, () -> Ingredient.of(MineriaItemRegistry.LEAD_INGOT)),
        COMPRESSED_LEAD(576, 6.25f, 3.0f, 2, 12, () -> Ingredient.of(MineriaItemRegistry.COMPRESSED_LEAD_INGOT)),
        SILVER(621, 7.0f, 2.6f, 2, 16, () -> Ingredient.of(MineriaItemRegistry.SILVER_INGOT)),
        TITANE(2048, 12.0f, 4.0f, 3, 12, () -> Ingredient.of(MineriaItemRegistry.TITANE_INGOT)),
        LONSDALEITE(3460, 20.0f, 7.0f, 3, 8, () -> Ingredient.of(MineriaItemRegistry.LONSDALEITE));

        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int harvestLevel;
        private final int enchantability;
        private final Supplier<Ingredient> repairItem;

        ItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Ingredient> ingredientSupplier) {
            this.maxUses = maxUses;
            this.efficiency = efficiency;
            this.attackDamage = attackDamage;
            this.harvestLevel = harvestLevel;
            this.enchantability = enchantability;
            this.repairItem = ingredientSupplier;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamage;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Override
        public int getLevel() {
            return harvestLevel;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repairItem.get();
        }

        @Override
        public float getSpeed() {
            return efficiency;
        }

        @Override
        public int getUses() {
            return maxUses;
        }
    }
}
