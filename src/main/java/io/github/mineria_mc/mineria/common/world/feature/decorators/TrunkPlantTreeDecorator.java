package io.github.mineria_mc.mineria.common.world.feature.decorators;

import io.github.mineria_mc.mineria.common.init.MineriaTreeDecoratorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class TrunkPlantTreeDecorator extends TrunkVineDecorator {
    public static final Codec<TrunkPlantTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(dec -> dec.state)
    ).apply(instance, TrunkPlantTreeDecorator::new));

    private final BlockState state;

    public TrunkPlantTreeDecorator(BlockState state) {
        this.state = state;
    }

    @Override
    public void place(TreeDecorator.Context ctx) {
        RandomSource pRandom = ctx.random();
        ctx.logs().forEach((pos) -> {
            if (pRandom.nextInt(3) > 0) {
                BlockPos west = pos.west();
                if (ctx.isAir(west)) {
                    addVine(ctx::setBlock, west);
                }
            }

            if (pRandom.nextInt(3) > 0) {
                BlockPos east = pos.east();
                if (ctx.isAir(east)) {
                    addVine(ctx::setBlock, east);
                }
            }

            if (pRandom.nextInt(3) > 0) {
                BlockPos north = pos.north();
                if (ctx.isAir(north)) {
                    addVine(ctx::setBlock, north);
                }
            }

            if (pRandom.nextInt(3) > 0) {
                BlockPos south = pos.south();
                if (ctx.isAir(south)) {
                    addVine(ctx::setBlock, south);
                }
            }

        });
    }

    protected void addVine(BiConsumer<BlockPos, BlockState> blockSetter, BlockPos pos) {
        blockSetter.accept(pos, state);
    }

    @Nonnull
    @Override
    protected TreeDecoratorType<?> type() {
        return MineriaTreeDecoratorTypes.TRUNK_PLANT.get();
    }
}
