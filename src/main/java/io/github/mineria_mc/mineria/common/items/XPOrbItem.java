package io.github.mineria_mc.mineria.common.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

public class XPOrbItem extends Item {
    private final int xpValue;

    public XPOrbItem(int xpValue, Item.Properties properties) {
        super(properties.stacksTo(16));
        this.xpValue = xpValue;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getAbilities().instabuild) {
            player.giveExperiencePoints(xpValue);
        } else {
            player.giveExperiencePoints(xpValue);
            stack.shrink(1);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.xpValue == 64;
    }
}
