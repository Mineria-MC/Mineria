package io.github.mineria_mc.mineria.client.models.entity;

import io.github.mineria_mc.mineria.common.entity.BrownBearEntity;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;

public class BrownBearModel extends QuadrupedModel<BrownBearEntity> {
    public BrownBearModel(ModelPart root) {
        super(root, true, 16.0F, 4.0F, 2.25F, 2.0F, 24);
    }

    @Override
    public void setupAnim(BrownBearEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(entity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        float f = pAgeInTicks - (float) entity.tickCount;
        float f1 = entity.getStandingAnimationScale(f);
        f1 = f1 * f1;
        float f2 = 1.0F - f1;
        this.body.xRot = ((float) Math.PI / 2F) - f1 * (float) Math.PI * 0.35F;
        this.body.y = 9.0F * f2 + 11.0F * f1;
        this.rightFrontLeg.y = 14.0F * f2 - 6.0F * f1;
        this.rightFrontLeg.z = -8.0F * f2 - 4.0F * f1;
        this.rightFrontLeg.xRot -= f1 * (float) Math.PI * 0.45F;
        this.leftFrontLeg.y = this.rightFrontLeg.y;
        this.leftFrontLeg.z = this.rightFrontLeg.z;
        this.leftFrontLeg.xRot -= f1 * (float) Math.PI * 0.45F;
        if (this.young) {
            this.head.y = 10.0F * f2 - 9.0F * f1;
            this.head.z = -16.0F * f2 - 7.0F * f1;
        } else {
            this.head.y = 10.0F * f2 - 14.0F * f1;
            this.head.z = -16.0F * f2 - 3.0F * f1;
        }

        this.head.xRot += f1 * (float) Math.PI * 0.15F;
    }
}
