package io.github.mineria_mc.mineria.client.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class MineriaArmPoses {
    public static final HumanoidModel.ArmPose BLOWGUN = HumanoidModel.ArmPose.create("BLOWGUN", false, (model, entity, arm) -> {
        if(HumanoidArm.RIGHT.equals(arm)) {
            model.rightArm.setRotation(
                    Mth.clamp(model.head.xRot - 1.7123F - (entity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F),
                    model.head.yRot - 0.3892F,
                    model.head.zRot - 0.054F
            );
        }
        if(HumanoidArm.LEFT.equals(arm)) {
            model.leftArm.setRotation(
                    Mth.clamp(model.head.xRot - 1.7123F - (entity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F),
                    model.head.yRot + 0.3892F,
                    model.head.zRot - 0.054F
            );
        }
    });

    public static void init() {}
}
