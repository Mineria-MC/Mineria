package com.mineria.mod.common.containers;

import com.mineria.mod.util.MineriaUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class MineriaContainer<T extends BlockEntity> extends AbstractContainerMenu
{
    protected final T tile;
    protected final ContainerLevelAccess worldPosCallable;

    public MineriaContainer(@Nullable MenuType<?> type, int id, T tileEntity)
    {
        super(type, id);

        this.tile = tileEntity;
        this.worldPosCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());

        this.createInventorySlots(tileEntity);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends BlockEntity> T getTileEntity(Class<T> clazz, Inventory playerInv, FriendlyByteBuf data)
    {
        Objects.requireNonNull(playerInv, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInv.player.level.getBlockEntity(data.readBlockPos());
        if(tileAtPos != null)
        {
            if (tileAtPos.getClass().equals(clazz))
            {
                return (T)tileAtPos;
            }
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    protected void createPlayerInventorySlots(Inventory playerInv, int startX, int startY)
    {
        int slotSizePlus2 = 18;

        int hotbarY = startY + 58;

        for(int column = 0; column < 9; column++)
        {
            this.addSlot(new Slot(playerInv, column, startX + (column * slotSizePlus2), hotbarY));
        }

        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 9; column++)
            {
                this.addSlot(new Slot(playerInv, 9 + (row * 9) + column, startX + (column * slotSizePlus2), startY + (row * slotSizePlus2)));
            }
        }
    }

    protected abstract void createInventorySlots(T tile);

    @Override
    public boolean stillValid(Player playerIn)
    {
        return stillValid(worldPosCallable, playerIn, this.tile.getBlockState().getBlock());
    }

    protected StackTransferHandler getStackTransferHandler()
    {
        return StackTransferHandler.NONE;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        StackTransferHandler handler = this.getStackTransferHandler();
        if(handler == StackTransferHandler.NONE || handler.maxOutputIndex < 0)
            return ItemStack.EMPTY;

        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        final int minOutputIndex = handler.minOutputIndex;
        final int maxOutputIndex = handler.maxOutputIndex;
        final int lastIndex = this.slots.size() - 1;
        final int lastInventoryIndex = lastIndex - 9;

        if(slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if(index >= minOutputIndex && index <= maxOutputIndex)
            {
                if(!this.moveItemStackTo(slotStack, maxOutputIndex + 1, lastIndex, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(slotStack, stackToTransfer);
            }
            else if(index >= maxOutputIndex)
            {
                final int ingredientIndex = getIndexForRecipe(slotStack);
                final int fuelIndex = handler.fuelIndex;
                final Int2ObjectMap<Predicate<ItemStack>> specialInputs = handler.specialInputs;

                if(ingredientIndex > -1)
                {
                    if(!this.moveItemStackTo(slotStack, ingredientIndex, ingredientIndex + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(fuelIndex > -1 && ForgeHooks.getBurnTime(slotStack, handler.fuelType) > 0)
                {
                    if(!this.moveItemStackTo(slotStack, fuelIndex, fuelIndex + 1, false)) return ItemStack.EMPTY;
                }
                else if(!specialInputs.isEmpty())
                {
                    for (int i : specialInputs.keySet())
                    {
                        if(specialInputs.get(i).test(slotStack))
                        {
                            if(!this.moveItemStackTo(slotStack, i, i + 1, false)) return ItemStack.EMPTY;
                            break;
                        }
                    }
                }
                else if (index < lastInventoryIndex)
                {
                    if (!this.moveItemStackTo(slotStack, lastInventoryIndex, lastIndex, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < lastIndex && !this.moveItemStackTo(slotStack, maxOutputIndex + 1, lastInventoryIndex, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotStack, maxOutputIndex + 1, lastIndex, false))
            {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (slotStack.getCount() == stackToTransfer.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return stackToTransfer;
    }

    protected abstract int getIndexForRecipe(ItemStack stack);

    protected <R extends Recipe<?>> int getIndexForRecipe(ItemStack stack, RecipeType<R> type, Class<R> recipeClass)
    {
        Recipe<?> recipe = findRecipeForStack(stack, type, recipeClass);
        if(recipe != null)
        {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            for (int i = 0; i < recipe.getIngredients().size(); i++)
            {
                if(ingredients.get(i).test(stack))
                    return i;
            }
        }

        return -1;
    }

    @Nullable
    protected <R extends Recipe<?>> Recipe<?> findRecipeForStack(ItemStack stack, RecipeType<R> type, Class<R> recipeClass)
    {
        Set<Recipe<?>> recipes = MineriaUtils.findRecipesByType(type, this.tile.getLevel());
        if(recipes != null)
        {
            for(Recipe<?> recipe : recipes.stream().filter(recipeClass::isInstance).collect(Collectors.toList()))
            {
                if(recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(stack)))
                    return recipe;
            }
        }

        return null;
    }

    /**
     * A class used to handle stack transferring in {@link MineriaContainer#quickMoveStack(Player, int)}.
     * Better for less code copy-pasting.
     */
    public static class StackTransferHandler
    {
        /**
         * Default instance of StackTransferHandler, used to define no handler.
         */
        public static final StackTransferHandler NONE = new StackTransferHandler(-1);

        private final int minOutputIndex;
        private final int maxOutputIndex;
        private int fuelIndex = -1;
        @Nullable
        private RecipeType<?> fuelType;
        private final Int2ObjectMap<Predicate<ItemStack>> specialInputs;

        public StackTransferHandler(int outputIndex)
        {
            this(outputIndex, outputIndex);
        }

        public StackTransferHandler(int minOutputIndex, int maxOutputIndex)
        {
            this.minOutputIndex = minOutputIndex;
            this.maxOutputIndex = maxOutputIndex;
            this.specialInputs = new Int2ObjectOpenHashMap<>();
        }

        /**
         * This method specifies an index where fuel may be transferred to.
         *
         * @param fuelIndex the index where the fuel should go.
         * @param type the recipe type of the fuel. Null for default. See {@link ForgeHooks#getBurnTime(ItemStack, RecipeType)}
         * @return the current instance of the StackTransferHandler.
         */
        public StackTransferHandler withFuel(int fuelIndex, @Nullable RecipeType<?> type)
        {
            if(this == StackTransferHandler.NONE) return this;
            this.fuelIndex = fuelIndex;
            this.fuelType = type;
            return this;
        }

        /**
         * This method specifies an index for a specified stack.
         *
         * @param inputIndex the index where the stack should go.
         * @param condition the condition the stack should match to go in the indicated index.
         * @return the current instance of the StackTransferHandler.
         */
        public StackTransferHandler withSpecialInput(int inputIndex, Predicate<ItemStack> condition)
        {
            if(inputIndex > minOutputIndex)
                throw new IllegalArgumentException("The given index doesn't correspond to an input!");
            this.specialInputs.put(inputIndex, condition);
            return this;
        }
    }
}
