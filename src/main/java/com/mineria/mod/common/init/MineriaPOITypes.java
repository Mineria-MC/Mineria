package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.entity.ai.village.poi.PoiType.getBlockStates;

public class MineriaPOITypes {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Mineria.MODID);

    public static final RegistryObject<PoiType> APOTHECARY = POI_TYPES.register("apothecary", () -> new PoiType("apothecary", getBlockStates(MineriaBlocks.APOTHECARY_TABLE), 1, 1));
}
