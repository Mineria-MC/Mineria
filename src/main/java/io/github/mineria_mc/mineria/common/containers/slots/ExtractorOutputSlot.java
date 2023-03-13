package io.github.mineria_mc.mineria.common.containers.slots;

import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ExtractorOutputSlot extends OutputSlot {
    public ExtractorOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
        if (player instanceof ServerPlayer) {
            MineriaCriteriaTriggers.EXTRACTED_ITEM.trigger((ServerPlayer) player, stack);
        }

        super.onTake(player, stack);
    }
}
