package com.mineria.mod.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CustomFlowerPotBlock extends FlowerPotBlock
{
    public CustomFlowerPotBlock(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> plant, Properties properties)
    {
        super(emptyPot, plant, properties);
        if(Blocks.FLOWER_POT != null) ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant.get().delegate.name(), () -> this);
    }
}
