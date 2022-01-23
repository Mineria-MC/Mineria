package com.mineria.mod.client.models.entity;


import com.mineria.mod.common.entity.AirSpiritEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

// Made with Blockbench 4.0.2
public class AirSpiritModel extends EntityModel<AirSpiritEntity>
{
    private boolean translucent;

    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer left_arm;
    private final ModelRenderer right_arm;
    private final ModelRenderer left_wing;
    private final ModelRenderer right_wing;

    public AirSpiritModel()
    {
        super(RenderType::entityTranslucent);
        texWidth = 64;
        texHeight = 64;

        head = new ModelRenderer(this);
        head.setPos(0.0F, 0.0F, 0.0F);
        head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.texOffs(24, 0).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 0.0F, 0.0F);
        body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 20.0F, 4.0F, 0.0F, false);

        left_arm = new ModelRenderer(this);
        left_arm.setPos(-5.0F, 2.0F, 0.0F);
        left_arm.texOffs(40, 16).addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

        right_arm = new ModelRenderer(this);
        right_arm.setPos(5.0F, 2.0F, 0.0F);
        right_arm.texOffs(40, 16).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        left_wing = new ModelRenderer(this);
        left_wing.setPos(0.0F, 0.0F, 2.0F);
        setRotationAngle(left_wing, 0.0F, -0.3491F, 0.0F);
        left_wing.texOffs(0, 42).addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F, 0.0F, true);

        right_wing = new ModelRenderer(this);
        right_wing.setPos(0.0F, 0.0F, 2.0F);
        setRotationAngle(right_wing, 0.0F, 0.3491F, 0.0F);
        right_wing.texOffs(0, 42).addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(AirSpiritEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void prepareMobModel(AirSpiritEntity entity, float p_212843_2_, float p_212843_3_, float p_212843_4_)
    {
        this.translucent = entity.isTranslucent();
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        left_arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        right_arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        left_wing.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        right_wing.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}