package io.github.mineria_mc.mineria.client.models.entity;

import io.github.mineria_mc.mineria.common.entity.WaterSpiritEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

import javax.annotation.Nonnull;

public class WaterSpiritModel extends EntityModel<WaterSpiritEntity> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public WaterSpiritModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("leftArm");
        this.rightArm = root.getChild("rightArm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8)
                        .texOffs(24, 0).addBox(-1, -3, -5, 2, 4, 1)
                        .texOffs(29, -3).addBox(4, -8, 4, 0, 8, 3)
                        .texOffs(29, -3).addBox(-4, -8, 4, 0, 8, 3)
                        .texOffs(29, 13).addBox(-4, -8, 4, 8, 0, 3),
                PartPose.offset(0, 0, 0)
        );

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4, 0, -2, 8, 20, 4, new CubeDeformation(0)), PartPose.offset(0, 0, 0));

        PartDefinition left_arm = root.addOrReplaceChild("leftArm", CubeListBuilder.create()
                        .texOffs(40, 16).mirror().addBox(9, -2, -2, 4, 12, 4).mirror(false)
                        .texOffs(56, 48).mirror().addBox(13, -2, 2, 0, 12, 4).mirror(false),
                PartPose.offset(-5, 2, 0)
        );

        PartDefinition right_arm = root.addOrReplaceChild("rightArm", CubeListBuilder.create()
                        .texOffs(40, 16).addBox(-13, -2, -2, 4, 12, 4)
                        .texOffs(48, 48).addBox(-13, -2, 2, 0, 12, 4),
                PartPose.offset(5, 2, 0)
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(WaterSpiritEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack stack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(stack, buffer, packedLight, packedOverlay);
        body.render(stack, buffer, packedLight, packedOverlay);
        leftArm.render(stack, buffer, packedLight, packedOverlay);
        rightArm.render(stack, buffer, packedLight, packedOverlay);
    }
}
