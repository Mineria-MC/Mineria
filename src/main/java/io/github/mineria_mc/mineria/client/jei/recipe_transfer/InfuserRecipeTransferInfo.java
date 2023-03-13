package io.github.mineria_mc.mineria.client.jei.recipe_transfer;

import io.github.mineria_mc.mineria.client.jei.recipe_categories.InfuserRecipeCategory;
import io.github.mineria_mc.mineria.common.containers.InfuserMenu;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.common.recipe.InfuserRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InfuserRecipeTransferInfo implements IRecipeTransferInfo<InfuserMenu, InfuserRecipe> {
    @Nonnull
    @Override
    public Class<? extends InfuserMenu> getContainerClass() {
        return InfuserMenu.class;
    }

    @Nonnull
    @Override
    public Optional<MenuType<InfuserMenu>> getMenuType() {
        return MineriaMenuTypes.INFUSER.map(Function.identity());
    }

    @Nonnull
    @Override
    public RecipeType<InfuserRecipe> getRecipeType() {
        return InfuserRecipeCategory.TYPE;
    }

    @Override
    public boolean canHandle(@Nonnull InfuserMenu container, @Nonnull InfuserRecipe recipe) {
        return true;
    }

    @Nonnull
    @Override
    public List<Slot> getRecipeSlots(@Nonnull InfuserMenu container, @Nonnull InfuserRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        slots.add(container.getSlot(0));
        slots.add(container.getSlot(1));
        slots.add(container.getSlot(3));
        return slots;
    }

    @Nonnull
    @Override
    public List<Slot> getInventorySlots(@Nonnull InfuserMenu container, @Nonnull InfuserRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 4; i < 40; i++) {
            Slot slot = container.getSlot(i);
            slots.add(slot);
        }
        return slots;
    }
}
