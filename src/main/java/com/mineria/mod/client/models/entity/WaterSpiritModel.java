package com.mineria.mod.client.models.entity;

import com.mineria.mod.common.entity.WaterSpiritEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WaterSpiritModel extends EntityModel<WaterSpiritEntity>
{
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer left_arm;
    private final ModelRenderer right_arm;

    public WaterSpiritModel()
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
    }

    @Override
    public void setupAnim(WaterSpiritEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        head.render(stack, buffer, packedLight, packedOverlay);
        body.render(stack, buffer, packedLight, packedOverlay);
        left_arm.render(stack, buffer, packedLight, packedOverlay);
        right_arm.render(stack, buffer, packedLight, packedOverlay);
    }
}
