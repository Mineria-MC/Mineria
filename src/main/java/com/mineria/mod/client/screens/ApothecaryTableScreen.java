package com.mineria.mod.client.screens;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.ApothecaryTableContainer;
import com.mineria.mod.common.effects.PoisonSource;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static net.minecraft.util.ColorHelper.PackedColor.*;

public class ApothecaryTableScreen extends ContainerScreen<ApothecaryTableContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/apothecary_table.png");

    public ApothecaryTableScreen(ApothecaryTableContainer container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);

        this.inventoryLabelX = 112;
        this.inventoryLabelY = 72;
        this.titleLabelY = 5;
    }

    @Override
    protected void init()
    {
        super.init();
        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack stack, int mouseX, int mouseY)
    {
        if (this.minecraft.player.inventory.getCarried().isEmpty())
        {
            if(this.hoveredSlot != null && this.hoveredSlot.hasItem())
            {
                this.renderTooltip(stack, this.hoveredSlot.getItem(), mouseX, mouseY);
            }
            else if(mouseX >= this.leftPos + 12 && mouseY >= this.topPos + 8 && mouseX <= this.leftPos + 29 && mouseY <= this.topPos + 79)
            {
                PoisonSource poisonSource = this.getPoisonSource();
                if(poisonSource != null)
                {
                    this.renderTooltip(stack, new TranslationTextComponent(poisonSource.getTranslationKey()), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void renderBg(MatrixStack stack, float mouseX, int mouseY, int partialTicks)
    {
        RenderSystem.color4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int liquidScaled = getLiquidScaled();
        if(getPoisonSource() != null)
        {
            int color = getPoisonSource().getColor();
            RenderSystem.color3f(red(color) / 255.0F, green(color) / 255.0F, blue(color) / 255.0F);
            this.blit(stack, this.leftPos + 13, this.topPos + 9 + 69 - liquidScaled, 177, 18 + 69 - liquidScaled, 15, liquidScaled);
            RenderSystem.color4f(1, 1, 1, 1);
        }
        this.blit(stack, this.leftPos + 12, this.topPos + 8, 192, 17, 17, 71);
        this.blit(stack, this.leftPos + 107, this.topPos + 35, 176, 0, getApplicationTimeScaled() + 1, 17);
    }

    @Nullable
    private PoisonSource getPoisonSource()
    {
        return this.menu.getPoisonSource();
    }

    private int getLiquidScaled()
    {
        return getPoisonSource() == null || this.menu.getLiquidAmount() == 0 ? 0 : this.menu.getLiquidAmount() * 69 / 5;
    }

    private int getApplicationTimeScaled()
    {
        return this.menu.getApplicationTime() == 0 ? 0 : this.menu.getApplicationTime() * 24 / this.menu.getTotalApplicationTime();
    }
}
