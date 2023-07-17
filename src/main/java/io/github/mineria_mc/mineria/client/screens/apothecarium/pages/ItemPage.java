package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemPage extends ApothecariumPage {
    private final Component title;
    private final ImmutableList<ItemStack> renderStacks;

    public ItemPage(PageCreationContext ctx, Component title, ItemStack stack) {
        this(ctx, title, List.of(stack));
    }

    public ItemPage(PageCreationContext ctx, Component title, List<ItemStack> renderStacks) {
        super(ctx);
        this.title = title;
        this.renderStacks = ImmutableList.copyOf(renderStacks);
    }

    private float time;

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int x) {
        PoseStack stack = graphics.pose();

        stack.pushPose();
        float scaleFactor = findFittingScale(title, height / 140f, width / 16f);
        stack.translate(x + (width - font.width(this.title) * scaleFactor) / 2f, y, 0);
        stack.scale(scaleFactor, scaleFactor, 1);
        font.draw(graphics, this.title, 0, 0, 0);
        stack.popPose();

        if(renderStacks.isEmpty()) {
            return;
        }

        float itemX = x + width / 5f;
        float itemY = y + (height - width * 3 / 5f) / 2f;
        float itemSize = width * 3 / 5f;
        if(renderStacks.size() == 1) {
            renderGuiItem(client, renderStacks.get(0), itemX, itemY, itemSize);
        } else {
            if(!Screen.hasControlDown()) {
                time += partialTicks;
            }
            int index = Mth.floor(this.time / 30.0F) % renderStacks.size();
            renderGuiItem(client, renderStacks.get(index), itemX, itemY, itemSize);
        }
    }

    public static void renderGuiItem(Minecraft client, ItemStack stack, float x, float y, float size) {
        ItemRenderer itemRenderer = client.getItemRenderer();
        BakedModel model = itemRenderer.getModel(stack, null, null, 0);

        client.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.translate(x, y, 100.0F);
        modelView.translate(size / 2f, size / 2f, 0.0F);
        modelView.scale(1, -1, 1);
        modelView.scale(size, size, size);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean blockLight = !model.usesBlockLight();
        if (blockLight) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(stack, ItemDisplayContext.GUI, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        if (blockLight) {
            Lighting.setupFor3DItems();
        }

        modelView.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
