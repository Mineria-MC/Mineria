package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.extractor.ExtractorBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.ExtractorOutputSlot;
import io.github.mineria_mc.mineria.common.containers.slots.FilterSlot;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.util.FunctionalIntReferenceHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ExtractorMenu extends MineriaMenu<ExtractorBlockEntity> {
    private final FunctionalIntReferenceHolder currentExtractTime;

    public ExtractorMenu(int id, Inventory playerInv, ExtractorBlockEntity tileEntity) {
        super(MineriaMenuTypes.EXTRACTOR.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 118);

        this.addDataSlot(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static ExtractorMenu create(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        return new ExtractorMenu(id, playerInv, getTileEntity(ExtractorBlockEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(ExtractorBlockEntity tile) {
        IItemHandler handler = tile.getInventory();
        this.addSlot(new SlotItemHandler(handler, 0, 8, 20));
        this.addSlot(new SlotItemHandler(handler, 1, 43, 20));
        this.addSlot(new FilterSlot(handler, 2, 25, 92));
        this.addSlot(new ExtractorOutputSlot(handler, 3, 137, 92));
        this.addSlot(new ExtractorOutputSlot(handler, 4, 137, 70));
        this.addSlot(new ExtractorOutputSlot(handler, 5, 137, 48));
        this.addSlot(new ExtractorOutputSlot(handler, 6, 137, 27));
        this.addSlot(new ExtractorOutputSlot(handler, 7, 102, 8));
        this.addSlot(new ExtractorOutputSlot(handler, 8, 70, 27));
        this.addSlot(new ExtractorOutputSlot(handler, 9, 70, 48));
    }

    @OnlyIn(Dist.CLIENT)
    public int getExtractTimeScaled() {
        return this.currentExtractTime.get() != 0 && this.tile.totalExtractTime != 0 ? this.currentExtractTime.get() * 53 / this.tile.totalExtractTime : 0;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(3, 9)
                .withSpecialInput(1, stack -> AbstractWaterBarrelBlockEntity.checkFluidFromStack(stack, Fluids.WATER))
                .withSpecialInput(2, stack -> stack.is(MineriaItems.FILTER.get()));
    }

    @Override
    protected int getIndexForStack(ItemStack stack) {
        return stack.is(MineriaBlocks.getItemFromBlock(MineriaBlocks.MINERAL_SAND.get())) ? 0 : -1;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
