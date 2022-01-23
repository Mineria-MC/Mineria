package com.mineria.mod.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

/**
 * De-obfuscated version of {@link net.minecraft.client.gui.AbstractGui}. Purely used for documentation.
 */
public class DeobfAbstractGui
{
    private int blitOffset;

    public static void blit(MatrixStack stack, int x, int y, int blitOffset, int width, int height, TextureAtlasSprite sprite)
    {
        innerBlit(stack.last().pose(), x, x + width, y, y + height, blitOffset, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
    }

    public void blit(MatrixStack stack, int x, int y, int u, int v, int width, int height)
    {
        blit(stack, x, y, this.blitOffset, (float) u, (float) v, width, height, 256, 256);
    }

    public static void blit(MatrixStack stack, int x, int y, int blitOffset, float u, float v, int width, int height, int imageHeight, int imageWidth)
    {
        innerBlit(stack, x, x + width, y, y + height, blitOffset, width, height, u, v, imageWidth, imageHeight);
    }

    public static void blit(MatrixStack stack, int x, int y, int offsetX, int offsetY, float u, float v, int width, int height, int imageWidth, int imageHeight)
    {
        innerBlit(stack, x, x + offsetX, y, y + offsetY, 0, width, height, u, v, imageWidth, imageHeight);
    }

    public static void blit(MatrixStack stack, int x, int y, float u, float v, int width, int height, int imageWidth, int imageHeight)
    {
        blit(stack, x, y, width, height, u, v, width, height, imageWidth, imageHeight);
    }

    private static void innerBlit(MatrixStack stack, int x, int drawWidth, int y, int drawHeight, int blitOffset, int selectionWidth, int selectionHeight, float u, float v, int imageWidth, int imageHeight)
    {
        innerBlit(stack.last().pose(), x, drawWidth, y, drawHeight, blitOffset, (u + 0.0F) / (float) imageWidth, (u + (float) selectionWidth) / (float) imageWidth, (v + 0.0F) / (float) imageHeight, (v + (float) selectionHeight) / (float) imageHeight);
    }

    private static void innerBlit(Matrix4f pose, int x, int drawWidth, int y, int drawHeight, int blitOffset, float u, float u1, float v, float v1)
    {
        BufferBuilder builder = Tessellator.getInstance().getBuilder();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.vertex(pose, (float) x, (float) drawHeight, (float) blitOffset).uv(u, v1).endVertex();
        builder.vertex(pose, (float) drawWidth, (float) drawHeight, (float) blitOffset).uv(u1, v1).endVertex();
        builder.vertex(pose, (float) drawWidth, (float) y, (float) blitOffset).uv(u1, v).endVertex();
        builder.vertex(pose, (float) x, (float) y, (float) blitOffset).uv(u, v).endVertex();
        builder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(builder);
    }
}
