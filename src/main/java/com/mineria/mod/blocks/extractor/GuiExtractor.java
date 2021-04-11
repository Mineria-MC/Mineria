package com.mineria.mod.blocks.extractor;

import com.mineria.mod.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiExtractor extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/extractor.png");
	private final InventoryPlayer player;
	private final TileEntityExtractor tile;
	
	public GuiExtractor(InventoryPlayer player, TileEntityExtractor tileentity)
	{
		super(new ContainerExtractor(player, tileentity));
		this.xSize = 213;
		this.ySize = 196;
		this.guiLeft = -40;
		this.guiTop = -10;
		this.player = player;
		this.tile = tileentity;
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
		String tileName = this.tile.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) - 74, 6, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 116, 104, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		int extractAnimation = this.getExtractDownScaled(53);
		this.drawTexturedModalRect(this.guiLeft + 11, this.guiTop + 35, 214, 0, 40, extractAnimation + 1);
	}

	private int getExtractDownScaled(int pixels)
	{
		int extractTime = this.tile.getField(0);
		int maxExtractTime = this.tile.getField(1);
		return maxExtractTime != 0 && extractTime != 0 ? extractTime * pixels / maxExtractTime : 0;
	}
}
