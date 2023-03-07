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
import java.util.List;

public class PolygonHeight extends HeightProvider {
    public static final Codec<PolygonHeight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VerticalAnchor.CODEC.fieldOf("minInclusive").forGetter(h -> h.minInclusive),
            VerticalAnchor.CODEC.fieldOf("maxInclusive").forGetter(h -> h.maxInclusive),
            Codec.list(VerticalAnchor.CODEC).fieldOf("points").forGetter(h -> h.points)
    ).apply(instance, PolygonHeight::new));

    private static final Logger LOGGER = LogUtils.getLogger();
    private final VerticalAnchor minInclusive;
    private final VerticalAnchor maxInclusive;
    private final List<VerticalAnchor> points;

    private PolygonHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, List<VerticalAnchor> points) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.points = points;
    }

//    public static PolygonHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, VerticalAnchor points) {
//        return new PolygonHeight(minInclusive, maxInclusive, points);
//    }

    @Override
    public int sample(@Nonnull RandomSource random, @Nonnull WorldGenerationContext ctx) {
        int min = this.minInclusive.resolveY(ctx);
        int max = this.maxInclusive.resolveY(ctx);
        if(min > max) {
            LOGGER.warn("Empty height range: {}", this);
            return min;
        }
        int highestPoint = /*this.highestPoint.resolveY(ctx)*/0;
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
        return MineriaHeightProviderTypes.POLYGON.get();
    }
}
