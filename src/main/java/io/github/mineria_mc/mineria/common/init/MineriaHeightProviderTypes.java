package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.world.gen.height_providers.AnyTriangleHeight;
import io.github.mineria_mc.mineria.common.world.gen.height_providers.PolygonHeight;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MineriaHeightProviderTypes {
    public static final DeferredRegister<HeightProviderType<?>> PROVIDER_TYPES = DeferredRegister.create(Registries.HEIGHT_PROVIDER_TYPE, Mineria.MODID);

    public static final RegistryObject<HeightProviderType<AnyTriangleHeight>> ANY_TRIANGLE = PROVIDER_TYPES.register("any_triangle", () -> () -> AnyTriangleHeight.CODEC);
    public static final RegistryObject<HeightProviderType<PolygonHeight>> POLYGON = PROVIDER_TYPES.register("polygon", () -> () -> PolygonHeight.CODEC);
}
