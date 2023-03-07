package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumScreen;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public abstract class PartitionedPage extends ApothecariumPage {
    private List<RenderPart> renderParts;

    public PartitionedPage(PageCreationContext ctx) {
        super(ctx);
    }

    protected abstract void initParts(List<RenderPart> parts);

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x) {
        if(renderParts == null) {
            renderParts = ImmutableList.copyOf(Util.make(new ArrayList<>(), this::initParts));
        }
        this.renderParts.forEach(renderPart -> renderPart.render(stack, mouseX, mouseY, partialTicks, x));
    }

    protected PartialRenderedPart scaledText(Component text, float y, float scale) {
        return scaledText(text.getVisualOrderText(), y, scale);
    }

    protected PartialRenderedPart scaledText(FormattedCharSequence seq, float y, float scale) {
        return scaledText(seq, FloatUnaryOperator.identity(), y, scale, 0);
    }

    protected PartialRenderedPart scaledText(FormattedCharSequence seq, FloatUnaryOperator xTransformer, float y, float scale) {
        return scaledText(seq, xTransformer, y, scale, 0);
    }

    protected PartialRenderedPart scaledText(FormattedCharSequence seq, float y, float scale, int color) {
        return scaledText(seq, FloatUnaryOperator.identity(), y, scale, color);
    }

    protected PartialRenderedPart scaledText(FormattedCharSequence seq, FloatUnaryOperator xTransformer, float y, float scale, int color) {
        return (stack, x) -> {
            stack.pushPose();
            stack.translate(xTransformer.apply(x), y, 0);
            stack.scale(scale, scale, 1);
            font.draw(stack, seq, 0, 0, color);
            stack.popPose();
        };
    }

    protected static RenderPart partial(PartialRenderedPart part) {
        return part;
    }

    public static void renderItemWithSlotBackground(Minecraft client, PoseStack poseStack, int mouseX, int mouseY, ItemStack stack, float x, float y, float size, boolean drawSlot, ApothecariumScreen parentScreen) {
        if(drawSlot) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, ApothecariumScreen.BOOK_TEXTURE);
            GuiComponent.blit(poseStack, Mth.floor(x), Mth.floor(y), Mth.ceil(size), Mth.ceil(size), 341, 0, 18, 18, 512, 512);
        }

        float stackSize = 16 * size / 18;
        float stackX = x + (size - stackSize) / 2;
        float stackY = y + (size - stackSize) / 2;

        ItemRenderer itemRenderer = client.getItemRenderer();
        BakedModel model = itemRenderer.getModel(stack, null, null, 0);

        client.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.translate(stackX, stackY, 100.0F + itemRenderer.blitOffset);
        modelView.translate(stackSize / 2f, stackSize / 2f, 0);
        modelView.scale(1, -1, 1);
        modelView.scale(stackSize, stackSize, stackSize);
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean blockLight = !model.usesBlockLight();
        if (blockLight) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, new PoseStack(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        if (blockLight) {
            Lighting.setupFor3DItems();
        }

        modelView.popPose();
        RenderSystem.applyModelViewMatrix();

        if (stack.getCount() != 1) {
            PoseStack poseStack1 = new PoseStack();
            String count = String.valueOf(stack.getCount());
            float fontScale = stackSize / 16;
            poseStack1.translate(Math.floor(x + size - client.font.width(count) * fontScale), Math.floor(y + size - client.font.lineHeight * fontScale), itemRenderer.blitOffset + 200);
            poseStack1.scale(fontScale, fontScale, 1);
            MultiBufferSource.BufferSource bufferSource1 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            client.font.drawInBatch(count, 0, 0, 16777215, true, poseStack1.last().pose(), bufferSource1, false, 0, 15728880);
            bufferSource1.endBatch();
        }

        if(mouseX > x && mouseX < x + size && mouseY > y && mouseY < y + size) {
            RenderSystem.disableDepthTest();
            RenderSystem.colorMask(true, true, true, false);
            ApothecariumScreen.fillGradient(poseStack, Mth.floor(x), Mth.floor(y), Mth.ceil(x + size), Mth.ceil(y + size), -2130706433, -2130706433, 0);
            RenderSystem.colorMask(true, true, true, true);
            parentScreen.renderTooltip(poseStack, parentScreen.getTooltipFromItem(stack), stack.getTooltipImage(), mouseX, mouseY);
            RenderSystem.enableDepthTest();
        }
    }

    @SuppressWarnings("deprecation")
    public static void renderEntity(Entity entity, float x, float y, float scale) {
        PoseStack view = RenderSystem.getModelViewStack();
        view.pushPose();
        view.translate(x, y, 1050F);
        view.scale(1, 1, -1);
        RenderSystem.applyModelViewMatrix();

        PoseStack stack = new PoseStack();
        Quaternionf xQuaternion = new Quaternionf().rotateX((float) Math.toRadians(-45) * 20F * ((float) Math.PI / 180F));
        Quaternionf zQuaternion = new Quaternionf().rotateZ((float) Math.PI).mul(xQuaternion);
        stack.translate(0, scale * entity.getDimensions(entity.getPose()).height, 1000);
        stack.scale(scale, scale, scale);
        stack.mulPose(zQuaternion);
        Lighting.setupForEntityInInventory();

        EntityRenderDispatcher renderer = Minecraft.getInstance().getEntityRenderDispatcher();
        xQuaternion.conjugate();
        renderer.overrideCameraOrientation(xQuaternion);
        renderer.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> renderer.render(entity, 0, 0, 0, 0, 1, stack, bufferSource, 15728880));
        bufferSource.endBatch();
        renderer.setRenderShadow(true);

        view.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @FunctionalInterface
    protected interface PartialRenderedPart extends RenderPart {
        void render(PoseStack stack, int x);

        @Override
        default void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x) {
            render(stack, x);
        }
    }

    @FunctionalInterface
    protected interface RenderPart {
        void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x);
    }
}
