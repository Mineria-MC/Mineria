package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.recipe.AbstractApothecaryTableRecipe;
import io.github.mineria_mc.mineria.common.recipe.DistillerRecipe;
import io.github.mineria_mc.mineria.common.recipe.InfuserRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MineriaRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Mineria.MODID);
    public static final RegistryObject<RecipeType<InfuserRecipe>> INFUSER_TYPE = registerRecipeType("infuser");
    public static final RegistryObject<RecipeType<DistillerRecipe>> DISTILLER_TYPE = registerRecipeType("distiller");
    public static final RegistryObject<RecipeType<AbstractApothecaryTableRecipe>> APOTHECARY_TABLE_TYPE = registerRecipeType("apothecary_table");

    public static <V extends Recipe<?>> RegistryObject<RecipeType<V>> registerRecipeType(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return new ResourceLocation(Mineria.MODID, name).toString();
            }
        });
    }
}
