package com.mineria.mod.common.recipe;

import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class FillCupRecipe extends AbstractApothecaryTableRecipe
{
    public static final Map<PoisonSource, Item> POISON_SOURCE_TO_CUP = Util.make(new HashMap<>(), map -> {
        map.put(PoisonSource.ELDERBERRY, MineriaItems.ELDERBERRY_TEA);
        map.put(PoisonSource.STRYCHNOS_TOXIFERA, MineriaItems.STRYCHNOS_TOXIFERA_TEA);
        map.put(PoisonSource.STRYCHNOS_NUX_VOMICA, MineriaItems.STRYCHNOS_NUX_VOMICA_TEA);
        map.put(PoisonSource.BELLADONNA, MineriaItems.BELLADONNA_TEA);
        map.put(PoisonSource.MANDRAKE, MineriaItems.MANDRAKE_TEA);
    });

    public FillCupRecipe(ResourceLocation id)
    {
        super(id, Ingredient.of(MineriaItems.CUP));
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, World world)
    {
        return this.input.test(wrapper.getItem(1)) && wrapper.getPoisonSource() != null;
    }

    @Override
    public ItemStack assemble(ApothecaryTableInventoryWrapper wrapper)
    {
        PoisonSource poisonSource = wrapper.getPoisonSource();
        return poisonSource == null ? new ItemStack(MineriaItems.CUP) : new ItemStack(POISON_SOURCE_TO_CUP.get(poisonSource));
    }

    @Override
    public ItemStack getResultItem()
    {
        return new ItemStack(MineriaItems.STRYCHNOS_TOXIFERA_TEA);
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.FILL_CUP.get();
    }

    @Override
    public boolean renderInJEI()
    {
        return false;
    }
}
