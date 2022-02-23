package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.recipe.*;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Mineria.MODID);

    public static final RecipeType<InfuserRecipe> INFUSER_TYPE = MineriaUtils.registerRecipeType(InfuserRecipe.RECIPE_ID);
    public static final RecipeType<DistillerRecipe> DISTILLER_TYPE = MineriaUtils.registerRecipeType(DistillerRecipe.RECIPE_ID);
    public static final RecipeType<AbstractApothecaryTableRecipe> APOTHECARY_TABLE_TYPE = MineriaUtils.registerRecipeType(AbstractApothecaryTableRecipe.RECIPE_ID);

    public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSER = RECIPE_SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<CustomSuspiciousStewRecipe>> CUSTOM_SUSPICIOUS_STEW = RECIPE_SERIALIZERS.register("custom_suspicious_stew", () -> new SimpleRecipeSerializer<>(CustomSuspiciousStewRecipe::new));
    public static final RegistryObject<RecipeSerializer<DistillerRecipe>> DISTILLER = RECIPE_SERIALIZERS.register("distiller", DistillerRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ApothecaryTableRecipe>> APOTHECARY_TABLE = RECIPE_SERIALIZERS.register("apothecary_table", ApothecaryTableRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<PoisonousJarRecipe>> POISONOUS_JAR = RECIPE_SERIALIZERS.register("poisonous_jar", () -> new SimpleRecipeSerializer<>(PoisonousJarRecipe::new));
    public static final RegistryObject<RecipeSerializer<FillCupRecipe>> FILL_CUP = RECIPE_SERIALIZERS.register("fill_cup", () -> new SimpleRecipeSerializer<>(FillCupRecipe::new));
    public static final RegistryObject<RecipeSerializer<MineriaSmithingRecipe>> SMITHING = RECIPE_SERIALIZERS.register("smithing", MineriaSmithingRecipe.Serializer::new);
}
