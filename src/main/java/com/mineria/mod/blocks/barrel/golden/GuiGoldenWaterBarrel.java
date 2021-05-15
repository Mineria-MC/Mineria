package com.mineria.mod.blocks.barrel.golden;

import com.mineria.mod.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGoldenWaterBarrel extends GuiContainer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/golden_water_barrel.png");
    private final InventoryPlayer playerInv;
    private final TileEntityGoldenWaterBarrel tile;

    public GuiGoldenWaterBarrel(InventoryPlayer playerInv, TileEntityGoldenWaterBarrel tile)
    {
        super(new ContainerGoldenWaterBarrel(playerInv, tile));
        this.playerInv = playerInv;
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 180;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String name = this.tile.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, (this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2), 5, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 112, 88, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft,  this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
