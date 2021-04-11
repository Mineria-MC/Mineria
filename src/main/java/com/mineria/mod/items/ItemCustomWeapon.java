package com.mineria.mod.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemCustomWeapon extends ItemSword
{
    // We're storing the ToolMaterial here for the getIsRepairable# method, it is not worth using ObfuscationReflectionHelper
    private final ToolMaterial material;
    private final float attackDamage;
    private final float attackSpeed;

    public ItemCustomWeapon(float attackDamage, float attackSpeed, ToolMaterial material)
    {
        this(attackDamage, attackSpeed, material, material.getMaxUses());
    }

    public ItemCustomWeapon(float attackDamage, float attackSpeed, ToolMaterial material, int maxDamage)
    {
        super(material);
        this.material = material;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        setCreativeTab(Mineria.MINERIA_TAB);
        setMaxDamage(maxDamage);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem().equals(ItemsInit.getToolRepairItems().get(material)) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public float getAttackDamage()
    {
        return this.attackDamage;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.attackSpeed, 0));
        }

        return multimap;
    }
}
