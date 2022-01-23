package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.recipe.*;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaRecipeSerializers
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Mineria.MODID);

    public static final IRecipeType<InfuserRecipe> INFUSER_TYPE = MineriaUtils.registerRecipeType(InfuserRecipe.RECIPE_ID);
    public static final IRecipeType<DistillerRecipe> DISTILLER_TYPE = MineriaUtils.registerRecipeType(DistillerRecipe.RECIPE_ID);
    public static final IRecipeType<AbstractApothecaryTableRecipe> APOTHECARY_TABLE_TYPE = MineriaUtils.registerRecipeType(AbstractApothecaryTableRecipe.RECIPE_ID);

    public static final RegistryObject<IRecipeSerializer<InfuserRecipe>> INFUSER = RECIPE_SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<CustomSuspiciousStewRecipe>> CUSTOM_SUSPICIOUS_STEW = RECIPE_SERIALIZERS.register("custom_suspicious_stew", () -> new SpecialRecipeSerializer<>(CustomSuspiciousStewRecipe::new));
    public static final RegistryObject<IRecipeSerializer<DistillerRecipe>> DISTILLER = RECIPE_SERIALIZERS.register("distiller", DistillerRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<ApothecaryTableRecipe>> APOTHECARY_TABLE = RECIPE_SERIALIZERS.register("apothecary_table", ApothecaryTableRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<PoisonousJarRecipe>> POISONOUS_JAR = RECIPE_SERIALIZERS.register("poisonous_jar", () -> new SpecialRecipeSerializer<>(PoisonousJarRecipe::new));
    public static final RegistryObject<IRecipeSerializer<FillCupRecipe>> FILL_CUP = RECIPE_SERIALIZERS.register("fill_cup", () -> new SpecialRecipeSerializer<>(FillCupRecipe::new));
    public static final RegistryObject<IRecipeSerializer<MineriaSmithingRecipe>> SMITHING = RECIPE_SERIALIZERS.register("smithing", MineriaSmithingRecipe.Serializer::new);
}
