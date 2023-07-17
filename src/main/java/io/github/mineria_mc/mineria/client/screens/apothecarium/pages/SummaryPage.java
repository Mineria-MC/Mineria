package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumBookmarkInfo;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;

public class SummaryPage extends PartitionedPage {
    public SummaryPage(PageCreationContext ctx) {
        super(ctx);
    }

    @Override
    protected void initParts(List<RenderPart> parts) {
        // Title
        Component tableOfContents = Component.translatable("mineria.apothecarium.table_of_contents").withStyle(ChatFormatting.UNDERLINE);
        float titleScale = width / (float) font.width(tableOfContents);
        parts.add(scaledText(tableOfContents, y, titleScale));

        float textScale = height / 120f;
        float maxTextWidth = width / textScale;
        int dotWidth = font.width(Component.literal("."));
        int digitWidth = font.width(Component.literal("0"));
        parts.add(partial((graphics, x) -> {
            float partY = y + titleScale + height * 2.5f / 10;

            for (Int2ObjectMap.Entry<ApothecariumBookmarkInfo> entry : parentScreen.getBookmarks().int2ObjectEntrySet()) {
                int page = entry.getIntKey() + 1;
                Component partName = entry.getValue().displayName();
                int numberWidth = page < 10 ? digitWidth : digitWidth * 2;
                int dotsWidth = Math.max(0, Mth.floor(maxTextWidth) - font.width(partName) - numberWidth);
                int dotCount = (int) (dotsWidth / 2.5f);
                scaledText(partName.copy().append(".".repeat(dotCount)).append(Integer.toString(page)), partY, textScale).render(graphics, x);
                partY += textScale * font.lineHeight;
            }
        }));
    }
}
