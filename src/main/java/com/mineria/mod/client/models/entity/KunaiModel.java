package com.mineria.mod.client.models.entity;

import com.mineria.mod.common.entity.KunaiEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * KunaiModel - LGatodu47
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class KunaiModel extends EntityModel<KunaiEntity>
{
    public ModelRenderer kunai;

    public KunaiModel()
    {
        this.texWidth = this.texHeight = 32;
        this.kunai = new ModelRenderer(this, 0, 0);
        this.kunai.setPos(0.0F, 0.0F, 0.0F);
        this.kunai.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(kunai, 0.0F, 0.0F, 3.154192653589793F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.kunai.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(KunaiEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
