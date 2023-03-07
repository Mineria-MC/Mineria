package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.world.feature.structure.pool_elements.SingleDataPoolElement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MineriaSPETypes {
    public static final DeferredRegister<StructurePoolElementType<?>> SPE_TYPES = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, Mineria.MODID);

    public static final RegistryObject<StructurePoolElementType<SingleDataPoolElement>> SINGLE_DATA = SPE_TYPES.register("single_data_pool_element", () -> () -> SingleDataPoolElement.CODEC);
}
