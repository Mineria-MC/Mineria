package io.github.mineria_mc.mineria.client.renderers;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.ExtractorGearModel;
import io.github.mineria_mc.mineria.common.blocks.extractor.ExtractorBlock;
import io.github.mineria_mc.mineria.common.blocks.extractor.ExtractorTileEntity;
import io.github.mineria_mc.mineria.util.MineriaConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class ExtractorTileEntityRenderer implements BlockEntityRenderer<ExtractorTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/block/extractor_gear.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "extractor_gear"), "main");
    private final ExtractorGearModel gearModel;

    public ExtractorTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.gearModel = new ExtractorGearModel(ctx.bakeLayer(LAYER));
    }

    @Override
    public void render(ExtractorTileEntity tileEntity, float p_225616_2_, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        stack.pushPose();
        try {
            moveFromDirection(tileEntity.getBlockState().getValue(ExtractorBlock.FACING), stack);
        } catch (NullPointerException ignored) {
        }
        if (tileEntity.isExtracting() && MineriaConfig.CLIENT.enableTERAnimations.get()) {
            this.gearModel.animate();
        }
        this.gearModel.renderToBuffer(stack, buffer.getBuffer(this.gearModel.renderType(TEXTURE)), packedLight, packedOverlay, 1, 1, 1, 1);
        stack.popPose();
    }

    private static void moveFromDirection(Direction direction, PoseStack stack) {
        switch (direction) {
            case NORTH -> {
                stack.mulPose(Axis.YP.rotationDegrees(180));
                stack.translate(-0.5, -0.5, -0.5);
            }
            case EAST -> {
                stack.mulPose(Axis.YP.rotationDegrees(90));
                stack.translate(-0.5, -0.5, 0.5);
            }
            case WEST -> {
                stack.mulPose(Axis.YP.rotationDegrees(-90));
                stack.translate(0.5, -0.5, -0.5);
            }
            case SOUTH -> stack.translate(0.5, -0.5, 0.5);
        }
    }
}
