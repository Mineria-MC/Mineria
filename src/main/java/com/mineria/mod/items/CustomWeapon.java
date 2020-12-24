package com.mineria.mod.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mineria.mod.Mineria;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class CustomWeapon extends ItemSword
{
    private final float attackDamage;
    private final float attackSpeed;
    private final Item.ToolMaterial material;

    public CustomWeapon(String name, float attackDamage, float attackSpeed, ToolMaterial material)
    {
        super(material);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.material = material;
        setCreativeTab(Mineria.mineriaTab);
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public CustomWeapon(String name, float attackDamage, float attackSpeed, ToolMaterial material, int maxDamage)
    {
        super(material);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.material = material;
        setCreativeTab(Mineria.mineriaTab);
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxDamage(maxDamage);
    }

    @Override
    public float getAttackDamage()
    {
        return this.attackDamage;
    }

    public float getAttackSpeed()
    {
        return this.attackSpeed;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.attackSpeed, 0));
        }

        return multimap;
    }
}
