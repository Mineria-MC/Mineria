package io.github.mineria_mc.mineria.common.recipe;

import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class FillCupRecipe extends AbstractApothecaryTableRecipe {
    public static final Lazy<Map<PoisonSource, Item>> POISON_SOURCE_TO_CUP = Lazy.of(() -> Util.make(new HashMap<>(), map -> {
        map.put(PoisonSource.ELDERBERRY, MineriaItems.ELDERBERRY_TEA.get());
        map.put(PoisonSource.STRYCHNOS_TOXIFERA, MineriaItems.STRYCHNOS_TOXIFERA_TEA.get());
        map.put(PoisonSource.STRYCHNOS_NUX_VOMICA, MineriaItems.STRYCHNOS_NUX_VOMICA_TEA.get());
        map.put(PoisonSource.BELLADONNA, MineriaItems.BELLADONNA_TEA.get());
        map.put(PoisonSource.MANDRAKE, MineriaItems.MANDRAKE_TEA.get());
    }));

    public FillCupRecipe(ResourceLocation id) {
        super(id, Ingredient.of(MineriaItems.CUP.get()));
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, Level world) {
        return this.input.test(wrapper.getItem(1)) && wrapper.getPoisonSource() != null;
    }

    @Override
    public ItemStack assemble(ApothecaryTableInventoryWrapper wrapper) {
        PoisonSource poisonSource = wrapper.getPoisonSource();
        return poisonSource == null ? new ItemStack(MineriaItems.CUP.get()) : new ItemStack(POISON_SOURCE_TO_CUP.get().get(poisonSource));
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return new ItemStack(MineriaItems.STRYCHNOS_TOXIFERA_TEA.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.FILL_CUP.get();
    }

    @Override
    public boolean renderInJEI() {
        return false;
    }
}
