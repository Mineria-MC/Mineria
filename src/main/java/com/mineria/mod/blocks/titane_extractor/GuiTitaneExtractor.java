package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTitaneExtractor extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/titane_extractor.png");
	private final InventoryPlayer player;
	private final TileEntityTitaneExtractor tile;
	
	public GuiTitaneExtractor(InventoryPlayer player, TileEntityTitaneExtractor tile)
	{
		super(new ContainerTitaneExtractor(player, tile));
		this.player = player;
		this.tile = tile;
		this.xSize = 200;
		this.ySize = 200;
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
		this.fontRenderer.drawString(name, (this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2 + 20), 7, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 132, this.ySize - 116 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int extractAnimation = this.getExtractDownScaled(53);
		this.drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 24, 201, 0, 36, extractAnimation + 1);
	}
	
	private int getExtractDownScaled(int pixels)
	{
		int extractTime = this.tile.getField(0);
		int totalExtractTime = this.tile.getField(1);
		return totalExtractTime != 0 && extractTime != 0 ? extractTime * pixels / totalExtractTime : 0;
	}
}
