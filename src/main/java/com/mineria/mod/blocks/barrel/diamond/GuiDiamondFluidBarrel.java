package com.mineria.mod.blocks.barrel.diamond;

import com.mineria.mod.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/** Not finished
 * We currently have issues with the diamond fluid barrel, so it'll be released later.
 */
public class GuiDiamondFluidBarrel extends GuiContainer
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/diamond_fluid_barrel.png");
    private final TileEntityDiamondFluidBarrel tile;
    private final InventoryPlayer playerInv;

    public GuiDiamondFluidBarrel(InventoryPlayer playerInv, TileEntityDiamondFluidBarrel tile)
    {
        super(new ContainerDiamondFluidBarrel(playerInv, tile));
        this.tile = tile;
        this.playerInv = playerInv;
        this.xSize = 199;
        this.ySize = 166;
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
        this.fontRenderer.drawString(name, (this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2), 7, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 122, 70, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
