package io.github.mineria_mc.mineria.common.item;

import java.util.function.Supplier;

import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import io.github.mineria_mc.mineria.util.Constants;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
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

    public enum ArmorMaterial implements net.minecraft.world.item.ArmorMaterial {
        LONSDALEITE("lonsdaleite", 62, new int[]{6, 9, 11, 7}, 8, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MineriaItemRegistry.LONSDALEITE), 4.0F, 0.3F),
        SILVER("silver", 17, new int[]{2, 6, 6, 3}, 16, SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MineriaItemRegistry.SILVER_INGOT), 0.5F, 0F),
        TITANE("titane", 45, new int[]{4, 7, 9, 4}, 12, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MineriaItemRegistry.TITANE_INGOT), 1.0F, 0.05F);

        private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
        private final String name;
        private final int durability;
        private final int[] damageReductionAmount;
        private final int enchantability;
        private final SoundEvent equipmentSound;
        private final Supplier<Ingredient> repairMaterial;
        private final float toughness;
        private final float knockbackResistance;

        ArmorMaterial(String name, int durability, int[] damageReductionAmount, int enchantability, SoundEvent equipmentSound, Supplier<Ingredient> repairMaterial, float toughness, float knockbackResistance) {
            this.name = Constants.MODID + ":" + name;
            this.durability = durability;
            this.damageReductionAmount = damageReductionAmount;
            this.enchantability = enchantability;
            this.equipmentSound = equipmentSound;
            this.repairMaterial = repairMaterial;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return HEALTH_PER_SLOT[type.getSlot().getIndex()] * durability;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return this.damageReductionAmount[type.getSlot().getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Override
        public SoundEvent getEquipSound() {
            return equipmentSound;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repairMaterial.get();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public float getToughness() {
            return toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return knockbackResistance;
        }
    }
}
