package com.mineria.mod.client.renderers;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.ExtractorGearModel;
import com.mineria.mod.common.blocks.extractor.ExtractorBlock;
import com.mineria.mod.common.blocks.extractor.ExtractorTileEntity;
import com.mineria.mod.util.MineriaConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public class ExtractorTileEntityRenderer implements BlockEntityRenderer<ExtractorTileEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/block/extractor_gear.png");
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "extractor_gear"), "main");
    private final ExtractorGearModel gearModel;

    public ExtractorTileEntityRenderer(BlockEntityRendererProvider.Context ctx)
    {
        this.gearModel = new ExtractorGearModel(ctx.bakeLayer(LAYER));
    }

    @Override
    public void render(ExtractorTileEntity tileEntity, float p_225616_2_, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        stack.pushPose();
        try {
            moveFromDirection(tileEntity.getBlockState().getValue(ExtractorBlock.FACING), stack);
        } catch (NullPointerException ignored) {}
        if(tileEntity.isExtracting() && MineriaConfig.CLIENT.enableTERAnimations.get())
            this.gearModel.animate();
        this.gearModel.renderToBuffer(stack, buffer.getBuffer(this.gearModel.renderType(TEXTURE)), packedLight, packedOverlay, 1, 1, 1, 1);
        stack.popPose();
    }

    private static void moveFromDirection(Direction direction, PoseStack stack)
    {
        switch (direction)
        {
            case NORTH -> {
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                stack.translate(-0.5, -0.5, -0.5);
            }
            case EAST -> {
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                stack.translate(-0.5, -0.5, 0.5);
            }
            case WEST -> {
                stack.mulPose(Vector3f.YP.rotationDegrees(-90));
                stack.translate(0.5, -0.5, -0.5);
            }
            case SOUTH -> stack.translate(0.5, -0.5, 0.5);
        }
    }
}
