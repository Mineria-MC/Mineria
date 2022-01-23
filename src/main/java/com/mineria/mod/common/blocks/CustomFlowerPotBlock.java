package com.mineria.mod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomFlowerPotBlock extends FlowerPotBlock
{
    public CustomFlowerPotBlock(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> plant, Properties properties)
    {
        super(emptyPot, plant, properties);
        if(Blocks.FLOWER_POT != null) ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(plant.get().delegate.name(), () -> this);
    }
}
