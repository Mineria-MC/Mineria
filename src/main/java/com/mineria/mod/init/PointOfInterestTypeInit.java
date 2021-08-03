package com.mineria.mod.init;

import com.mineria.mod.References;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.village.PointOfInterestType.getAllStates;

public class PointOfInterestTypeInit
{
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, References.MODID);

    public static final RegistryObject<PointOfInterestType> APOTHECARY = POI_TYPES.register("apothecary", () -> new PointOfInterestType("apothecary", getAllStates(BlocksInit.INFUSER), 1, 1));
}
