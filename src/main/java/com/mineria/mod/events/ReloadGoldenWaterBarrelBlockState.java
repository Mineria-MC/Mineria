package com.mineria.mod.events;

import com.mineria.mod.References;
import com.mineria.mod.blocks.barrel.golden.GoldenWaterBarrelTileEntity;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadGoldenWaterBarrelBlockState
{
    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event)
    {
        event.getWorld().getChunk(event.getPos().asBlockPos()).getTileEntitiesPos().stream()
                .map(event.getWorld()::getTileEntity)
                .filter(GoldenWaterBarrelTileEntity.class::isInstance)
                .map(GoldenWaterBarrelTileEntity.class::cast)
                .forEach(GoldenWaterBarrelTileEntity::reloadBlockState);
    }
}
