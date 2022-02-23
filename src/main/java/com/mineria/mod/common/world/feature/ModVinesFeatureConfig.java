package com.mineria.mod.common.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class ModVinesFeatureConfig implements FeatureConfiguration
{
    public static final Codec<ModVinesFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(config -> config.state),
                Codec.INT.fieldOf("minHeight").forGetter(config -> config.minHeight),
                Codec.INT.fieldOf("maxHeight").forGetter(config -> config.maxHeight),
                Codec.BOOL.fieldOf("strictCount").forGetter(config -> config.strictCount),
                Codec.STRING.listOf().fieldOf("acceptedDirections").forGetter(config -> config.acceptedDirections.stream().map(Direction::getName).collect(Collectors.toList()))
        ).apply(instance, (state, minHeight, maxHeight, strictCount, acceptedDirections) -> new ModVinesFeatureConfig(state, minHeight, maxHeight, strictCount, acceptedDirections.stream().map(Direction::byName).toArray(Direction[]::new)))
    );

    public final BlockState state;
    public final int minHeight;
    public final int maxHeight;
    public final boolean strictCount;
    public final List<Direction> acceptedDirections;

    public ModVinesFeatureConfig(BlockState state, int minHeight, int maxHeight, boolean strictCount, Direction... acceptedDirections)
    {
        this.state = state;
        if(minHeight >= maxHeight)
            throw new IllegalArgumentException("The parameter minHeight can not be equal or higher than the parameter maxHeight !");
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.strictCount = strictCount;
        this.acceptedDirections = Lists.newArrayList(acceptedDirections);
    }

    public ModVinesFeatureConfig(BlockState state, int minHeight, int maxHeight)
    {
        this(state, minHeight, maxHeight, Direction.values());
    }

    public ModVinesFeatureConfig(BlockState state, int minHeight, int maxHeight, Direction... acceptedDirections)
    {
        this(state, minHeight, maxHeight, true, acceptedDirections);
    }

    public ModVinesFeatureConfig(BlockState state, int height, Direction... acceptedDirections)
    {
        this(state, height, true, acceptedDirections);
    }

    public ModVinesFeatureConfig(BlockState state, int height, boolean strictCount, Direction... acceptedDirections)
    {
        this(state, -1, height, strictCount, acceptedDirections);
    }

    public ModVinesFeatureConfig(BlockState state, int height)
    {
        this(state, height, Direction.values());
    }
}
