package com.mineria.mod.client.models.entity;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.entity.GreatDruidOfGaulsEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class GreatDruidOfGaulsModel extends SegmentedModel<GreatDruidOfGaulsEntity> implements IHasArm, IHasHead
{
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer body;
    private final ModelRenderer arms;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;

    public GreatDruidOfGaulsModel(float texStart, float yOffset, int texSizeX, int texSizeY)
    {
        this.head = (new ModelRenderer(this)).setTexSize(texSizeX, texSizeY);
        this.head.setPos(0.0F, 0.0F + yOffset, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, texStart);
        this.hat = (new ModelRenderer(this, 32, 0)).setTexSize(texSizeX, texSizeY);
        this.hat.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, texStart + 0.45F);
        this.head.addChild(this.hat);
        ModelRenderer modelrenderer = (new ModelRenderer(this)).setTexSize(texSizeX, texSizeY);
        modelrenderer.setPos(0.0F, yOffset - 2.0F, 0.0F);
        modelrenderer.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, texStart);
        this.head.addChild(modelrenderer);
        this.body = (new ModelRenderer(this)).setTexSize(texSizeX, texSizeY);
        this.body.setPos(0.0F, 0.0F + yOffset, 0.0F);
        this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, texStart);
        this.body.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, texStart + 0.5F);
        this.arms = (new ModelRenderer(this)).setTexSize(texSizeX, texSizeY);
        this.arms.setPos(0.0F, 0.0F + yOffset + 2.0F, 0.0F);
        this.arms.texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, texStart);
        ModelRenderer modelrenderer1 = (new ModelRenderer(this, 44, 22)).setTexSize(texSizeX, texSizeY);
        modelrenderer1.mirror = true;
        modelrenderer1.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, texStart);
        this.arms.addChild(modelrenderer1);
        this.arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, texStart);
        this.leftLeg = (new ModelRenderer(this, 0, 22)).setTexSize(texSizeX, texSizeY);
        this.leftLeg.setPos(-2.0F, 12.0F + yOffset, 0.0F);
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, texStart);
        this.rightLeg = (new ModelRenderer(this, 0, 22)).setTexSize(texSizeX, texSizeY);
        this.rightLeg.mirror = true;
        this.rightLeg.setPos(2.0F, 12.0F + yOffset, 0.0F);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, texStart);
        this.rightArm = (new ModelRenderer(this, 40, 46)).setTexSize(texSizeX, texSizeY);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, texStart);
        this.rightArm.setPos(-5.0F, 2.0F + yOffset, 0.0F);
        this.leftArm = (new ModelRenderer(this, 40, 46)).setTexSize(texSizeX, texSizeY);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, texStart);
        this.leftArm.setPos(5.0F, 2.0F + yOffset, 0.0F);
    }

    @Override
    public Iterable<ModelRenderer> parts()
    {
        return ImmutableList.of(this.head, this.body, this.leftLeg, this.rightLeg, this.arms, this.rightArm, this.leftArm);
    }

    @Override
    public void setupAnim(GreatDruidOfGaulsEntity entity, float p1, float p2, float p3, float p4, float p5)
    {
        this.head.yRot = p4 * ((float) Math.PI / 180F);
        this.head.xRot = p5 * ((float) Math.PI / 180F);
        this.arms.y = 3.0F;
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
        if (this.riding)
        {
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = ((float) Math.PI / 10F);
            this.leftLeg.zRot = 0.07853982F;
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = (-(float) Math.PI / 10F);
            this.rightLeg.zRot = -0.07853982F;
        } else
        {
            this.leftLeg.xRot = MathHelper.cos(p1 * 0.6662F) * 1.4F * p2 * 0.5F;
            this.leftLeg.yRot = 0.0F;
            this.leftLeg.zRot = 0.0F;
            this.rightLeg.xRot = MathHelper.cos(p1 * 0.6662F + (float) Math.PI) * 1.4F * p2 * 0.5F;
            this.rightLeg.yRot = 0.0F;
            this.rightLeg.zRot = 0.0F;
        }

        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        this.rightArm.xRot = MathHelper.cos(p3 * 0.6662F) * 0.25F;
        this.leftArm.xRot = MathHelper.cos(p3 * 0.6662F) * 0.25F;
        this.rightArm.zRot = 2.3561945F;
        this.leftArm.zRot = -2.3561945F;
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;

        this.arms.visible = false;
        this.leftArm.visible = true;
        this.rightArm.visible = true;
    }

    private ModelRenderer getArm(HandSide handSide)
    {
        return handSide == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelRenderer getHat()
    {
        return this.hat;
    }

    @Override
    public ModelRenderer getHead()
    {
        return this.head;
    }

    @Override
    public void translateToHand(HandSide side, MatrixStack stack)
    {
        this.getArm(side).translateAndRotate(stack);
    }
}
