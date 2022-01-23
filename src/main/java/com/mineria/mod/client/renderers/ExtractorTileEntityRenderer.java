package com.mineria.mod.client.renderers;

import com.mineria.mod.Mineria;
import com.mineria.mod.client.models.ExtractorGearModel;
import com.mineria.mod.common.blocks.extractor.ExtractorBlock;
import com.mineria.mod.common.blocks.extractor.ExtractorTileEntity;
import com.mineria.mod.util.MineriaConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ExtractorTileEntityRenderer extends TileEntityRenderer<ExtractorTileEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/block/extractor_gear.png");
    private final ExtractorGearModel gearModel = new ExtractorGearModel();

    public ExtractorTileEntityRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(ExtractorTileEntity tileEntity, float p_225616_2_, MatrixStack stack, IRenderTypeBuffer buffer, int p_225616_5_, int p_225616_6_)
    {
        stack.pushPose();
        try {
            moveFromDirection(tileEntity.getBlockState().getValue(ExtractorBlock.FACING), stack);
        } catch (NullPointerException ignored) {}
        if(tileEntity.isExtracting() && MineriaConfig.CLIENT.enableTERAnimations.get())
            this.gearModel.animate();
        this.gearModel.renderToBuffer(stack, buffer.getBuffer(this.gearModel.renderType(TEXTURE)), p_225616_5_, p_225616_6_, 1, 1, 1, 1);
        stack.popPose();
    }

    private static void moveFromDirection(Direction direction, MatrixStack stack)
    {
        switch (direction)
        {
            case NORTH:
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                stack.translate(-0.5, -0.5, -0.5);
                break;
            case EAST:
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                stack.translate(-0.5, -0.5, 0.5);
                break;
            case WEST:
                stack.mulPose(Vector3f.YP.rotationDegrees(-90));
                stack.translate(0.5, -0.5, -0.5);
                break;
            case SOUTH:
                stack.translate(0.5, -0.5, 0.5);
                break;
        }
    }
}
