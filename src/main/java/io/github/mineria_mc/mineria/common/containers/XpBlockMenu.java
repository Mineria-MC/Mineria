package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.block.xp_block.XpBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.XpBlockSlot;
import io.github.mineria_mc.mineria.common.registries.MineriaMenuTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class XpBlockMenu extends MineriaMenu<XpBlockEntity> {
    private final ContainerData data;

    public XpBlockMenu(int id, Inventory playerInv, XpBlockEntity tileEntity, ContainerData dataSlots) {
        super(MineriaMenuTypesRegistry.XP_BLOCK, id, tileEntity);
        this.data = dataSlots;
        this.createPlayerInventorySlots(playerInv, 8, 84);

        this.addDataSlots(dataSlots);
    }

    public static XpBlockMenu create(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        XpBlockEntity tile = getTileEntity(XpBlockEntity.class, playerInv, buffer);
        return new XpBlockMenu(id, playerInv, tile, tile.dataSlots);
    }

    @Override
    protected void createInventorySlots(XpBlockEntity tile) {
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new XpBlockSlot(tile, (row * 9) + column, 8 + (column * 18), 16 + (row * 18)));
            }
        }
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.worldPosCallable.execute((world, pos) -> this.clearContainer(playerIn, new SimpleContainer(this.tile.getInventory().toArray(new ItemStack[0]))));
        this.tile.clearContent();
    }

    public BlockPos getTileEntityPos() {
        return tile.getBlockPos();
    }

    public void setActive(boolean value) {
        this.setData(0, value ? 1 : 0);
    }

    public void setOrbItemDelay(int delay) {
        this.setData(1, delay);
    }

    public boolean isActive() {
        return this.data.get(0) == 1;
    }

    public int getOrbItemDelay() {
        return this.data.get(1);
    }

    public void onClose() {
        this.tile.onClose();
    }
    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(0, 17);
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
