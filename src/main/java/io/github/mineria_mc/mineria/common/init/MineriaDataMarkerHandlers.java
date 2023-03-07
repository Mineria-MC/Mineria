package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.world.feature.structure.data_markers.WizardTowerDataMarkerHandler;
import io.github.mineria_mc.mineria.common.world.feature.structure.data_markers.*;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class MineriaDataMarkerHandlers {
    public static final DeferredRegister<IDataMarkerHandler> HANDLERS = DeferredRegister.create(MineriaRegistries.Keys.DATA_MARKER_HANDLER, Mineria.MODID);

    public static final RegistryObject<IDataMarkerHandler> NONE = HANDLERS.register("none", DataMarkerHandlerBase::none);
    public static final RegistryObject<IDataMarkerHandler> WIZARD_TOWER = HANDLERS.register("wizard_tower", WizardTowerDataMarkerHandler::new);
    public static final RegistryObject<IDataMarkerHandler> WIZARD_LABORATORY = HANDLERS.register("wizard_laboratory", WizardLaboratoryDataMarkerHandler::new);
    public static final RegistryObject<IDataMarkerHandler> PAGODA = HANDLERS.register("pagoda", PagodaDataMarkerHandler::new);
    public static final RegistryObject<IDataMarkerHandler> RITUAL_STRUCTURE = HANDLERS.register("ritual_structure", RitualStructureDataMarkerHandler::new);
}
