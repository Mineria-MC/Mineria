package io.github.mineria_mc.mineria.common.data;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class MineriaLootTables {
    private static final Set<ResourceLocation> REQUIRED_TABLES = new HashSet<>();

    public static final ResourceLocation WIZARD_TOWER_CHEST = required("chests/wizard_tower_chest");
    public static final ResourceLocation WIZARD_LABORATORY_CHEST = required("chests/wizard_laboratory_chest");
    public static final ResourceLocation PAGODA_CHEST = required("chests/pagoda_chest");

    private static ResourceLocation of(String name) {
        return new ResourceLocation(Mineria.MODID, name);
    }

    private static ResourceLocation required(String name) {
        ResourceLocation id = of(name);
        REQUIRED_TABLES.add(id);
        return id;
    }

    public static Set<ResourceLocation> requiredTables() {
        return Set.copyOf(REQUIRED_TABLES);
    }
}
