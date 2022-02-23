package com.mineria.mod.client.models.entity;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.entity.DirtGolemEntity;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// Made with Blockbench 3.9.3
public class DirtGolemModel extends ListModel<DirtGolemEntity>
{
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public DirtGolemModel(ModelPart root)
    {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-10.0F, -6.0F, -8.0F, 20.0F, 16.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 70).addBox(-5.5F, 10.0F, -4.0F, 11.0F, 5.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition flower = body.addOrReplaceChild("flower", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.0F, 5.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition flower1 = flower.addOrReplaceChild("flower1", CubeListBuilder.create().texOffs(1, 9).addBox(-1.0F, -18.0F, -14.0F, 0.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition flower2 = flower1.addOrReplaceChild("flower2", CubeListBuilder.create().texOffs(1, 9).addBox(-1.0F, -18.0F, -13.5F, 0.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, -8.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition mushroom = body.addOrReplaceChild("mushroom", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.3054F, 0.0F));

        PartDefinition mushroom1 = mushroom.addOrReplaceChild("mushroom1", CubeListBuilder.create().texOffs(32, 25).addBox(7.0F, -13.0F, -7.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -10.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition mushroom2 = mushroom.addOrReplaceChild("mushroom2", CubeListBuilder.create().texOffs(32, 25).addBox(7.0F, -13.0F, -7.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -10.0F));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(81, 33).addBox(-15.0F, -2.5F, -3.0F, 6.0F, 26.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(83, 0).addBox(10.0F, -2.5F, -3.0F, 6.0F, 26.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(37, 0).addBox(-11.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 11.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0).addBox(6.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 11.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public Iterable<ModelPart> parts()
    {
        return ImmutableList.of(body, head, right_arm, left_arm, right_leg, left_leg);
    }

    @Override
    public void setupAnim(DirtGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.right_leg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.left_leg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.right_arm.yRot = 0.0F;
        this.left_arm.yRot = 0.0F;
    }

    @Override
    public void prepareMobModel(DirtGolemEntity entity, float limbSwing, float limbSwingAmount, float p_212843_4_)
    {
        this.right_arm.xRot = (-0.2F + 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        this.left_arm.xRot = (-0.2F - 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
    }


}