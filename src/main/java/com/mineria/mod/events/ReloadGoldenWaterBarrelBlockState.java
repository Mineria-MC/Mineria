package com.mineria.mod.events;

import com.mineria.mod.References;
import com.mineria.mod.blocks.barrel.golden.TileEntityGoldenWaterBarrel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = References.MODID)
public class ReloadGoldenWaterBarrelBlockState
{
    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event)
    {
        for(Map.Entry<BlockPos, TileEntity> tileEntry : event.getChunkInstance().getTileEntityMap().entrySet())
        {
            TileEntity tile = event.getChunkInstance().getWorld().getTileEntity(tileEntry.getKey());

            if(tile instanceof TileEntityGoldenWaterBarrel)
            {
                TileEntityGoldenWaterBarrel barrel = (TileEntityGoldenWaterBarrel) tile;
                barrel.reloadBlockState();
            }
        }
    }
}
