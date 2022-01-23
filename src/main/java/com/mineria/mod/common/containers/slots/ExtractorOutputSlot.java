package com.mineria.mod.common.containers.slots;

import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ExtractorOutputSlot extends OutputSlot
{
    public ExtractorOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack)
    {
        if(player instanceof ServerPlayerEntity)
        {
            MineriaCriteriaTriggers.EXTRACTED_ITEM.trigger((ServerPlayerEntity) player, stack);
        }

        super.onTake(player, stack);
        return stack;
    }
}
