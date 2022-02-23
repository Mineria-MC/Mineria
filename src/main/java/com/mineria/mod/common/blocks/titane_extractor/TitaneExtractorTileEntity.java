package com.mineria.mod.common.blocks.titane_extractor;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.containers.TitaneExtractorContainer;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TitaneExtractorTileEntity extends MineriaLockableTileEntity
{
    public int extractTime;
    public int totalExtractTime = 200;

    public TitaneExtractorTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.TITANE_EXTRACTOR.get(), pos, state, new CustomItemStackHandler(4));
    }

    @Override
    protected Component getDefaultName()
    {
        return new TranslatableComponent("tile_entity.mineria.titane_extractor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player)
    {
        return new TitaneExtractorContainer(id, player, this);
    }

    public boolean isExtracting()
    {
        return this.extractTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TitaneExtractorTileEntity tile)
    {
        boolean alreadyExtracting = tile.isExtracting();
        boolean dirty = false;

        if (!level.isClientSide)
        {
            if (tile.canExtract())
            {
                ++tile.extractTime;

                if (tile.extractTime == tile.totalExtractTime)
                {
                    tile.extractTime = 0;
                    tile.extractItem();
                    dirty = true;
                }
            }
            if (!tile.canExtract() && tile.extractTime > 0)
            {
                tile.extractTime = Mth.clamp(tile.extractTime - 2, 0, tile.totalExtractTime);
            }

            if (alreadyExtracting != tile.isExtracting() && alreadyExtracting != tile.canExtract())
            {
                dirty = true;
                level.setBlockAndUpdate(pos, state.setValue(TitaneExtractorBlock.LIT, tile.isExtracting()));
            }
        }

        if (dirty)
        {
            tile.setChanged();
        }
    }

    private boolean canExtract()
    {
        ItemStack input0 = this.inventory.getStackInSlot(0);
        ItemStack input1 = this.inventory.getStackInSlot(1);
        ItemStack input2 = this.inventory.getStackInSlot(2);
        boolean hasWater = AbstractWaterBarrelTileEntity.checkWaterFromStack(input1);

        if (input0.isEmpty() || input1.isEmpty() || input2.isEmpty() || !hasWater)
        {
            return false;
        }
        else
        {
            if(input0.sameItem(new ItemStack(MineriaBlocks.MINERAL_SAND)) && input2.sameItem(new ItemStack(MineriaItems.FILTER)))
            {
                ItemStack output = this.inventory.getStackInSlot(3);
                ItemStack result = new ItemStack(MineriaItems.TITANE_NUGGET, 1);

                if (!output.sameItem(result) && !output.isEmpty())
                {
                    return false;
                }

                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
            return false;
        }
    }

    private void extractItem()
    {
        if (this.canExtract())
        {
            ItemStack input0 = this.inventory.getStackInSlot(0);
            ItemStack input1 = this.inventory.getStackInSlot(1);
            ItemStack input2 = this.inventory.getStackInSlot(2);
            ItemStack result = new ItemStack(MineriaItems.TITANE_NUGGET, 1);
            ItemStack output = this.inventory.getStackInSlot(3);

            if (output.isEmpty())
            {
                this.inventory.setStackInSlot(3, result.copy());
            }
            else if (output.getItem() == result.getItem())
            {
                output.grow(result.getCount());
            }

            input0.shrink(1);
            this.inventory.setStackInSlot(1, AbstractWaterBarrelTileEntity.decreaseFluidFromStack(input1));
            input2.shrink(1);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound)
    {
        super.save(compound);
        compound.putInt("ExtractTime", this.extractTime);
        return compound;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.extractTime = nbt.getInt("ExtractTime");
    }
}
