package io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.ItemPage;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.ApothecariumPage;
import io.github.mineria_mc.mineria.common.blocks.PlantBlock;
import io.github.mineria_mc.mineria.util.PlantBiomes;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;

public class PlantDescriptionPageSet implements PageSet {
    private final Lazy<Block> plant;
    private final ItemPageFactory pageFactory;

    public PlantDescriptionPageSet(Supplier<Block> plant) {
        this(plant, (ctx, block) -> new ItemPage(ctx, block.getName(), new ItemStack(block)));
    }

    public PlantDescriptionPageSet(Supplier<Block> plant, Supplier<Item> fruit) {
        this(plant, (ctx, block) -> new ItemPage(ctx, block.getName(), List.of(new ItemStack(block), new ItemStack(fruit.get()))));
    }

    public PlantDescriptionPageSet(Supplier<Block> plant, @Nonnull ItemPageFactory pageFactory) {
        this.plant = Lazy.of(plant);
        this.pageFactory = pageFactory;
    }

    @Override
    public ApothecariumPage[] pages(PageCreationContext ctx) {
        Block block = plant.get();
        return new ApothecariumPage[] { pageFactory.create(ctx, block), new PlantInfoPage(ctx, block) };
    }

    @Override
    public PageStart pageStart() {
        return PageStart.EVEN;
    }

    @FunctionalInterface
    public interface ItemPageFactory {
        @Nonnull
        ItemPage create(PageCreationContext ctx, Block block);
    }

    private static class PlantInfoPage extends ApothecariumPage {
        protected final List<RenderedText> drawingLines;

        public PlantInfoPage(PageCreationContext ctx, Block plant) {
            super(ctx);
            String plantId = Optional.ofNullable(ForgeRegistries.BLOCKS.getKey(plant)).map(ResourceLocation::getPath).orElse("plantain");
            Map<Component, Component> categories = ImmutableMap.<Component, Component>builder()
                    .put(Component.translatable("mineria.apothecarium.plants.plant_type"),
                            Component.translatable("mineria.apothecarium.plants.plant_type." + getPlantType(plant)).withStyle(ChatFormatting.DARK_GREEN))
                    .put(Component.translatable("mineria.apothecarium.plants.plant_generation"),
                            biomeList(plant).withStyle(ChatFormatting.GOLD))
                    .put(Component.translatable("mineria.apothecarium.plants.infused_effects"),
                            Component.translatable("mineria.apothecarium.plants." + plantId + ".infused_effects").withStyle(ChatFormatting.GRAY))
                    .put(Component.translatable("mineria.apothecarium.plants.other_uses"),
                            usagesOrDefault(plantId, plant.getName()))
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

        protected String getPlantType(Block block) {
            if(block instanceof PlantBlock plantBlock && plantBlock.isBush()) {
                return "bush";
            }
            if(block instanceof VineBlock) {
                return "vine";
            }
            if(block instanceof SaplingBlock) {
                return "tree";
            }
            return "plant";
        }

        protected MutableComponent biomeList(Block plant) {
            return Arrays.stream(PlantBiomes.biomesFor(plant))
                    .map(key -> key.location().toLanguageKey("biome"))
                    .map(Component::translatable)
                    .reduce((c1, c2) -> c1.append(", ").append(c2))
                    .orElse(Component.translatable("mineria.apothecarium.plants.plant_generation.no_biomes"));
        }

        protected MutableComponent usagesOrDefault(String plantId, Component plantName) {
            String key = "mineria.apothecarium.plants." + plantId + ".other_uses";
            if(I18n.exists(key)) {
                return Component.translatable(key).withStyle(ChatFormatting.LIGHT_PURPLE);
            }
            return Component.translatable("mineria.apothecarium.plants.no_other_uses", plantName).withStyle(ChatFormatting.GRAY);
        }

        @FunctionalInterface
        private interface RenderedText {
            void draw(PoseStack stack, int x);
        }
    }
}
