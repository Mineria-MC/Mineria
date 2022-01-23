package com.mineria.mod.common.world.feature.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;

import java.util.Set;

public class TrunkPlantTreeDecorator extends TrunkVineTreeDecorator
{
    public static final Codec<TrunkPlantTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(dec -> dec.state)
    ).apply(instance, TrunkPlantTreeDecorator::new));

    private final BlockState state;

    public TrunkPlantTreeDecorator(BlockState state)
    {
        this.state = state;
    }

    @Override
    protected void placeVine(IWorldWriter world, BlockPos pos, BooleanProperty property, Set<BlockPos> positions, MutableBoundingBox boundingBox)
    {
        this.setBlock(world, pos, state.setValue(property, true), positions, boundingBox);
    }
}
