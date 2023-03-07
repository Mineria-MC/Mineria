package io.github.mineria_mc.mineria.common.init;

import com.google.common.collect.ImmutableSet;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class MineriaPOITypes {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Mineria.MODID);

    public static final RegistryObject<PoiType> APOTHECARY = POI_TYPES.register("apothecary", () -> new PoiType(getBlockStates(MineriaBlocks.APOTHECARY_TABLE.get()), 1, 1));

    private static Set<BlockState> getBlockStates(Block pBlock) {
        return ImmutableSet.copyOf(pBlock.getStateDefinition().getPossibleStates());
    }
}
