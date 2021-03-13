package com.mineria.mod.blocks.extractor;

public class GuiExtractor// extends GuiContainer
{
	/*
	private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/extractor/extractor.png");
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

		int l = this.getExtractDownScaled(53);
		this.drawTexturedModalRect(this.guiLeft + 11, this.guiTop + 35, 214, 0, 40, l + 1);
	}

	private int getExtractDownScaled(int pixels)
	{
		int i = this.tile.getField(0);
		int j = this.tile.getField(1);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	 */
}
