package io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.ApothecariumPage;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.PartitionedPage;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.List;

public class BossSummoningRitualPages implements PageSet {
    @Override
    public ApothecariumPage[] pages(PageCreationContext ctx) {
        return new ApothecariumPage[] { new Page1(ctx), new Page2(ctx), new Page3(ctx) };
    }

    private static Component stepText(int step) {
        return Component.translatable("mineria.apothecarium.druid_rites.step", step).withStyle(ChatFormatting.RED)
                .append(": ")
                .append(Component.translatable("mineria.apothecarium.druid_rites.boss_summoning.step_" + step).withStyle(ChatFormatting.BLACK));
    }

    private static Component noteText(String type, int step) {
        return Component.translatable("mineria.apothecarium.druid_rites." + type).withStyle(ChatFormatting.DARK_AQUA)
                .append(": ")
                .append(Component.translatable("mineria.apothecarium.druid_rites.boss_summoning.step_" + step + "." + type).withStyle(ChatFormatting.BLACK));
    }

    private static class Page1 extends PartitionedPage {
        private static final ResourceLocation RITUAL_TABLE = new ResourceLocation(Mineria.MODID, "textures/gui/apothecarium/ritual_structure.png");

        public Page1(PageCreationContext ctx) {
            super(ctx);
        }

        @Override
        protected void initParts(List<RenderPart> parts) {
            // Title
            Component title = Component.translatable("mineria.apothecarium.druid_rites.boss_summoning.title").withStyle(ChatFormatting.DARK_RED);
            float titleScale = findFittingScale(title, height / 80f, width / 12f);
            parts.add(scaledText(title.getVisualOrderText(), x -> x + (width - font.width(title) * titleScale) / 2f, y, titleScale));

            // Step 1
            float textScale = height / 180f;
            List<FormattedCharSequence> step1Content = font.split(stepText(1), (int) (width / textScale));
            for (int i = 0; i < step1Content.size(); i++) {
                if(i == 3) {
                    break;
                }
                parts.add(scaledText(step1Content.get(i), y + height * 2 / 10f + (font.lineHeight - 1) * textScale * i, textScale));
            }

            // Image
            int imgWidth = 809;
            int imgHeight = 388;
            int scaledImgHeight = Math.min(imgHeight * width / imgWidth, height * 8 / 10);
            parts.add(partial((graphics, x) -> {
                RenderSystem.setShaderColor(1, 1, 1, 1);
                graphics.blit(RITUAL_TABLE, x, y + Mth.floor(height * 3.5f / 10), width, scaledImgHeight, 0, 0, imgWidth, imgHeight, imgWidth, imgHeight);
            }));

            // Tip
            float tipTextScale = height / 240f;
            List<FormattedCharSequence> tip1Content = font.split(noteText("tip", 1), (int) (width / tipTextScale));
            for (int i = 0; i < tip1Content.size(); i++) {
                if(i == 3) {
                    break;
                }
                parts.add(scaledText(tip1Content.get(i), y + height * 3.5f / 10f + scaledImgHeight + (font.lineHeight - 1) * tipTextScale * (i + 1), tipTextScale));
            }
        }
    }

    private static class Page2 extends PartitionedPage {
        public Page2(PageCreationContext ctx) {
            super(ctx);
        }

