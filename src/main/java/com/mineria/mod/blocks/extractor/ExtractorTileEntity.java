package com.mineria.mod.blocks.extractor;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import com.mineria.mod.util.jei.extractor.ExtractorRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public class ExtractorTileEntity extends MineriaLockableTileEntity implements ITickableTileEntity
{
    protected int extractTime;
    protected int totalExtractTime = 400;

    public ExtractorTileEntity()
    {
        super(TileEntitiesInit.EXTRACTOR.get(), new CustomItemStackHandler(10));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.extractor");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new ExtractorContainer(id, player, this);
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
                world.setBlockState(this.pos, this.getBlockState().with(ExtractorBlock.LIT, this.isExtracting()));
            }
        }

        if (dirty)
        {
            this.markDirty();
        }
    }

    private boolean canExtract()
    {
        ItemStack input = this.inventory.getStackInSlot(0);
        ItemStack waterSource = this.inventory.getStackInSlot(1);
        ItemStack filter = this.inventory.getStackInSlot(2);
        boolean hasWater = AbstractWaterBarrelTileEntity.checkWaterFromStack(waterSource);

        if (input.isEmpty() || waterSource.isEmpty() || filter.isEmpty() || !hasWater)
        {
            return false;
        }
        else
        {

            for(int index = 3; index < this.inventory.getSlots(); index++)
            {
                ItemStack output = this.inventory.getStackInSlot(index);
                ItemStack result = getOutputStackFromSlot(index, ExtractorRecipe.DEFAULT_RECIPE.getOutputsMap());

                if(output.isEmpty())
                {
                    continue;
                }
                else if (!output.isItemEqual(result))
                {
                    return false;
                }

                int res = output.getCount() + result.getCount();
                if(!(res <= 64 && res <= output.getMaxStackSize()))
                {
                    return false;
                }
            }
            return true;
        }
    }

    private void extractItem()
    {
        if (this.canExtract())
        {
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack waterSource = this.inventory.getStackInSlot(1);
            ItemStack filter = this.inventory.getStackInSlot(2);
            Map<Integer, ItemStack> outputs = ExtractorRecipe.DEFAULT_RECIPE.getOutputsMap();

            final int multiplier = 2;

            for(int i = 0; i < multiplier; i++)
            {
                for(Map.Entry<Integer, ItemStack> entry : outputs.entrySet())
                {
                    Random rand = new Random();
                    int chance = rand.nextInt(1000);

                    if (chance <= entry.getKey())
                    {
                        ItemStack result = entry.getValue();
                        ItemStack output = this.inventory.getStackInSlot(getOutputIndexFromStack(result, outputs));

                        if(output.getCount() + result.getCount() > 64)
                        {
                            continue;
                        }
                        if (output.isEmpty())
                        {
                            this.inventory.setStackInSlot(getOutputIndexFromStack(result, outputs), result.copy());
                        }
                        else if (output.getItem() == result.getItem())
                        {
                            output.grow(result.getCount());
                        }
                    }
                }
            }

            input.shrink(1);
            this.inventory.setStackInSlot(1, AbstractWaterBarrelTileEntity.decreaseFluidFromStack(waterSource));
            filter.shrink(1);
        }
    }

    private static ItemStack getOutputStackFromSlot(int slotId, Map<Integer, ItemStack> map)
    {
        List<Integer> ints = new ArrayList<>(map.keySet());
        ints.sort(Comparator.reverseOrder());
        return map.get(ints.get(slotId - 3));
    }

    private static int getOutputIndexFromStack(ItemStack stack, Map<Integer, ItemStack> map)
    {
        List<Integer> ints = new ArrayList<>(map.keySet());
        ints.sort(Comparator.reverseOrder());
        for(Map.Entry<Integer, ItemStack> entry : map.entrySet())
        {
            if(entry.getValue().isItemEqual(stack))
            {
                return ints.indexOf(entry.getKey()) + 3;
            }
        }
        return -1;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("ExtractTime", this.extractTime);
        compound.putInt("TotalExtractTime", this.totalExtractTime);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.extractTime = nbt.getInt("ExtractTime");
        this.totalExtractTime = nbt.getInt("TotalExtractTime");
    }
}
