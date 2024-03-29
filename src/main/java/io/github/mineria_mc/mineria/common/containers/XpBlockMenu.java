package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.XpBlockSlot;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class XpBlockMenu extends MineriaMenu<XpBlockEntity> {
    private final ContainerData data;

    public XpBlockMenu(int id, Inventory playerInv, XpBlockEntity tileEntity, ContainerData dataSlots) {
        super(MineriaMenuTypes.XP_BLOCK.get(), id, tileEntity);
        this.data = dataSlots;
        Mineria.getProxy().onXpBlockContainerOpen(playerInv.player, tileEntity);
        /*if(playerInv.player instanceof ClientPlayerEntity)
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(new XpBlockMessageHandler.XpBlockMessage(tileEntity.getBlockPos()));
        else tileEntity.onOpen(playerInv.player);*/

        this.createPlayerInventorySlots(playerInv, 8, 84);

        this.addDataSlots(dataSlots);
    }

    public static XpBlockMenu create(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        XpBlockEntity tile = getTileEntity(XpBlockEntity.class, playerInv, buffer);
        return new XpBlockMenu(id, playerInv, tile, tile.dataSlots);
    }

    @Override
    protected void createInventorySlots(XpBlockEntity tile) {
//        this.addSlot(new XpBlockSlot(tile.getInventory(), 0, 113, 21));
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new XpBlockSlot(tile.getInventory(), (row * 9) + column, 8 + (column * 18), 16 + (row * 18)));
            }
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.worldPosCallable.execute((world, pos) -> this.clearContainer(playerIn, new SimpleContainer(this.tile.getInventory().toNonNullList().toArray(new ItemStack[0]))));
        this.tile.clearContent();
    }

    public BlockPos getTileEntityPos() {
        return tile.getBlockPos();
    }

    @OnlyIn(Dist.CLIENT)
    public void setActive(boolean value) {
        this.setData(0, value ? 1 : 0);
    }

    @OnlyIn(Dist.CLIENT)
    public void setOrbItemDelay(int delay) {
        this.setData(1, delay);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isActive() {
        return this.data.get(0) == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getOrbItemDelay() {
        return this.data.get(1);
    }

    public void onClose() {
        this.tile.onClose();
    }

    /*@Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(0);

        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (this.moveItemStackTo(itemstack1, 0, 1, false))
            {
                return ItemStack.EMPTY;
            }
            else if (index >= 1 && index < 28)
            {
                if (!this.moveItemStackTo(itemstack1, 28, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 28 && index < 37)
            {
                if (!this.moveItemStackTo(itemstack1, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 1, 37, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }*/

    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(0, 17);
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
