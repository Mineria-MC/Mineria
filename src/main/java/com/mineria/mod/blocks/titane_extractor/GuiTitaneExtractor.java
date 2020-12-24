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
	private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/titane_extractor/titane_extractor.png");
	private final InventoryPlayer player;
	private final TileEntityTitaneExtractor tileTitaneExtracting;
	protected final int xSize = 200;
	protected final int	ySize = 200;
	
	public GuiTitaneExtractor(InventoryPlayer player, TileEntityTitaneExtractor tileentity)
	{
		super(new ContainerTitaneExtractor(player, tileentity));
		this.player = player;
		this.tileTitaneExtracting = tileentity;
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
		String s = this.tileTitaneExtracting.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, (this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2 + 20), 7, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 132, this.ySize - 116 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int l = this.getExtractDownScaled(53);
		this.drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 24, 201, 0, 36, l + 1);
	}
	
	private int getExtractDownScaled(int pixels)
	{
		int i = this.tileTitaneExtracting.getField(2);
		int j = this.tileTitaneExtracting.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
