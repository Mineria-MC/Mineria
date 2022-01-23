package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaContainerTypes
{
    //Deferred Register
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Mineria.MODID);

    public static final RegistryObject<ContainerType<TitaneExtractorContainer>> TITANE_EXTRACTOR = CONTAINER_TYPES.register("titane_extractor", () -> IForgeContainerType.create(TitaneExtractorContainer::create));
    public static final RegistryObject<ContainerType<InfuserContainer>> INFUSER = CONTAINER_TYPES.register("infuser", () -> IForgeContainerType.create(InfuserContainer::create));
    public static final RegistryObject<ContainerType<XpBlockContainer>> XP_BLOCK = CONTAINER_TYPES.register("xp_block", () -> IForgeContainerType.create(XpBlockContainer::create));
    public static final RegistryObject<ContainerType<CopperWaterBarrelContainer>> COPPER_WATER_BARREL = CONTAINER_TYPES.register("copper_water_barrel", () -> IForgeContainerType.create(CopperWaterBarrelContainer::create));
    public static final RegistryObject<ContainerType<GoldenWaterBarrelContainer>> GOLDEN_WATER_BARREL = CONTAINER_TYPES.register("golden_water_barrel", () -> IForgeContainerType.create(GoldenWaterBarrelContainer::create));
    public static final RegistryObject<ContainerType<ExtractorContainer>> EXTRACTOR = CONTAINER_TYPES.register("extractor", () -> IForgeContainerType.create(ExtractorContainer::create));
    public static final RegistryObject<ContainerType<DistillerContainer>> DISTILLER = CONTAINER_TYPES.register("distiller", () -> IForgeContainerType.create(DistillerContainer::create));
    public static final RegistryObject<ContainerType<ApothecaryTableContainer>> APOTHECARY_TABLE = CONTAINER_TYPES.register("apothecary_table", () -> IForgeContainerType.create(ApothecaryTableContainer::create));
}
