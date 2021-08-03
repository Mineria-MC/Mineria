package com.mineria.mod.init;

import com.google.common.collect.ImmutableSet;
import com.mineria.mod.References;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ProfessionsInit
{
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, References.MODID);

    public static final RegistryObject<VillagerProfession> APOTHECARY = PROFESSIONS.register("apothecary", () -> new VillagerProfession("apothecary", PointOfInterestTypeInit.APOTHECARY.get(), ImmutableSet.of(), ImmutableSet.of(), null));
}
