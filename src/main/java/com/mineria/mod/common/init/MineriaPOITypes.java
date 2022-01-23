package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.village.PointOfInterestType.getBlockStates;

public class MineriaPOITypes
{
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Mineria.MODID);

    public static final RegistryObject<PointOfInterestType> APOTHECARY = POI_TYPES.register("apothecary", () -> new PointOfInterestType("apothecary", getBlockStates(MineriaBlocks.APOTHECARY_TABLE), 1, 1));
}
