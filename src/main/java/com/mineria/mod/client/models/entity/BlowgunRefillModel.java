package com.mineria.mod.client.models.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 3.9.3
public class BlowgunRefillModel extends EntityModel<Entity>
{
    private final ModelRenderer stick;
    private final ModelRenderer stick0;
    private final ModelRenderer stick1;
    private final ModelRenderer dart;
    private final ModelRenderer dart0;
    private final ModelRenderer dart1;

    public BlowgunRefillModel()
    {
        texWidth = 16;
        texHeight = 16;

        stick = new ModelRenderer(this);
        stick.setPos(-4.0F, -4.0F, 4.0F);


        stick1 = new ModelRenderer(this);
        stick1.setPos(0.0F, 5.0F, -3.0F);
        stick.addChild(stick1);
        setRotationAngle(stick1, 0.0F, 0.0F, -0.7854F);
        stick1.texOffs(0, 0).addBox(0.0F, -1.0F, 1.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);

        stick0 = new ModelRenderer(this);
        stick0.setPos(0.0F, 5.0F, -3.0F);
        stick.addChild(stick0);
        setRotationAngle(stick0, 0.0F, 0.0F, 0.7854F);
        stick0.texOffs(0, 0).addBox(0.0F, -1.0F, 1.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);

        dart = new ModelRenderer(this);
        dart.setPos(-4.0F, -4.0F, 4.0F);


        dart1 = new ModelRenderer(this);
        dart1.setPos(0.0F, 5.0F, -3.0F);
        dart.addChild(dart1);
        setRotationAngle(dart1, 0.0F, 0.0F, -0.7854F);
        dart1.texOffs(0, 0).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);

        dart0 = new ModelRenderer(this);
        dart0.setPos(0.0F, 5.0F, -3.0F);
        dart.addChild(dart0);
        setRotationAngle(dart0, 0.0F, 0.0F, 0.7854F);
        dart0.texOffs(0, 0).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTkics, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        stick.render(matrixStack, buffer, packedLight, packedOverlay);
        dart.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}