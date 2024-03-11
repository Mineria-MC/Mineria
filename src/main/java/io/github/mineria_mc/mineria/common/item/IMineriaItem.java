package io.github.mineria_mc.mineria.common.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IMineriaItem {
    
    private Item self() {
        if(!(this instanceof Item)) throw new RuntimeException("IMineriaItem can only be implemented as an Item object!");
        return (Item) this;
    }

    default int getInvulnerableTime(Entity entity) {
        return 20;
    }

    default boolean isFireResistant(ItemStack stack) {
        return self().isFireResistant();
    }

    default boolean canBeHurtBy(ItemStack stack, DamageSource source) {
        return self().canBeHurtBy(source);
    }

    default boolean rendersOnHead() {
        return false;
    }
}
