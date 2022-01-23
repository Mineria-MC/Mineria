package com.mineria.mod.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class CustomWeaponItem extends SwordItem implements IMineriaItem
{
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final int targetInvulnerableTime;

    public CustomWeaponItem(IItemTier tier, float attackDamage, float attackSpeed, Properties properties)
    {
        this(tier, attackDamage, attackSpeed, 20, properties);
    }

    public CustomWeaponItem(IItemTier tier, float attackDamage, float attackSpeed, int targetInvulnerableTime, Properties properties)
    {
        super(tier, 0, attackSpeed, properties);
        this.attackDamage = attackDamage;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
        this.targetInvulnerableTime = targetInvulnerableTime;
    }

    @Override
    public float getDamage()
    {
        return this.attackDamage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
        return slot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public int getInvulnerableTime(Entity entity)
    {
        return targetInvulnerableTime;
    }
}
