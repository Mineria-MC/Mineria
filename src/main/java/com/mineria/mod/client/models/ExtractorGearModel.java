package com.mineria.mod.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class ExtractorGearModel extends Model
{
    private final ModelRenderer gear;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;

    public ExtractorGearModel()
    {
        super(ExtractorGearModel::getRenderType);
        texWidth = 16;
        texHeight = 16;

        gear = new ModelRenderer(this);
        gear.setPos(8.0F, 16.0F, 0.0F);
        gear.texOffs(1, 1).addBox(-1.1F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
        gear.texOffs(1, 2).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        gear.texOffs(5, 1).addBox(-1.0F, -1.0F, 3.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        gear.texOffs(1, 3).addBox(-1.0F, -7.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        gear.texOffs(1, 0).addBox(-1.0F, 3.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, -8.0F, -8.0F);
        gear.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
        cube_r1.texOffs(1, 0).addBox(-1.0F, 3.0F, 10.5F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, -8.0F, 8.0F);
        gear.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.7854F, 0.0F, 0.0F);
        cube_r2.texOffs(3, 2).addBox(-1.0F, 3.0F, -12.5F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.0F, 8.0F, 8.0F);
        gear.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.7854F, 0.0F, 0.0F);
        cube_r3.texOffs(4, 1).addBox(-1.0F, -7.0F, -12.5F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(0.0F, 8.0F, -8.0F);
        gear.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.7854F, 0.0F, 0.0F);
        cube_r4.texOffs(1, 2).addBox(-1.0F, -7.0F, 10.5F, 1.0F, 4.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder buffer, int p_225598_3_, int p_225598_4_, float red, float green, float blue, float alpha)
    {
        gear.render(stack, buffer, p_225598_3_, p_225598_4_);
    }

    public void animate()
    {
        if(this.gear.xRot >= Math.PI * 2)
            this.gear.xRot = 0;
        else this.gear.xRot += 0.002F;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    private static RenderType getRenderType(ResourceLocation location)
    {
        return RenderType.create("solid", DefaultVertexFormats.BLOCK, 7, 256, true, false,
                RenderType.State.builder()
                        .setShadeModelState(new RenderState.ShadeModelState(true))
                        .setLightmapState(new RenderState.LightmapState(true))
                        .setTextureState(new RenderState.TextureState(location, false, true))
                        .createCompositeState(true));
    }
}