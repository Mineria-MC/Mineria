package com.mineria.mod.common.blocks.apothecary_table;

import com.mineria.mod.common.containers.ApothecaryTableContainer;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.common.recipe.AbstractApothecaryTableRecipe;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApothecaryTableTileEntity extends MineriaLockableTileEntity implements ITickableTileEntity
{
    public static final Map<Item, PoisonSource> ITEM_TO_POISON_SOURCE_MAP = Util.make(new HashMap<>(), map -> {
        map.put(MineriaItems.ELDERBERRY_TEA, PoisonSource.ELDERBERRY);
        map.put(MineriaItems.STRYCHNOS_TOXIFERA_TEA, PoisonSource.STRYCHNOS_TOXIFERA);
        map.put(MineriaItems.STRYCHNOS_NUX_VOMICA_TEA, PoisonSource.STRYCHNOS_NUX_VOMICA);
        map.put(MineriaItems.BELLADONNA_TEA, PoisonSource.BELLADONNA);
        map.put(MineriaItems.MANDRAKE_TEA, PoisonSource.MANDRAKE);
    });

    /*private final IIntArray dataAccess = new IIntArray()
    {
        public int get(int index)
        {
            switch (index)
            {
                case 0:
                    return applicationTime;
                case 1:
                    return getLiquidAmount();
                case 2:
                    return getPoisonSourceOrdinal();
                default:
                    return 0;
            }
        }

        public void set(int index, int value)
        {
            switch (index)
            {
                case 0:
                    applicationTime = value;
                    break;
                case 1:
                    setLiquidAmount(value);
                    break;
                case 2:
                    setPoisonSourceFromOrdinal(value);
                    break;
            }

        }

        public int getCount()
        {
            return 4;
        }
    };*/

    @Nullable
    private PoisonSource poisonSource = null;
    private int liquidAmount;
    public int applicationTime;
    public final int totalApplicationTime = 80;

    public ApothecaryTableTileEntity()
    {
        super(MineriaTileEntities.APOTHECARY_TABLE.get(), new CustomItemStackHandler(3));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.apothecary_table");
    }

    @Override
    protected Container createMenu(int windowId, PlayerInventory playerInv)
    {
        return new ApothecaryTableContainer(windowId, playerInv, this);
    }

    private boolean isApplying()
    {
        return applicationTime > 0;
    }

    @Override
    public void tick()
    {
        boolean alreadyExtracting = this.isApplying();
        boolean changed = false;

        if (!this.level.isClientSide)
        {
            if(canStorePoison())
            {
                storePoison();
            }

            AbstractApothecaryTableRecipe recipe = findRecipe();

            if (this.canApply(recipe))
            {
                ++this.applicationTime;

                if (this.applicationTime == this.totalApplicationTime)
                {
                    this.applicationTime = 0;
                    this.applyPoison(recipe);
                    changed = true;
                }
            }

            if (!this.canApply(recipe) && this.applicationTime > 0)
            {
                this.applicationTime = MathHelper.clamp(this.applicationTime - 2, 0, this.totalApplicationTime);
            }

            if (alreadyExtracting != this.isApplying())
            {
                changed = true;
            }
        }

        if (changed)
        {
            this.setChanged();
        }
    }

    private boolean canStorePoison()
    {
        Item drink = this.inventory.getStackInSlot(0).getItem();
        return ITEM_TO_POISON_SOURCE_MAP.containsKey(drink) && (ITEM_TO_POISON_SOURCE_MAP.get(drink).equals(this.poisonSource) || this.poisonSource == null) && this.liquidAmount < 5;
    }

    private void storePoison()
    {
        if(canStorePoison())
        {
            ItemStack stack = this.inventory.getStackInSlot(0);
            PoisonSource poisonSource = ITEM_TO_POISON_SOURCE_MAP.get(stack.getItem());
            this.inventory.setStackInSlot(0, stack.getContainerItem());
            if(this.poisonSource == null)
                this.poisonSource = poisonSource;
            this.liquidAmount = Math.min(liquidAmount + 1, 5);
        }
    }

    private boolean canRemovePoison()
    {
        return this.poisonSource != null && this.liquidAmount > 0;
    }

    private void removePoison()
    {
        this.liquidAmount = Math.max(liquidAmount - 1, 0);
        if(liquidAmount == 0) poisonSource = null;
    }

    private boolean canApply(AbstractApothecaryTableRecipe recipe)
    {
        if(recipe == null)
            return false;

        if(canRemovePoison() && recipe.matches(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource), this.level))
        {
            ItemStack output = this.inventory.getStackInSlot(2);
            ItemStack result = recipe.assemble(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource));

            if(result.isEmpty())
                return false;

            if(output.isEmpty() || output.sameItem(result) && ItemStack.tagMatches(output, result))
            {
                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void applyPoison(AbstractApothecaryTableRecipe recipe)
    {
        if(recipe == null)
            return;

        if(canApply(recipe))
        {
            ItemStack input = this.inventory.getStackInSlot(1);
            ItemStack output = this.inventory.getStackInSlot(2);
            ItemStack result = recipe.assemble(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource));

            if(output.isEmpty())
                this.inventory.setStackInSlot(2, result);
            else if(output.sameItem(result) && ItemStack.tagMatches(output, result))
                output.grow(result.getCount());

            input.shrink(1);
            removePoison();
        }
    }

    public int getPoisonSourceOrdinal()
    {
        return poisonSource == null ? -1 : poisonSource.ordinal();
    }

    public void setPoisonSourceFromOrdinal(int ordinal)
    {
        this.poisonSource = PoisonSource.byOrdinal(ordinal);
    }

    public int getLiquidAmount()
    {
        return liquidAmount;
    }

    public void setLiquidAmount(int liquidAmount)
    {
        this.liquidAmount = Math.min(5, liquidAmount);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.poisonSource = nbt.contains("PoisonSource") ? PoisonSource.byName(ResourceLocation.tryParse(nbt.getString("PoisonSource"))) : null;
        this.liquidAmount = nbt.getInt("Amount");
        this.applicationTime = nbt.getInt("ApplicationTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        super.save(compound);
        if(poisonSource != null) compound.putString("PoisonSource", poisonSource.getId().toString());
        compound.putInt("Amount", liquidAmount);
        compound.putInt("ApplicationTime", applicationTime);
        return compound;
    }

    @Nullable
    private AbstractApothecaryTableRecipe findRecipe()
    {
        Set<IRecipe<?>> recipes = MineriaUtils.findRecipesByType(MineriaRecipeSerializers.APOTHECARY_TABLE_TYPE, this.level);
        if(recipes != null)
            for(IRecipe<?> recipe : recipes)
                if(recipe instanceof AbstractApothecaryTableRecipe)
                    if(((AbstractApothecaryTableRecipe)recipe).matches(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource), this.level))
                        return (AbstractApothecaryTableRecipe)recipe;
        return null;
    }
}
