package com.mineria.mod.client.models.entity;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.entity.DirtGolemEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 3.9.3
public class DirtGolemModel extends SegmentedModel<DirtGolemEntity>
{
    private final ModelRenderer body;
    private final ModelRenderer flower;
    private final ModelRenderer flower1;
    private final ModelRenderer flower2;
    private final ModelRenderer mushroom;
    private final ModelRenderer mushroom1;
    private final ModelRenderer mushroom2;
    private final ModelRenderer head;
    private final ModelRenderer right_arm;
    private final ModelRenderer left_arm;
    private final ModelRenderer right_leg;
    private final ModelRenderer left_leg;

    public DirtGolemModel()
    {
        texWidth = 128;
        texHeight = 128;

        body = new ModelRenderer(this);
        body.setPos(0.0F, -7.0F, 0.0F);
        body.texOffs(0, 40).addBox(-10.0F, -6.0F, -8.0F, 20.0F, 16.0F, 14.0F, 0.0F, false);
        body.texOffs(0, 70).addBox(-5.5F, 10.0F, -4.0F, 11.0F, 5.0F, 8.0F, 0.5F, false);

        flower = new ModelRenderer(this);
        flower.setPos(1.0F, 0.0F, 5.0F);
        body.addChild(flower);
        setRotationAngle(flower, 0.0F, 0.7854F, 0.0F);


        flower1 = new ModelRenderer(this);
        flower1.setPos(0.0F, 0.0F, 0.0F);
        flower.addChild(flower1);
        flower1.texOffs(1, 9).addBox(-1.0F, -18.0F, -14.0F, 0.0F, 12.0F, 13.0F, 0.0F, false);

        flower2 = new ModelRenderer(this);
        flower2.setPos(6.0F, 0.0F, -8.0F);
        flower1.addChild(flower2);
        setRotationAngle(flower2, 0.0F, 1.5708F, 0.0F);
        flower2.texOffs(1, 9).addBox(-1.0F, -18.0F, -13.5F, 0.0F, 12.0F, 13.0F, 0.0F, false);

        mushroom = new ModelRenderer(this);
        mushroom.setPos(0.0F, 1.0F, 0.0F);
        body.addChild(mushroom);
        setRotationAngle(mushroom, 0.0F, 0.3054F, 0.0F);


        mushroom1 = new ModelRenderer(this);
        mushroom1.setPos(4.0F, 0.0F, -10.5F);
        mushroom.addChild(mushroom1);
        setRotationAngle(mushroom1, 0.0F, -1.5708F, 0.0F);
        mushroom1.texOffs(32, 25).addBox(7.0F, -13.0F, -7.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);

        mushroom2 = new ModelRenderer(this);
        mushroom2.setPos(0.0F, 0.0F, 0.0F);
        mushroom.addChild(mushroom2);
        mushroom2.texOffs(32, 25).addBox(7.0F, -13.0F, -7.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 0.0F, -10.0F);
        head.texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);
        head.texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        right_arm = new ModelRenderer(this);
        right_arm.setPos(0.0F, -7.0F, 0.0F);
        right_arm.texOffs(81, 33).addBox(-15.0F, -2.5F, -3.0F, 6.0F, 26.0F, 7.0F, 0.0F, false);

        left_arm = new ModelRenderer(this);
        left_arm.setPos(0.0F, -7.0F, 0.0F);
        left_arm.texOffs(83, 0).addBox(10.0F, -2.5F, -3.0F, 6.0F, 26.0F, 7.0F, 0.0F, false);

        right_leg = new ModelRenderer(this);
        right_leg.setPos(4.0F, 11.0F, 0.0F);
        right_leg.texOffs(37, 0).addBox(-11.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F, false);

        left_leg = new ModelRenderer(this);
        left_leg.setPos(-5.0F, 11.0F, 0.0F);
        left_leg.texOffs(60, 0).addBox(6.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelRenderer> parts()
    {
        return ImmutableList.of(body, head, right_arm, left_arm, right_leg, left_leg);
    }

    @Override
    public void setupAnim(DirtGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.right_leg.xRot = -1.5F * MathHelper.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.left_leg.xRot = 1.5F * MathHelper.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.right_arm.yRot = 0.0F;
        this.left_arm.yRot = 0.0F;
    }

    @Override
    public void prepareMobModel(DirtGolemEntity entity, float limbSwing, float limbSwingAmount, float p_212843_4_)
    {
        this.right_arm.xRot = (-0.2F + 1.5F * MathHelper.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        this.left_arm.xRot = (-0.2F - 1.5F * MathHelper.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}