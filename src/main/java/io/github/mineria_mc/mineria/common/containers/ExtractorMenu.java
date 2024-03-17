package io.github.mineria_mc.mineria.common.containers;

import org.jetbrains.annotations.Nullable;

import io.github.mineria_mc.mineria.common.block.extractor.ExtractorBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.ExtractorOutputSlot;
import io.github.mineria_mc.mineria.common.containers.slots.FilterSlot;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaMenuTypesRegistry;
import io.github.mineria_mc.mineria.util.FunctionalIntReferenceHolder;
import io.github.mineria_mc.mineria.util.MineriaUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;

public class ExtractorMenu extends MineriaMenu<ExtractorBlockEntity> {
    private final FunctionalIntReferenceHolder currentExtractTime;

    public ExtractorMenu(int id, Inventory playerInv, ExtractorBlockEntity tileEntity) {
        super(MineriaMenuTypesRegistry.EXTRACTOR, id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 118);

        this.addDataSlot(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static ExtractorMenu create(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        return new ExtractorMenu(id, playerInv, getTileEntity(ExtractorBlockEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(ExtractorBlockEntity tile) {
        Container handler = tile;
        this.addSlot(new Slot(handler, 0, 8, 20));
        this.addSlot(new Slot(handler, 1, 43, 20));
        this.addSlot(new FilterSlot(handler, 2, 25, 92));
        this.addSlot(new ExtractorOutputSlot(handler, 3, 137, 92));
        this.addSlot(new ExtractorOutputSlot(handler, 4, 137, 70));
        this.addSlot(new ExtractorOutputSlot(handler, 5, 137, 48));
        this.addSlot(new ExtractorOutputSlot(handler, 6, 137, 27));
        this.addSlot(new ExtractorOutputSlot(handler, 7, 102, 8));
        this.addSlot(new ExtractorOutputSlot(handler, 8, 70, 27));
        this.addSlot(new ExtractorOutputSlot(handler, 9, 70, 48));
    }

    public int getExtractTimeScaled() {
        return this.currentExtractTime.get() != 0 && this.tile.totalExtractTime != 0 ? this.currentExtractTime.get() * 53 / this.tile.totalExtractTime : 0;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(3, 9)
                .withSpecialInput(1, stack -> MineriaUtil.checkFluidFromStack(stack, Fluids.WATER))
                .withSpecialInput(2, stack -> stack.is(MineriaItemRegistry.FILTER));
    }

    @Override
    protected int getIndexForStack(ItemStack stack) {
        return stack.is(MineriaBlockRegistry.getItemFromBlock(MineriaBlockRegistry.MINERAL_SAND)) ? 0 : -1;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
