package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.recipe.InfuserRecipe;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, References.MODID);

    public static final IRecipeType<InfuserRecipe> INFUSER_TYPE = MineriaUtils.registerRecipeType(InfuserRecipe.RECIPE_ID);
    public static final RegistryObject<IRecipeSerializer<InfuserRecipe>> INFUSER = RECIPE_SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);
}
