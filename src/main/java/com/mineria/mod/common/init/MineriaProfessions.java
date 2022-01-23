package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.entity.profession.ApothecaryProfession;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaProfessions
{
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, Mineria.MODID);

    public static final RegistryObject<VillagerProfession> APOTHECARY = PROFESSIONS.register("apothecary", ApothecaryProfession::new);
}
