package io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.ApothecariumPage;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.ItemPage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AntiPoisonDescriptionPageSet implements PageSet {
    private final Lazy<Item> drink;

    public AntiPoisonDescriptionPageSet(Supplier<Item> drink) {
        this.drink = Lazy.of(drink);
    }

    @Override
    public ApothecariumPage[] pages(PageCreationContext ctx) {
        ItemStack stack = new ItemStack(drink.get());
        return new ApothecariumPage[] { new ItemPage(ctx, stack.getItem().getName(stack), stack), new AntiPoisonInfoPage(ctx, drink.get()) };
    }

    @Override
    public PageStart pageStart() {
        return PageStart.EVEN;
    }

    private static class AntiPoisonInfoPage extends ApothecariumPage {
        protected final List<RenderedText> drawingLines;

        public AntiPoisonInfoPage(PageCreationContext ctx, Item drink) {
            super(ctx);
            String antiPoisonId = Optional.ofNullable(ForgeRegistries.ITEMS.getKey(drink)).map(ResourceLocation::getPath).orElse("catholicon");
            Map<Component, Component> categories = ImmutableMap.<Component, Component>builder()
                    .put(Component.translatable("mineria.apothecarium.anti-poison.recipe"),
                            Component.translatable("mineria.apothecarium.anti-poison." + antiPoisonId + ".recipe").withStyle(style -> style.withColor(0x03A545)))
                    .put(Component.translatable("mineria.apothecarium.anti-poison.use_condition"),
                            Component.translatable("mineria.apothecarium.anti-poison." + antiPoisonId + ".use_condition").withStyle(style -> style.withColor(0xFF8722)))
                    .put(Component.translatable("mineria.apothecarium.anti-poison.nota_bene"),
                            notaBeneOrDefault(antiPoisonId))
                    .build();
            this.drawingLines = ImmutableList.copyOf(createRenderedText(categories));
        }

        protected List<RenderedText> createRenderedText(Map<Component, Component> categories) {
            List<RenderedText> result = new ArrayList<>();

            final float categoryHeight = height / (float) categories.size();
            final float sectionHeight = categoryHeight / 4;
            final float titleScale = sectionHeight / (font.lineHeight - 1);

            int categoryIndex = 0;
            for (Map.Entry<Component, Component> entry : categories.entrySet()) {
                Component title = entry.getKey();
                final float categoryStartY = y + categoryHeight * categoryIndex;

                Pair<List<FormattedCharSequence>, Float> textLines = splitAndResize(entry.getValue(), sectionHeight * 2, titleScale);

                result.add(((stack, x) -> {
                    stack.pushPose();
                    stack.translate(x, categoryStartY, 0);
                    stack.scale(titleScale, titleScale, 1);
                    font.draw(stack, title, 0, 0, 0);
                    stack.popPose();

                    float textSize = textLines.getSecond();
                    for (int i = 0; i < textLines.getFirst().size(); i++) {
                        FormattedCharSequence line = textLines.getFirst().get(i);
                        stack.pushPose();
                        stack.translate(x, categoryStartY + sectionHeight + (textSize * (font.lineHeight - 1)) * i, 0);
                        stack.scale(textSize, textSize, 1);
                        font.draw(stack, line, 0, 0, 0);
                        stack.popPose();
                    }
                }));
                categoryIndex++;
            }

            return result;
        }

        @Override
        public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x) {
            this.drawingLines.forEach(text -> text.draw(stack, x));
        }

        protected Pair<List<FormattedCharSequence>, Float> splitAndResize(Component component, final float textAreaSize, float textScale) {
            List<FormattedCharSequence> lines = font.split(component, (int) (width / textScale));
            if(textScale > 0.2f && lines.size() * textScale * (font.lineHeight - 1) > textAreaSize) {
                return splitAndResize(component, textAreaSize, textScale - 0.1f);
            }
            return Pair.of(lines, textScale);
        }

        protected MutableComponent notaBeneOrDefault(String antiPoisonId) {
            String key = "mineria.apothecarium.anti-poison." + antiPoisonId + ".nota_bene";
            if(I18n.exists(key)) {
                return Component.translatable(key).withStyle(style -> style.withColor(0x6415D1));
            }
            return Component.translatable("mineria.apothecarium.anti-poison.no_additional_notes").withStyle(ChatFormatting.GRAY);
        }

        @FunctionalInterface
        private interface RenderedText {
            void draw(PoseStack stack, int x);
        }
    }
}
