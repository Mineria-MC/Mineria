package io.github.mineria_mc.mineria.common.world.gen.height_providers;

import io.github.mineria_mc.mineria.common.init.MineriaHeightProviderTypes;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

public class AnyTriangleHeight extends HeightProvider {
    public static final Codec<AnyTriangleHeight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VerticalAnchor.CODEC.fieldOf("minInclusive").forGetter(h -> h.minInclusive),
            VerticalAnchor.CODEC.fieldOf("maxInclusive").forGetter(h -> h.maxInclusive),
            VerticalAnchor.CODEC.fieldOf("highestPoint").forGetter(h -> h.highestPoint)
    ).apply(instance, AnyTriangleHeight::new));

    private static final Logger LOGGER = LogUtils.getLogger();
    private final VerticalAnchor minInclusive;
    private final VerticalAnchor maxInclusive;
    private final VerticalAnchor highestPoint;

    private AnyTriangleHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, VerticalAnchor highestPoint) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.highestPoint = highestPoint;
    }

    public static AnyTriangleHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
        return new AnyTriangleHeight(minInclusive, maxInclusive, ctx -> (maxInclusive.resolveY(ctx) - minInclusive.resolveY(ctx)) / 2);
    }

    public static AnyTriangleHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, VerticalAnchor middlePoint) {
        return new AnyTriangleHeight(minInclusive, maxInclusive, middlePoint);
    }

    @Override
    public int sample(@Nonnull RandomSource random, @Nonnull WorldGenerationContext ctx) {
        int min = this.minInclusive.resolveY(ctx);
        int max = this.maxInclusive.resolveY(ctx);
        if(min > max) {
            LOGGER.warn("Empty height range: {}", this);
            return min;
        }
        int highestPoint = this.highestPoint.resolveY(ctx);
        if(highestPoint < min || highestPoint > max) {
            LOGGER.warn("Highest point is out of range: {}", this);
            return Mth.randomBetweenInclusive(random, min, max);
        }
        int dif = max - min;
        int highDif = highestPoint - min;
        int lowDif = dif - highDif;
        return min + Mth.randomBetweenInclusive(random, 0, lowDif) + Mth.randomBetweenInclusive(random, 0, highDif);
    }

    @Nonnull
    @Override
    public HeightProviderType<?> getType() {
        return MineriaHeightProviderTypes.ANY_TRIANGLE.get();
    }
}
