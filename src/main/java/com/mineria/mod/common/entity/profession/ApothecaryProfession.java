package com.mineria.mod.common.entity.profession;

import com.google.common.collect.ImmutableSet;
import com.mineria.mod.common.init.MineriaPOITypes;
import net.minecraft.entity.merchant.villager.VillagerProfession;

public class ApothecaryProfession extends VillagerProfession implements IMineriaProfession
{
    public ApothecaryProfession()
    {
        super("apothecary", MineriaPOITypes.APOTHECARY.get(), ImmutableSet.of(), ImmutableSet.of(), null);
    }

    @Override
    public int getMaxTrades(int level)
    {
        return 4;
    }
}
