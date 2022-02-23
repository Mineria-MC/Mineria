package com.mineria.mod.client.models.entity;

import com.mineria.mod.common.entity.DruidicWolfEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class DruidicWolfModel extends EntityModel<DruidicWolfEntity>
{
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;
    private final ModelPart upperBody;

    public DruidicWolfModel(ModelPart root)
    {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.upperBody = root.getChild("upper_body");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.tail = root.getChild("tail");
    }

    @Override
    public void setupAnim(DruidicWolfEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
    {
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.tail.xRot = pAgeInTicks;
    }

    @Override
    public void prepareMobModel(DruidicWolfEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick)
    {
        this.body.setPos(0.0F, 14.0F, 2.0F);
        this.body.xRot = ((float)Math.PI / 2F);
        this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
        this.upperBody.xRot = this.body.xRot;
        this.tail.setPos(-1.0F, 12.0F, 8.0F);
        this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
        this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
        this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
        this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
        this.rightHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer builder, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha)
    {
        this.head.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.body.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.rightHindLeg.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.leftHindLeg.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.rightFrontLeg.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.leftFrontLeg.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.upperBody.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        this.tail.render(poseStack, builder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
