package com.mineria.mod.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CustomWeaponItem extends SwordItem
{
    public CustomWeaponItem(IItemTier tier, float attackDamage, float attackSpeed, Properties properties)
    {
        super(tier, 0, attackSpeed, properties);
        ObfuscationReflectionHelper.setPrivateValue(SwordItem.class, this, attackDamage, "attackDamage");
    }
}
