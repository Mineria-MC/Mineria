package com.mineria.mod.blocks.infuser;

import com.mineria.mod.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInfuser extends GuiContainer
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/infuser.png");
    private final InventoryPlayer player;
    private final TileEntityInfuser tile;

    public GuiInfuser(InventoryPlayer player, TileEntityInfuser tile)
    {
        super(new ContainerInfuser(player, tile));
        this.tile = tile;
        this.player = player;
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
        String s = this.tile.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, (this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2 + 20), 5, 4210752);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (TileEntityInfuser.isInfusing(this.tile))
        {
            int burnAnimation = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(this.guiLeft + 149, this.guiTop + 36 + 12 - burnAnimation, 202, 12 - burnAnimation, 14, burnAnimation + 1);
        }

        int infuseAnimation = this.getInfuseProgressScaled(26);
        this.drawTexturedModalRect(this.guiLeft + 64, this.guiTop + 36, 176, 0, infuseAnimation + 1, 13);
    }

    private int getInfuseProgressScaled(int pixels)
    {
        int infuseTime = this.tile.getField(2);
        int totalInfuseTime = this.tile.getField(3);
        return totalInfuseTime != 0 && infuseTime != 0 ? infuseTime * pixels / totalInfuseTime : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int currentBurnTime = this.tile.getField(1);

        if (currentBurnTime == 0)
        {
            currentBurnTime = 2400;
        }

        return this.tile.getField(0) * pixels / currentBurnTime;
    }
}
