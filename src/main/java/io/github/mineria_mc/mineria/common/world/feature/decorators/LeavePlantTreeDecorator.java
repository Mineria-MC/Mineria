package io.github.mineria_mc.mineria.common.world.feature.decorators;

import io.github.mineria_mc.mineria.common.init.MineriaTreeDecoratorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class LeavePlantTreeDecorator extends LeaveVineDecorator {
    public static final Codec<LeavePlantTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(dec -> dec.state)
    ).apply(instance, LeavePlantTreeDecorator::new));

    private final BlockState state;

    public LeavePlantTreeDecorator(VineBlock vine) {
        this(vine.defaultBlockState());
    }

    public LeavePlantTreeDecorator(BlockState state) {
        super(0.25f);
        this.state = state;
    }

    @Override
    public void place(TreeDecorator.Context ctx) {
        RandomSource rand = ctx.random();
        ctx.leaves().forEach((pos) -> {
            if (rand.nextFloat() < 0.25f) {
                BlockPos west = pos.west();
                if (ctx.isAir(west)) {
                    addHangingVine(ctx, VineBlock.EAST, west);
                }
            }

            if (rand.nextFloat() < 0.25f) {
                BlockPos east = pos.east();
                if (ctx.isAir(east)) {
                    addHangingVine(ctx, VineBlock.WEST, east);
                }
            }

            if (rand.nextFloat() < 0.25f) {
                BlockPos north = pos.north();
                if (ctx.isAir(north)) {
                    addHangingVine(ctx, VineBlock.SOUTH, north);
                }
            }

            if (rand.nextFloat() < 0.25f) {
                BlockPos south = pos.south();
                if (ctx.isAir(south)) {
                    addHangingVine(ctx, VineBlock.NORTH, south);
                }
            }
        });
    }

    @Nonnull
    @Override
    protected TreeDecoratorType<?> type() {
        return MineriaTreeDecoratorTypes.LEAVE_PLANT.get();
    }

    private void addHangingVine(TreeDecorator.Context ctx, BooleanProperty property, BlockPos pos) {
        ctx.setBlock(pos, state.setValue(property, true));
        int i = 4;

        for (BlockPos blockPos = pos.below(); ctx.isAir(blockPos) && i > 0; --i) {
            ctx.setBlock(blockPos, state.setValue(property, true));
            blockPos = blockPos.below();
        }
    }

    protected void addVine(BiConsumer<BlockPos, BlockState> blockSetter, BlockPos pos) {
        blockSetter.accept(pos, state);
    }
}
