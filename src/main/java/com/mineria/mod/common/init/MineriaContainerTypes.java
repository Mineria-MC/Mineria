package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaContainerTypes
{
    //Deferred Register
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Mineria.MODID);

    public static final RegistryObject<MenuType<TitaneExtractorContainer>> TITANE_EXTRACTOR = CONTAINER_TYPES.register("titane_extractor", () -> IForgeContainerType.create(TitaneExtractorContainer::create));
    public static final RegistryObject<MenuType<InfuserContainer>> INFUSER = CONTAINER_TYPES.register("infuser", () -> IForgeContainerType.create(InfuserContainer::create));
    public static final RegistryObject<MenuType<XpBlockContainer>> XP_BLOCK = CONTAINER_TYPES.register("xp_block", () -> IForgeContainerType.create(XpBlockContainer::create));
    public static final RegistryObject<MenuType<CopperWaterBarrelContainer>> COPPER_WATER_BARREL = CONTAINER_TYPES.register("copper_water_barrel", () -> IForgeContainerType.create(CopperWaterBarrelContainer::create));
    public static final RegistryObject<MenuType<GoldenWaterBarrelContainer>> GOLDEN_WATER_BARREL = CONTAINER_TYPES.register("golden_water_barrel", () -> IForgeContainerType.create(GoldenWaterBarrelContainer::create));
    public static final RegistryObject<MenuType<ExtractorContainer>> EXTRACTOR = CONTAINER_TYPES.register("extractor", () -> IForgeContainerType.create(ExtractorContainer::create));
    public static final RegistryObject<MenuType<DistillerContainer>> DISTILLER = CONTAINER_TYPES.register("distiller", () -> IForgeContainerType.create(DistillerContainer::create));
    public static final RegistryObject<MenuType<ApothecaryTableContainer>> APOTHECARY_TABLE = CONTAINER_TYPES.register("apothecary_table", () -> IForgeContainerType.create(ApothecaryTableContainer::create));
}
