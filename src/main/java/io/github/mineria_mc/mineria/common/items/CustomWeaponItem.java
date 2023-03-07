package io.github.mineria_mc.mineria.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class CustomWeaponItem extends SwordItem implements IMineriaItem {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final int targetInvulnerableTime;

    public CustomWeaponItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        this(tier, attackDamage, attackSpeed, 20, properties);
    }

    public CustomWeaponItem(Tier tier, float attackDamage, float attackSpeed, int targetInvulnerableTime, Properties properties) {
        super(tier, 0, attackSpeed, properties);
        this.attackDamage = attackDamage;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
        this.targetInvulnerableTime = targetInvulnerableTime;
    }

    @Override
    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public int getInvulnerableTime(Entity entity) {
        return targetInvulnerableTime;
    }
}
