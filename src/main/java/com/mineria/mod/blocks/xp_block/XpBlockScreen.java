package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.References;
import com.mineria.mod.network.GuiButtonPressedMessageHandler;
import com.mineria.mod.util.MineriaPacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class XpBlockScreen extends ContainerScreen<XpBlockContainer>
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/xp_block_gui.png");

    public XpBlockScreen(XpBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.xSize = 178;
        this.ySize = 139;
        this.titleX = 28;
        this.titleY = 6;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.ySize - 96 + 2;
    }

    @Override
    protected void init()
    {
        super.init();
        this.addButton(new Button(this.guiLeft + 21, this.guiTop + 19, 78, 20, new TranslationTextComponent("screen.mineria.xp_block.give_xp"), button -> {
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(new GuiButtonPressedMessageHandler.GuiButtonPressedMessage(this.container.getTileEntityPos(), 0));
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
