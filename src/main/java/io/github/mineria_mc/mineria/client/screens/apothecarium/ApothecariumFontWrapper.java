package io.github.mineria_mc.mineria.client.screens.apothecarium;

import io.github.mineria_mc.mineria.Mineria;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public class ApothecariumFontWrapper {
    private final Font wrapped;
    public final int lineHeight;
    private final ResourceLocation customFontId;

    public ApothecariumFontWrapper(Font wrapped, ResourceLocation customFontId) {
        this.wrapped = wrapped;
        this.lineHeight = wrapped.lineHeight;
        this.customFontId = customFontId;
    }

    public void draw(PoseStack stack, Component component, float x, float y, int color) {
        wrapped.draw(stack, tryApplyFont(component), x, y, color);
    }

    public void draw(PoseStack stack, String text, float x, float y, int color) {
        draw(stack, Component.literal(text), x, y, color);
    }

    public void draw(PoseStack stack, FormattedCharSequence text, float x, float y, int color) {
        wrapped.draw(stack, text, x, y, color);
    }

    public List<FormattedCharSequence> split(FormattedText text, int maxWidth) {
        return wrapped.split(tryApplyFont(text), maxWidth);
    }

    public int width(FormattedText text) {
        return wrapped.width(tryApplyFont(text));
    }

    public Font wrapped() {
        return wrapped;
    }

    public <T extends FormattedText> T tryApplyFont(T text) {
        if(text instanceof MutableComponent mutable) {
            if(getInternalFont(mutable.getStyle()) == null) {
                mutable.withStyle(style -> style.withFont(this.customFontId));
            }
        }
        return text;
    }

    private static Field FONT;
    private static boolean errorLogged;

    private static ResourceLocation getInternalFont(Style style) {
        try {
            if(FONT == null) {
                FONT = ObfuscationReflectionHelper.findField(Style.class, "f_131110_");
            }
            return (ResourceLocation) FONT.get(style);
        } catch (IllegalAccessException e) {
            if(!errorLogged) {
                errorLogged = true;
                Mineria.LOGGER.error("Failed to obtain font from style: ", e);
            }
        }
        return null;
    }
}
