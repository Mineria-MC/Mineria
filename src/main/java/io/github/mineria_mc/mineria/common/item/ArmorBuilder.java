package io.github.mineria_mc.mineria.common.item;

import org.apache.logging.log4j.util.TriConsumer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArmorBuilder {
    
    private final ArmorMaterial material;
    private final ArmorItem.Type type;
    private boolean hasEffect = false;
    private TriConsumer<ItemStack, Level, Player> function = (world, player, stack) -> {};

    public ArmorBuilder(ArmorMaterial material, ArmorItem.Type type) {
        this.material = material;
        this.type = type;
    }

    public ArmorBuilder addEffect() {
        this.hasEffect = true;
        return this;
    }

    public ArmorBuilder onArmorTick(TriConsumer<ItemStack, Level, Player> function) {
        this.function = function;
        return this;
    }

    public ArmorItem build() {
        return new BuiltArmor(this);
    }

    private static class BuiltArmor extends ArmorItem {
        private final ArmorBuilder builder;

        public BuiltArmor(ArmorBuilder builder) {
            super(builder.material, builder.type, new Item.Properties());
            this.builder = builder;
        }

        @Override
        public boolean isFoil(ItemStack itemStack) {
            return builder.hasEffect || super.isFoil(itemStack);
        }

        /*@Override
        public void onArmorTick(ItemStack stack, Level world, Player player) {
            builder.function.accept(stack, world, player);
        }*/
    }
}
