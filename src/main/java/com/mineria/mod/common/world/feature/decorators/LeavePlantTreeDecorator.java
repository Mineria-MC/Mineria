package com.mineria.mod.common.world.feature.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;

import java.util.Set;

public class LeavePlantTreeDecorator extends LeaveVineTreeDecorator
{
    public static final Codec<LeavePlantTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(dec -> dec.state)
    ).apply(instance, state1 -> new LeavePlantTreeDecorator((VineBlock) state1.getBlock())));

    private final BlockState state;

    public LeavePlantTreeDecorator(VineBlock vine)
    {
        this.state = vine.defaultBlockState();
    }

    @Override
    protected void placeVine(IWorldWriter world, BlockPos pos, BooleanProperty property, Set<BlockPos> positions, MutableBoundingBox boundingBox)
    {
        this.setBlock(world, pos, state.setValue(property, true), positions, boundingBox);
    }
}
