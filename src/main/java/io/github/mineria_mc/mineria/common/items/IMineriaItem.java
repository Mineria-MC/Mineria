package io.github.mineria_mc.mineria.common.items;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IMineriaItem {
    private Item self() {
        if(!(this instanceof Item)) {
            throw new RuntimeException("IMineriaItem should only be implemented on Item classes!");
        }
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

    @OnlyIn(Dist.CLIENT)
    default boolean rendersOnHead() {
        return false;
    }
}
