package io.github.mineria_mc.mineria.common.world.feature.structure.pool_elements;

import io.github.mineria_mc.mineria.common.init.MineriaRegistries;
import io.github.mineria_mc.mineria.common.init.MineriaSPETypes;
import io.github.mineria_mc.mineria.common.world.feature.structure.data_markers.IDataMarkerHandler;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class SingleDataPoolElement extends SinglePoolElement {
    public static final Codec<SingleDataPoolElement> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(templateCodec(),
                            processorsCodec(),
                            projectionCodec(),
                            ResourceLocation.CODEC.fieldOf("data_handler").forGetter(SingleDataPoolElement::getDataHandlerId),
                            Codec.INT.fieldOf("ground_level_delta").orElse(1).forGetter(SingleDataPoolElement::getGroundLevelDelta),
                            Rotation.CODEC.optionalFieldOf("forced_rotation").forGetter(singleDataPoolElement -> Optional.ofNullable(singleDataPoolElement.getForcedRotation()))
            ).apply(instance, (template, processors, projection, dataMarker, groundDelta, forcedRotation) -> new SingleDataPoolElement(template, processors, projection, dataMarker)
                    .setGroundLevelDelta(groundDelta).setRotation(forcedRotation.orElse(null)))
    );

    protected final IDataMarkerHandler dataHandler;
    protected int groundLevelDelta = 1;
    @Nullable
    protected Rotation forcedRotation;

    public SingleDataPoolElement(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, ResourceLocation dataHandlerId) {
        super(template, processors, projection);
        this.dataHandler = getDataHandler(dataHandlerId);
    }

    public SingleDataPoolElement(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, IDataMarkerHandler dataHandler) {
        super(template, processors, projection);
        this.dataHandler = dataHandler;
    }

    @Override
    public void handleDataMarker(@Nonnull LevelAccessor level, StructureTemplate.StructureBlockInfo info, @Nonnull BlockPos pos, @Nonnull Rotation rotation, @Nonnull RandomSource random, BoundingBox box) {
        // IMPORTANT : The structure is very likely to generate on multiple chunks so multiple threads may call this method.
        // We want to only handle data markers that are in the bounding box (in the part of the structure that is being generated)
        if (!box.isInside(info.pos)) return;
        if (level instanceof WorldGenLevel) {
            this.dataHandler.handleDataMarker((WorldGenLevel) level, info.nbt.getString("metadata"), info.pos, info, pos, box, this, rotation, random);
        }
    }

    @Nonnull
    @Override
    public BoundingBox getBoundingBox(@Nonnull StructureTemplateManager manager, @Nonnull BlockPos pos, @Nonnull Rotation rotation) {
        return super.getBoundingBox(manager, pos, forcedRotation == null ? rotation : forcedRotation);
    }

    @Nonnull
    @Override
    protected StructurePlaceSettings getSettings(@Nonnull Rotation pRotation, @Nonnull BoundingBox pBoundingBox, boolean pKeepJigsaws) {
        return super.getSettings(forcedRotation == null ? pRotation : forcedRotation, pBoundingBox, pKeepJigsaws).popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    public SingleDataPoolElement setGroundLevelDelta(int groundLevelDelta) {
        this.groundLevelDelta = groundLevelDelta;
        return this;
    }

    public SingleDataPoolElement setRotation(Rotation rotation) {
        this.forcedRotation = rotation;
        return this;
    }

    @Override
    public int getGroundLevelDelta() {
        return groundLevelDelta;
    }

    @Nullable
    public Rotation getForcedRotation() {
        return forcedRotation;
    }

    @Nonnull
    @Override
    public StructurePoolElementType<?> getType() {
        return MineriaSPETypes.SINGLE_DATA.get();
    }

    @Nonnull
    @Override
    public String toString() {
        return "Mineria$SingleData[" + this.template + "]";
    }

    protected static ResourceLocation getDataHandlerId(SingleDataPoolElement element) {
        return MineriaRegistries.DATA_MARKER_HANDLERS.get().getKey(element.dataHandler);
    }

    protected static IDataMarkerHandler getDataHandler(ResourceLocation id) {
        return MineriaRegistries.DATA_MARKER_HANDLERS.get().getValue(id);
    }
}
