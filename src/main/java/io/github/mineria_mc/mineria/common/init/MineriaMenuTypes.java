package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MineriaMenuTypes {
    //Deferred Register
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Mineria.MODID);

    public static final RegistryObject<MenuType<TitaneExtractorMenu>> TITANE_EXTRACTOR = MENU_TYPES.register("titane_extractor", () -> IForgeMenuType.create(TitaneExtractorMenu::create));
    public static final RegistryObject<MenuType<InfuserMenu>> INFUSER = MENU_TYPES.register("infuser", () -> IForgeMenuType.create(InfuserMenu::create));
    public static final RegistryObject<MenuType<XpBlockMenu>> XP_BLOCK = MENU_TYPES.register("xp_block", () -> IForgeMenuType.create(XpBlockMenu::create));
    public static final RegistryObject<MenuType<CopperWaterBarrelMenu>> COPPER_WATER_BARREL = MENU_TYPES.register("copper_water_barrel", () -> IForgeMenuType.create(CopperWaterBarrelMenu::create));
    public static final RegistryObject<MenuType<GoldenWaterBarrelMenu>> GOLDEN_WATER_BARREL = MENU_TYPES.register("golden_water_barrel", () -> IForgeMenuType.create(GoldenWaterBarrelMenu::create));
    public static final RegistryObject<MenuType<DiamondFluidBarrelMenu>> DIAMOND_FLUID_BARREL = MENU_TYPES.register("diamond_fluid_barrel", () -> IForgeMenuType.create(DiamondFluidBarrelMenu::create));
    public static final RegistryObject<MenuType<ExtractorMenu>> EXTRACTOR = MENU_TYPES.register("extractor", () -> IForgeMenuType.create(ExtractorMenu::create));
    public static final RegistryObject<MenuType<DistillerMenu>> DISTILLER = MENU_TYPES.register("distiller", () -> IForgeMenuType.create(DistillerMenu::create));
    public static final RegistryObject<MenuType<ApothecaryTableMenu>> APOTHECARY_TABLE = MENU_TYPES.register("apothecary_table", () -> IForgeMenuType.create(ApothecaryTableMenu::create));
}
