package com.mineria.mod.client.models.entity;


import com.mineria.mod.common.entity.AirSpiritEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

// Made with Blockbench 4.0.2
public class AirSpiritModel extends EntityModel<AirSpiritEntity>
{
    private boolean translucent;

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public AirSpiritModel(ModelPart root)
    {
        super(RenderType::entityTranslucent);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("leftArm");
        this.rightArm = root.getChild("rightArm");
        this.leftWing = root.getChild("leftWing");
        this.rightWing = root.getChild("rightWing");
    }

    public static LayerDefinition createBody()
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, false)
                .texOffs(24, 0)
                .addBox(-1.0F, -3.0F, -5.0F, 2.0F, 4.0F, 1.0F, false), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 16)
                .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 20.0F, 4.0F, false), PartPose.ZERO);
        PartDefinition leftArm = root.addOrReplaceChild("leftArm", CubeListBuilder.create()
                .texOffs(40, 16)
                .addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true), PartPose.offset(-5.0F, 2.0F, 0.0F));
        PartDefinition rightArm = root.addOrReplaceChild("rightArm", CubeListBuilder.create()
                .texOffs(40, 16)
                .addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, false), PartPose.offset(5.0F, 2.0F, 0.0F));
        PartDefinition leftWing = root.addOrReplaceChild("leftWing", CubeListBuilder.create()
                .texOffs(0, 42)
                .addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F, true),
                PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, -0.3491F, 0.0F));
        PartDefinition rightWing = root.addOrReplaceChild("rightWing", CubeListBuilder.create()
                .texOffs(0, 42)
                .addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.3491F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
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
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        leftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        rightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        leftWing.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
        rightWing.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, this.translucent ? 0.1F : 1.0F);
    }
}