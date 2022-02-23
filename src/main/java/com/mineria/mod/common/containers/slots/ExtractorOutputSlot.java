package com.mineria.mod.common.containers.slots;

import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ExtractorOutputSlot extends OutputSlot
{
    public ExtractorOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public void onTake(Player player, ItemStack stack)
    {
        if(player instanceof ServerPlayer)
        {
            MineriaCriteriaTriggers.EXTRACTED_ITEM.trigger((ServerPlayer) player, stack);
        }

        super.onTake(player, stack);
    }
}
