package com.mineria.mod.blocks.infuser;

import com.mineria.mod.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInfuser extends GuiContainer
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/infuser/infuser_gui.png");
    private final InventoryPlayer player;
    private final TileEntityInfuser tileInfuser;

    public GuiInfuser(InventoryPlayer player, TileEntityInfuser tileInfuser)
    {
        super(new ContainerInfuser(player, tileInfuser));
        this.tileInfuser = tileInfuser;
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
        String s = this.tileInfuser.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, (this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2 + 20), 5, 4210752);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (TileEntityInfuser.isInfusing(this.tileInfuser))
        {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(this.guiLeft + 149, this.guiTop + 36 + 12 - k, 202, 12 - k, 14, k + 1);
        }

        int l = this.getInfuseProgressScaled(26);
        this.drawTexturedModalRect(this.guiLeft + 64, this.guiTop + 36, 176, 0, l + 1, 13);
    }

    private int getInfuseProgressScaled(int pixels)
    {
        int i = this.tileInfuser.getField(2);
        int j = this.tileInfuser.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileInfuser.getField(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.tileInfuser.getField(0) * pixels / i;
    }
}
