package io.github.mineria_mc.mineria.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ExtractorGearModel extends Model {
    private final ModelPart gear;

    public ExtractorGearModel(ModelPart root) {
        super(ExtractorGearModel::getRenderType);
        this.gear = root.getChild("gear");
    }

    public static LayerDefinition createLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition gear = root.addOrReplaceChild("gear", CubeListBuilder.create()
                        .texOffs(1, 1).addBox(-1.1F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, false)
                        .texOffs(1, 2).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 4.0F, false)
                        .texOffs(5, 1).addBox(-1.0F, -1.0F, 3.0F, 1.0F, 2.0F, 4.0F, false)
                        .texOffs(1, 3).addBox(-1.0F, -7.0F, -1.0F, 1.0F, 4.0F, 2.0F, false)
                        .texOffs(1, 0).addBox(-1.0F, 3.0F, -1.0F, 1.0F, 4.0F, 2.0F, false),
                PartPose.offset(8.0F, 16.0F, 0.0F));

        gear.addOrReplaceChild("part0", CubeListBuilder.create()
                        .texOffs(1, 0).addBox(-1.0F, 3.0F, 10.5F, 1.0F, 4.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, -8.0F, -8.0F, -0.7854F, 0.0F, 0.0F));

        gear.addOrReplaceChild("part1", CubeListBuilder.create()
                        .texOffs(3, 2).addBox(-1.0F, 3.0F, -12.5F, 1.0F, 4.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, -8.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

        gear.addOrReplaceChild("part2", CubeListBuilder.create()
                        .texOffs(4, 1).addBox(-1.0F, -7.0F, -12.5F, 1.0F, 4.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 8.0F, 8.0F, -0.7854F, 0.0F, 0.0F));

        gear.addOrReplaceChild("part3", CubeListBuilder.create()
                        .texOffs(1, 2).addBox(-1.0F, -7.0F, 10.5F, 1.0F, 4.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 8.0F, -8.0F, 0.7854F, 0.0F, 0.0F));


        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack stack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        gear.render(stack, buffer, packedLight, packedOverlay);
    }

    public void animate() {
        if (this.gear.xRot >= Math.PI * 2) {
            this.gear.xRot = 0;
        } else {
            this.gear.xRot += 0.002F;
        }
    }

    private static RenderType getRenderType(ResourceLocation location) {
        return RenderType.create("solid", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeSolidShader))
                        .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                        .setTextureState(new RenderStateShard.TextureStateShard(location, false, true))
                        .createCompositeState(true));
    }
}