package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.common.block.xp_block.XpBlockEntity;
import io.github.mineria_mc.mineria.util.Constants;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MineriaBlockEntityRegistry {
    private static final Map<String, BlockEntityType<?>> BLOCK_ENTITIES = new HashMap<String, BlockEntityType<?>>();

    public static final BlockEntityType<XpBlockEntity> XP_BLOCK = register("xp_block", MineriaBlockRegistry.XP_BLOCK, XpBlockEntity::new);

    public static void register() {
        for(Map.Entry<String, BlockEntityType<?>> entry : BLOCK_ENTITIES.entrySet()) {
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Constants.MODID, entry.getKey()), entry.getValue());
        }
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Block parentBlock, FabricBlockEntityTypeBuilder.Factory<T> entity) {
        BlockEntityType<T> entityBlock = FabricBlockEntityTypeBuilder.create(entity, parentBlock).build();
        BLOCK_ENTITIES.put(name, entityBlock);
        return entityBlock;
    }
}
