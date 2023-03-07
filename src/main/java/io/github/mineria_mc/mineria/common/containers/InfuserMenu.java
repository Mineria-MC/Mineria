package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.infuser.InfuserBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.FuelSlot;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import io.github.mineria_mc.mineria.util.FunctionalIntReferenceHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

public class InfuserMenu extends MineriaMenu<InfuserBlockEntity> {
    private final FunctionalIntReferenceHolder infuseTime;
    private final FunctionalIntReferenceHolder burnTime;
    private final FunctionalIntReferenceHolder currentBurnTime;

    public InfuserMenu(int id, Inventory playerInv, InfuserBlockEntity tileEntity) {
        super(MineriaMenuTypes.INFUSER.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 84);

        this.addDataSlot(infuseTime = new FunctionalIntReferenceHolder(() -> tileEntity.infuseTime, value -> tileEntity.infuseTime = value));
        this.addDataSlot(burnTime = new FunctionalIntReferenceHolder(() -> tileEntity.burnTime, value -> tileEntity.burnTime = value));
        this.addDataSlot(currentBurnTime = new FunctionalIntReferenceHolder(() -> tileEntity.currentBurnTime, value -> tileEntity.currentBurnTime = value));
    }

    public static InfuserMenu create(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        return new InfuserMenu(id, playerInv, getTileEntity(InfuserBlockEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(InfuserBlockEntity tile) {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 14, 10));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 47, 35));
        this.addSlot(new FuelSlot(tile.getInventory(), 2, 130, 35));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 3, 91, 35));
    }

    @OnlyIn(Dist.CLIENT)
    public int getInfuseTimeScaled(int pixels) {
        return this.infuseTime.get() != 0 && this.tile.totalInfuseTime != 0 ? this.infuseTime.get() * pixels / this.tile.totalInfuseTime : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnTimeScaled(int pixels) {
        int i = this.currentBurnTime.get();

        if (i == 0) {
            i = 2400;
        }

        return this.burnTime.get() * pixels / i;
    }

    public InfuserBlockEntity getTileEntity() {
        return tile;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(3)
                .withFuel(2, null)
                .withSpecialInput(1, stack -> AbstractWaterBarrelBlockEntity.checkFluidFromStack(stack, Fluids.WATER));
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return MineriaRecipeTypes.INFUSER_TYPE.get();
    }
}
