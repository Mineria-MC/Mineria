package io.github.mineria_mc.mineria.client.models;

import io.github.mineria_mc.mineria.Mineria;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BlowgunModel extends Model {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(Mineria.MODID, "bamboo_blowgun"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/item/blowgun_model.png");

    private final ModelPart main;

    public BlowgunModel(ModelPart main) {
        super(RenderType::entitySolid);
        this.main = main;
    }

    public static LayerDefinition createLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-1, -26, -16, 2, 2, 12, new CubeDeformation(0.0F)), PartPose.ZERO);

        return LayerDefinition.create(mesh, 32, 32);
    }

    public RenderType getDefaultRenderType() {
        return renderType(TEXTURE);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.main.render(stack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}