        @Override
        protected void initParts(List<RenderPart> parts) {
            // Step 2
            float textScale = height / 180f;
            List<FormattedCharSequence> step2Content = font.split(stepText(2), (int) (width / textScale));
            for (int i = 0; i < step2Content.size(); i++) {
                if(i == 4) {
                    break;
                }
                parts.add(scaledText(step2Content.get(i), y + (font.lineHeight - 1) * textScale * i, textScale));
            }

            // Blue Glowstone item
            parts.add((graphics, mouseX, mouseY, partialTicks, x) -> renderItemWithSlotBackground(client, graphics, mouseX, mouseY,
                    new ItemStack(MineriaBlocks.BLUE_GLOWSTONE.get()),
                    x + (width - 2 * height / 10f) / 2f,
                    y + height * 2.25f / 10,
                    2 * height / 10f,
                    false,
                    parentScreen
            ));

            // Note
            float noteTextScale = height / 240f;
            List<FormattedCharSequence> note2Content = font.split(noteText("note", 2), (int) (width / noteTextScale));
            for (int i = 0; i < note2Content.size(); i++) {
                if(i == 3) {
                    break;
                }
                parts.add(scaledText(note2Content.get(i), y + height * 4.5f / 10f + (font.lineHeight - 1) * noteTextScale * i, noteTextScale));
            }

            // Step 3
            List<FormattedCharSequence> step3Content = font.split(stepText(3), (int) (width / textScale));
            for (int i = 0; i < step3Content.size(); i++) {
                if(i == 4) {
                    break;
                }
                parts.add(scaledText(step3Content.get(i), y + height * 6 / 10f + (font.lineHeight - 1) * textScale * i, textScale));
            }

            // Ritual Ingredients
            float size = Math.min(2 * height / 10f, width / 3f);
            float spacing = (width - 3 * size) / 4;
            parts.add((graphics, mouseX, mouseY, partialTicks, x) -> renderItemWithSlotBackground(client, graphics, mouseX, mouseY,
                    new ItemStack(MineriaItems.MISTLETOE.get(), 16),
                    x + spacing,
                    y + height * 8.25f / 10,
                    size,
                    true,
                    parentScreen
            ));
            parts.add((graphics, mouseX, mouseY, partialTicks, x) -> renderItemWithSlotBackground(client, graphics, mouseX, mouseY,
                    new ItemStack(MineriaItems.VANADIUM_INGOT.get()),
                    x + spacing * 2 + size,
                    y + height * 8.25f / 10,
                    size,
                    true,
                    parentScreen
            ));
            parts.add((graphics, mouseX, mouseY, partialTicks, x) -> renderItemWithSlotBackground(client, graphics, mouseX, mouseY,
                    new ItemStack(Items.LAPIS_LAZULI, 64),
                    x + spacing * 3 + size * 2,
                    y + height * 8.25f / 10,
                    size,
                    true,
                    parentScreen
            ));
        }
    }

    private static class Page3 extends PartitionedPage {
        public Page3(PageCreationContext ctx) {
            super(ctx);
        }

        @Override
        protected void initParts(List<RenderPart> parts) {
            // Step 4
            float textScale = height / 180f;
            List<FormattedCharSequence> step4Content = font.split(stepText(4), (int) (width / textScale));
            for (int i = 0; i < step4Content.size(); i++) {
                if(i == 3) {
                    break;
                }
                parts.add(scaledText(step4Content.get(i), y + (font.lineHeight - 1) * textScale * i, textScale));
            }

            parts.add((graphics, mouseX, mouseY, partialTicks, x) -> renderItemWithSlotBackground(client, graphics, mouseX, mouseY,
                    PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_POTION.get()), MineriaPotions.YEW_POISONING.get()),
                    x + (width - 2 * height / 10f) / 2f,
                    y + height * 1.75f / 10,
                    2 * height / 10f,
                    false,
                    parentScreen
            ));

            // Warning 4
            float noteTextScale = height / 240f;
            List<FormattedCharSequence> warning4Content = font.split(noteText("warning", 4), (int) (width / noteTextScale));
            for (int i = 0; i < warning4Content.size(); i++) {
                if(i == 4) {
                    break;
                }
                parts.add(scaledText(warning4Content.get(i), y + height * 4f / 10f + (font.lineHeight - 1) * noteTextScale * i, noteTextScale));
            }
            // Tip 4
            List<FormattedCharSequence> tip4Content = font.split(noteText("tip", 4), (int) (width / noteTextScale));
            for (int i = 0; i < tip4Content.size(); i++) {
                if(i == 3) {
                    break;
                }
                parts.add(scaledText(tip4Content.get(i), y + height * 5.5f / 10f + (font.lineHeight - 1) * noteTextScale * i, noteTextScale));
            }

            // Step 5
            List<FormattedCharSequence> step5Content = font.split(stepText(5), (int) (width / textScale));
            for (int i = 0; i < step5Content.size(); i++) {
                if(i == 7) {
                    break;
                }
                parts.add(scaledText(step5Content.get(i), y + height * 6.75f / 10f + (font.lineHeight - 1) * textScale * i, textScale));
            }
        }
    }
}
