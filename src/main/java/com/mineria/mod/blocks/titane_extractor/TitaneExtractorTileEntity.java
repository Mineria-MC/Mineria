package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TitaneExtractorTileEntity extends MineriaLockableTileEntity implements ITickableTileEntity
{
    protected int extractTime;
    protected int totalExtractTime = 200;

    public TitaneExtractorTileEntity()
    {
        super(TileEntitiesInit.TITANE_EXTRACTOR.get(), new CustomItemStackHandler(4));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.titane_extractor");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new TitaneExtractorContainer(id, player, this);
    }

    public boolean isExtracting()
    {
        return this.extractTime > 0;
    }

    @Override
    public void tick()
    {
        boolean alreadyExtracting = this.isExtracting();
        boolean dirty = false;

        if (!this.world.isRemote)
        {
            if (this.canExtract())
            {
                ++this.extractTime;

                if (this.extractTime == this.totalExtractTime)
                {
                    this.extractTime = 0;
                    this.extractItem();
                    dirty = true;
                }
            }
            if (!this.canExtract() && this.extractTime > 0)
            {
                this.extractTime = MathHelper.clamp(this.extractTime - 2, 0, this.totalExtractTime);
            }

            if (alreadyExtracting != this.isExtracting())
            {
                dirty = true;
                this.world.setBlockState(this.pos, this.getBlockState().with(TitaneExtractorBlock.LIT, this.isExtracting()));
            }
        }

        if (dirty)
        {
            this.markDirty();
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
            if(input0.isItemEqual(new ItemStack(BlocksInit.MINERAL_SAND)) && input2.isItemEqual(new ItemStack(ItemsInit.FILTER)))
            {
                ItemStack output = this.inventory.getStackInSlot(3);
                ItemStack result = new ItemStack(ItemsInit.TITANE_NUGGET, 1);

                if (!output.isItemEqual(result) && !output.isEmpty())
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
            ItemStack result = new ItemStack(ItemsInit.TITANE_NUGGET, 1);
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
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("ExtractTime", this.extractTime);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.extractTime = nbt.getInt("ExtractTime");
    }
}
