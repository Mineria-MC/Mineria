package io.github.mineria_mc.mineria.common.containers.slots;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ExtractorOutputSlot extends OutputSlot {
    public ExtractorOutputSlot(Container inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer) {
            //TODO: MineriaCriteriaTriggers.EXTRACTED_ITEM.trigger((ServerPlayer) player, stack);
        }

        super.onTake(player, stack);
    }
}
