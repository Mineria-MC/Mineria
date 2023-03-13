package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class MineriaItem extends Item {
    public MineriaItem() {
        super(new Item.Properties());
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return this == MineriaItems.LONSDALEITE.get();
    }

    public static ItemStack withCustomModelData(RegistryObject<? extends ItemLike> obj, int data) {
        return obj.map(ItemStack::new).map(stack -> withCustomModelData(stack, data)).orElse(ItemStack.EMPTY);
    }

    public static ItemStack withCustomModelData(ItemLike item, int data) {
        return withCustomModelData(new ItemStack(item), data);
    }

    public static ItemStack withCustomModelData(ItemStack stack, int data) {
        stack.getOrCreateTag().putInt("CustomModelData", data);
        return stack;
    }

    public enum ItemTier implements Tier {
        COPPER(187, 3.5F, 1.5F, 2, 4, () -> Ingredient.of(Items.COPPER_INGOT)),
        LEAD(294, 6.25F, 2.2F, 2, 12, () -> Ingredient.of(MineriaItems.LEAD_INGOT.get())),
        COMPRESSED_LEAD(576, 6.25F, 3.0F, 2, 12, () -> Ingredient.of(MineriaItems.COMPRESSED_LEAD_INGOT.get())),
        LONSDALEITE(3460, 20.0F, 7.0F, 3, 8, () -> Ingredient.of(MineriaItems.LONSDALEITE.get())),
        SILVER(621, 7.0F, 2.6F, 2, 16, () -> Ingredient.of(MineriaItems.SILVER_INGOT.get())),
        TITANE(2048, 12.0F, 4.0F, 3, 12, () -> Ingredient.of(MineriaItems.TITANE_INGOT.get()));

        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int harvestLevel;
        private final int enchantability;
        private final Lazy<Ingredient> repairItem;

        ItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Ingredient> ingredientSupplier) {
            this.maxUses = maxUses;
            this.efficiency = efficiency;
            this.attackDamage = attackDamage;
            this.harvestLevel = harvestLevel;
            this.enchantability = enchantability;
            this.repairItem = Lazy.of(ingredientSupplier);
        }

        @Override
        public int getUses() {
            return maxUses;
        }

        @Override
        public float getSpeed() {
            return efficiency;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamage;
        }

        @Override
        public int getLevel() {
            return harvestLevel;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Nonnull
        @Override
        public Ingredient getRepairIngredient() {
            return repairItem.get();
        }
    }

    public enum ArmorMaterial implements net.minecraft.world.item.ArmorMaterial {
        LONSDALEITE("lonsdaleite", 62, new int[]{6, 9, 11, 7}, 8, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MineriaItems.LONSDALEITE.get()), 4.0F, 0.3F),
        SILVER("silver", 17, new int[]{2, 6, 6, 3}, 16, SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(MineriaItems.SILVER_INGOT.get()), 0.5F, 0F),
        TITANE("titane", 45, new int[]{4, 7, 9, 4}, 12, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MineriaItems.TITANE_INGOT.get()), 1.0F, 0.05F),
        VANADIUM("vanadium", 37, new int[]{0, 0, 0, 4}, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(MineriaItems.VANADIUM_INGOT.get()), 0.0F, 0F);

        private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
        private final String name;
        private final int durability;
        private final int[] damageReductionAmount;
        private final int enchantability;
        private final SoundEvent equipmentSound;
        private final Lazy<Ingredient> repairMaterial;
        private final float toughness;
        private final float knockbackResistance;

        ArmorMaterial(String name, int durability, int[] damageReductionAmount, int enchantability, SoundEvent equipmentSound, Supplier<Ingredient> repairMaterial, float toughness, float knockbackResistance) {
            this.name = Mineria.MODID + ":" + name;
            this.durability = durability;
            this.damageReductionAmount = damageReductionAmount;
            this.enchantability = enchantability;
            this.equipmentSound = equipmentSound;
            this.repairMaterial = Lazy.of(repairMaterial);
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
        }

        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            return HEALTH_PER_SLOT[slotIn.getIndex()] * durability;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
            return this.damageReductionAmount[slotIn.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Nonnull
        @Override
        public SoundEvent getEquipSound() {
            return equipmentSound;
        }

        @Nonnull
        @Override
        public Ingredient getRepairIngredient() {
            return repairMaterial.get();
        }

        @Nonnull
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
