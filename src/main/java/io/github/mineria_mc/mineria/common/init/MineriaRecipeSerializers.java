package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.recipe.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MineriaRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Mineria.MODID);

    public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSER = RECIPE_SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<CustomSuspiciousStewRecipe>> CUSTOM_SUSPICIOUS_STEW = RECIPE_SERIALIZERS.register("custom_suspicious_stew", () -> new SimpleCraftingRecipeSerializer<>(CustomSuspiciousStewRecipe::new));
    public static final RegistryObject<RecipeSerializer<DistillerRecipe>> DISTILLER = RECIPE_SERIALIZERS.register("distiller", DistillerRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ApothecaryTableRecipe>> APOTHECARY_TABLE = RECIPE_SERIALIZERS.register("apothecary_table", ApothecaryTableRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<PoisonousJarRecipe>> POISONOUS_JAR = RECIPE_SERIALIZERS.register("poisonous_jar", () -> new MineriaRecipeSerializer<>(PoisonousJarRecipe::new));
    public static final RegistryObject<RecipeSerializer<FillCupRecipe>> FILL_CUP = RECIPE_SERIALIZERS.register("fill_cup", () -> new MineriaRecipeSerializer<>(FillCupRecipe::new));
    public static final RegistryObject<RecipeSerializer<MineriaSmithingRecipe>> SMITHING = RECIPE_SERIALIZERS.register("smithing", MineriaSmithingRecipe.Serializer::new);

}
