package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.common.containers.TitaneExtractorMenu;
import io.github.mineria_mc.mineria.common.containers.XpBlockMenu;
import io.github.mineria_mc.mineria.util.Constants;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MineriaMenuTypesRegistry {
    private static final Map<String, MenuType<?>> MENU_TYPES = new HashMap<String, MenuType<?>>();

    public static final MenuType<XpBlockMenu> XP_BLOCK = register("xp_block", XpBlockMenu::create);
    public static final MenuType<TitaneExtractorMenu> TITANE_EXTRACTOR = register("titane_extractor", TitaneExtractorMenu::create);

    public static void register() {
        for(Map.Entry<String, MenuType<?>> entry : MENU_TYPES.entrySet()) {
            Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Constants.MODID, entry.getKey()), entry.getValue());
        }
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, ExtendedFactory<T> menu) {
        MenuType<T> menuType = new ExtendedScreenHandlerType<>(menu);
        MENU_TYPES.put(name, menuType);
        return menuType;
    }
}
