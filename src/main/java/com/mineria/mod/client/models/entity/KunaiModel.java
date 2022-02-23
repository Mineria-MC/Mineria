package com.mineria.mod.client.models.entity;

import com.mineria.mod.common.entity.KunaiEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * KunaiModel - LGatodu47
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class KunaiModel extends EntityModel<KunaiEntity>
{
    public ModelPart kunai;

    public KunaiModel(ModelPart root)
    {
        this.kunai = root.getChild("kunai");
    }

    public static LayerDefinition createLayerDefinition()
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("kunai", CubeListBuilder.create()
                .addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F),
                PartPose.rotation(0.0F, 0.0F, 3.154192653589793F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.kunai.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(KunaiEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }
}
