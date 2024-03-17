package io.github.mineria_mc.mineria.common.containers;

import org.jetbrains.annotations.Nullable;

import io.github.mineria_mc.mineria.common.block.titane_extractor.TitaneExtractorBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.ExtractorOutputSlot;
import io.github.mineria_mc.mineria.common.containers.slots.FilterSlot;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaMenuTypesRegistry;
import io.github.mineria_mc.mineria.util.FunctionalIntReferenceHolder;
import io.github.mineria_mc.mineria.util.MineriaUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;

public class TitaneExtractorMenu extends MineriaMenu<TitaneExtractorBlockEntity> {

    private final FunctionalIntReferenceHolder currentExtractTime;

    public TitaneExtractorMenu(int id, Inventory playerInv, TitaneExtractorBlockEntity tileEntity) {
        super(MineriaMenuTypesRegistry.TITANE_EXTRACTOR, id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 101);

        this.addDataSlot(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static TitaneExtractorMenu create(int windowID, Inventory playerInv, FriendlyByteBuf data) {
        return new TitaneExtractorMenu(windowID, playerInv, getTileEntity(TitaneExtractorBlockEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(TitaneExtractorBlockEntity tile) {
        this.addSlot(new Slot(tile, 0, 10, 7));
        this.addSlot(new Slot(tile, 1, 41, 7));
        this.addSlot(new FilterSlot(tile, 2, 24, 78));
        this.addSlot(new ExtractorOutputSlot(tile, 3, 95, 47));
    }

    public int getExtractTimeScaled() {
        return this.currentExtractTime.get() != 0 && this.tile.totalExtractTime != 0 ? this.currentExtractTime.get() * 53 / this.tile.totalExtractTime : 0;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(3)
                .withSpecialInput(1, stack -> MineriaUtil.checkFluidFromStack(stack, Fluids.WATER))
                .withSpecialInput(2, stack -> stack.is(MineriaItemRegistry.FILTER));
    }

    @Override
    protected int getIndexForStack(ItemStack stack) {
        return stack.getItem().equals(MineriaBlockRegistry.getItemFromBlock(MineriaBlockRegistry.MINERAL_SAND)) ? 0 : -1;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
