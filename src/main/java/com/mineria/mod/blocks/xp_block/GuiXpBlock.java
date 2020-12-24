package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.References;
import com.mineria.mod.network.GuiButtonPressedMessageHandler;
import com.mineria.mod.util.handler.MineriaPacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiXpBlock extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/xp_block/xp_block_gui.png");
	public final EntityPlayer player;
	private final String buttonText = I18n.format("gui.xp_block.give_xp");
	private final TileEntityXpBlock tileEntity;
	protected final int xSize = 178;
	protected final int	ySize = 139;
	
	public GuiXpBlock(InventoryPlayer playerInv, TileEntityXpBlock xpBlockInv)
	{
		super(new ContainerXpBlock(playerInv, xpBlockInv));
		this.player = playerInv.player;
		this.tileEntity = xpBlockInv;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		GuiButton button = new GuiButton(0, this.guiLeft + 21, this.guiTop + 19, 78, 20, buttonText);
		this.buttonList.add(button);
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
		this.fontRenderer.drawString(I18n.format("container.xp_block"), 28, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		MineriaPacketHandler.PACKET_HANDLER.sendToServer(new GuiButtonPressedMessageHandler.GUIButtonPressedMessage(button.id, tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()));
	}
}
