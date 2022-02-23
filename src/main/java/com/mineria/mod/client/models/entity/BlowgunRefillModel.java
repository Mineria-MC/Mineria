package com.mineria.mod.client.models.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 3.9.3
public class BlowgunRefillModel extends EntityModel<Entity>
{
    private final ModelPart stick;
//    private final ModelPart stick0;
//    private final ModelPart stick1;
    private final ModelPart dart;
//    private final ModelPart dart0;
//    private final ModelPart dart1;

    public BlowgunRefillModel(ModelPart root)
    {
        this.stick = root.getChild("stick");
        this.dart = root.getChild("dart");
    }

    public static LayerDefinition createLayerDefinition()
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition stick = root.addOrReplaceChild("stick", CubeListBuilder.create(), PartPose.offset(-4.0F, -4.0F, 4.0F));
        stick.addOrReplaceChild("stick0", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, -1.0F, 1.0F, 0.0F, 2.0F, 5.0F, false),
                PartPose.offsetAndRotation(0.0F, 5.0F, -3.0F, 0.0F, 0.0F, -0.7854F));
        stick.addOrReplaceChild("stick1", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, -1.0F, 1.0F, 0.0F, 2.0F, 5.0F, false),
                PartPose.offsetAndRotation(0.0F, 5.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition dart = root.addOrReplaceChild("dart", CubeListBuilder.create(), PartPose.offset(-4.0F, -4.0F, 4.0F));
        dart.addOrReplaceChild("dart0", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 5.0F, -3.0F, 0.0F, 0.0F, -0.7854F));
        dart.addOrReplaceChild("dart1", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 5.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTkics, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        stick.render(matrixStack, buffer, packedLight, packedOverlay);
        dart.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}