package io.github.mineria_mc.mineria.client.models.entity;

import io.github.mineria_mc.mineria.common.entity.GreatDruidOfGaulsEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class GreatDruidOfGaulsModel extends HierarchicalModel<GreatDruidOfGaulsEntity> implements ArmedModel, HeadedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart arms;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public GreatDruidOfGaulsModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.hat = this.head.getChild("hat");
        this.arms = root.getChild("arms");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(GreatDruidOfGaulsEntity entity, float p1, float p2, float p3, float p4, float p5) {
        this.head.yRot = p4 * ((float) Math.PI / 180F);
        this.head.xRot = p5 * ((float) Math.PI / 180F);
        this.arms.y = 3.0F;
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
        if (this.riding) {
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = ((float) Math.PI / 10F);
            this.leftLeg.zRot = 0.07853982F;
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = (-(float) Math.PI / 10F);
            this.rightLeg.zRot = -0.07853982F;
        } else {
            this.leftLeg.xRot = Mth.cos(p1 * 0.6662F) * 1.4F * p2 * 0.5F;
            this.leftLeg.yRot = 0.0F;
            this.leftLeg.zRot = 0.0F;
            this.rightLeg.xRot = Mth.cos(p1 * 0.6662F + (float) Math.PI) * 1.4F * p2 * 0.5F;
            this.rightLeg.yRot = 0.0F;
            this.rightLeg.zRot = 0.0F;
        }

        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        this.rightArm.xRot = Mth.cos(p3 * 0.6662F) * 0.25F;
        this.leftArm.xRot = Mth.cos(p3 * 0.6662F) * 0.25F;
        this.rightArm.zRot = 2.3561945F;
        this.leftArm.zRot = -2.3561945F;
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;

        this.arms.visible = false;
        this.leftArm.visible = true;
        this.rightArm.visible = true;
    }

    private ModelPart getArm(HumanoidArm handSide) {
        return handSide == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelPart getHat() {
        return this.hat;
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        this.getArm(side).translateAndRotate(stack);
    }
}
