package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ApothecaryTableRecipe extends AbstractApothecaryTableRecipe {
    private final PoisonSource poisonSource;
    private final ItemStack output;

    public ApothecaryTableRecipe(ResourceLocation id, Ingredient input, PoisonSource poisonSource, @Nonnull ItemStack output) {
        super(id, input);
        this.poisonSource = poisonSource;
        this.output = output;
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, @Nullable Level world) {
        return input.test(wrapper.getItem(1)) && poisonSource.equals(wrapper.getPoisonSource());
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull ApothecaryTableInventoryWrapper wrapper, @Nonnull RegistryAccess access) {
        return this.output.copy();
    }

    @Nonnull
    @Override
    public ItemStack getOutputStack() {
        return this.output;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.APOTHECARY_TABLE.get();
    }

    public PoisonSource getPoisonSource() {
        return poisonSource;
    }

    public static class Serializer implements RecipeSerializer<ApothecaryTableRecipe> {
        @Override
        public ApothecaryTableRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            PoisonSource poisonSource = PoisonSource.byName(ResourceLocation.tryParse(GsonHelper.getAsString(json, "poison_source")));
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);

            return new ApothecaryTableRecipe(id, input, poisonSource, output);
        }

        @Nullable
        @Override
        public ApothecaryTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new ApothecaryTableRecipe(id, Ingredient.fromNetwork(buf), PoisonSource.byName(buf.readResourceLocation()), buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ApothecaryTableRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeResourceLocation(recipe.poisonSource.getId());
            buf.writeItemStack(recipe.output, false);
        }
    }
}
