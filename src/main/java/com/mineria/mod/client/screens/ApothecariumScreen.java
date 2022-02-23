package com.mineria.mod.client.screens;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaSounds;
import com.mineria.mod.util.MineriaUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Apothecarium is not finished yet, because we lack of resources and of time, so it will be upgraded in later versions. TODOLTR
 * For now you can take a look at this messy half-commented code.
 */
public class ApothecariumScreen extends Screen
{
    private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/the_apothecarium_2.png");

    /*private static final ImmutableList<ITextComponent> TEXT = ImmutableList.of(
            new TranslationTextComponent("mineria.apothecarium.introduction"),
            new TranslationTextComponent("apothecarium.page_1.summary"),
            new TranslationTextComponent("apothecarium.page_2.first_part")
    );*/
    private static final ImmutableList<Page> PAGES = ImmutableList.of(
//            () -> new TranslationTextComponent("mineria.apothecarium.introduction"),
            new SummaryPage(),
            new EntitledPage(new TranslatableComponent("mineria.apothecarium.druid_rites.title"), new TranslatableComponent("mineria.apothecarium.druid_rites.part_one.title"), new TranslatableComponent("mineria.apothecarium.druid_rites.part_one.text")),
            new SubtitledPage(new TranslatableComponent("mineria.apothecarium.druid_rites.part_two.title"), new TranslatableComponent("mineria.apothecarium.druid_rites.part_two.text"))
    );
    /*private static final ImmutableMap<Integer, ITextComponent> TEXT_MAP = ImmutableMap.<Integer, ITextComponent>builder()
            .put(1, new TranslationTextComponent("apothecarium.page_0.introduction"))
            .put(2, new TranslationTextComponent("apothecarium.page_1.summary"))
            .put(3, new TranslationTextComponent("apothecarium.page_2.first_part"))
            .build();
    private static final int PAGES_COUNT = Math.round(TEXT_MAP.size() / 2.0F);*/
    private int leftPos;
    private int topPos;
    private final List<List<FormattedCharSequence>> pages = new ArrayList<>();
    private final int imageWidth = 292;
    private final int imageHeight = 180;
    private int currentPage;
    private List<FormattedCharSequence> leftProcessors = Collections.emptyList();
    private List<FormattedCharSequence> rightProcessors = Collections.emptyList();
    private final List<Bookmark> bookmarks = new ArrayList<>();
    private int cachedPage = -1;
    private TurnPageButton forwardButton;
    private TurnPageButton backButton;

    public ApothecariumScreen()
    {
        super(NarratorChatListener.NO_TITLE);
        this.currentPage = -1;
    }

    public boolean setPage(int toPage)
    {
        int page = Mth.clamp(toPage, -1, getPagesCount() - 1);
        if (page != this.currentPage)
        {
            this.currentPage = page;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        }
        return false;
    }

    @Override
    protected void init()
    {
        this.leftPos = (this.width - imageWidth) / 2;
        this.topPos = (this.height - imageHeight - 20) / 2;
        initPages();
        createMenuControls();
        createPageControlButtons();
        createBookmarks();
    }

    protected void initPages()
    {
        this.bookmarks.clear();
        this.pages.clear();
        this.pages.add(Collections.singletonList(TextComponent.EMPTY.getVisualOrderText()));

        for(Page page : PAGES)
            addPages(page, this.font.split(page.getNonNullText(), 114), 0);
    }

    private void addPages(Page page, List<FormattedCharSequence> processors, int pageIndex)
    {
        if(processors.size() <= 16)
        {
            List<FormattedCharSequence> processed = page.process(processors, pageIndex);
            this.pages.add(processed);
            if(page.hasBookmark() && pageIndex == 0)
                this.bookmarks.add(new Bookmark(this.pages.indexOf(processed), page.getTitle()));
        }
        else
        {
            List<FormattedCharSequence> processed = page.process(processors.subList(0, 16), pageIndex);
            this.pages.add(processed);
            if(page.hasBookmark() && pageIndex == 0)
                this.bookmarks.add(new Bookmark(this.pages.indexOf(processed), page.getTitle()));
            this.addPages(page, processors.subList(16, processors.size()), pageIndex + 1);
        }
    }

    protected void createMenuControls()
    {
        this.addRenderableWidget(new Button(this.leftPos, this.topPos + 196, 200, 20, CommonComponents.GUI_DONE, (btn) -> this.minecraft.setScreen(null)));
    }

    protected void createPageControlButtons()
    {
        this.forwardButton = this.addRenderableWidget(new TurnPageButton(this.leftPos + this.imageWidth - 36, this.topPos + 158, true, (btn) -> this.pageForward()));
        this.backButton = this.addRenderableWidget(new TurnPageButton(this.leftPos + 12, this.topPos + 158, false, (btn) -> this.pageBack()));
        this.updateButtonVisibility();
    }

    protected void createBookmarks()
    {
        this.renderables.removeIf(BookmarkButton.class::isInstance);
        this.children().removeIf(BookmarkButton.class::isInstance);

        for(int index = 0; index < this.bookmarks.size(); index++)
        {
            int bookmarkPage = this.bookmarks.get(index).page / 2;
            Component name = this.bookmarks.get(index).name;

            /*if(currentPage == bookmarkPage)
                this.addButton(new BookmarkButton(this.leftPos + (this.imageWidth / 2) - 16, this.topPos + 8, 128, 221, 140, 31, StringTextComponent.EMPTY, btn -> createBookmarks()));
            else */
            if(currentPage < bookmarkPage)
                this.addRenderableWidget(new BookmarkButton(this.leftPos + this.imageWidth - 12, this.topPos + 8 + 23 * index, 179, 221, 24, 19, false, name, btn -> setPage(bookmarkPage)));
            else
                this.addRenderableWidget(new BookmarkButton(this.leftPos - 16, this.topPos + 8 + 23 * index, 204, 221, 24, 19, true, name, btn -> setPage(bookmarkPage)));
        }
    }

    protected void pageBack()
    {
        if (this.currentPage > -1)
        {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward()
    {
        if (this.currentPage < getPagesCount() - 1)
        {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility()
    {
        this.forwardButton.visible = this.currentPage < getPagesCount() - 1;
        this.backButton.visible = this.currentPage > -1;
        createBookmarks();
    }

    protected int getPagesCount()
    {
        return Math.round(this.pages.size() / 2.0F);
    }

    @Override
    public boolean keyPressed(int keyCode, int mouseX, int mouseY)
    {
        if (super.keyPressed(keyCode, mouseX, mouseY))
        {
            return true;
        } else
        {
            switch (keyCode)
            {
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double offset)
    {
        if(super.mouseScrolled(mouseX, mouseY, offset))
        {
            return true;
        } else
        {
            if(offset > 0 && this.currentPage < getPagesCount() - 1)
            {
                pageForward();
                return true;
            } else if(offset < 0 && this.currentPage > -1)
            {
                pageBack();
                return true;
            }
            return false;
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(stack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 79, 5, 291, 180);

        if(currentPage == -1)
        {
            this.blit(stack, this.leftPos + (this.imageWidth / 2) + 18, this.topPos + 25, 178, 185, 99, 34);
        }
        else
        {
            if (this.cachedPage != this.currentPage)
            {
//                ITextProperties leftContent = TEXT_MAP.getOrDefault(this.currentPage * 2, StringTextComponent.EMPTY);
                this.leftProcessors = getPageContent(this.currentPage * 2);
//                ITextProperties rightContent = TEXT_MAP.getOrDefault(this.currentPage * 2 + 1, StringTextComponent.EMPTY);
                this.rightProcessors = getPageContent(this.currentPage * 2 + 1);
                /*this.pageMsg = new TranslationTextComponent("book.pageIndicator", this.currentPage + 1, Math.max(PAGES_COUNT, 1));*/
            }

            this.cachedPage = this.currentPage;
            /*int msgWidth = this.font.width(this.pageMsg);
            this.font.draw(stack, this.pageMsg, (float)(this.topPos - msgWidth + 192 - 44), 18.0F, 0);*/
//            int leftRows = Math.min(128 / 9, this.leftProcessors.size());

            for(int index = 0; index < this.leftProcessors.size(); ++index)
            {
                FormattedCharSequence processor = this.leftProcessors.get(index);
                this.font.draw(stack, processor, this.leftPos + 14, this.topPos + 16 + index * 9, 0);
            }

//            int rightRows = Math.min(128 / 9, this.rightProcessors.size());

            for(int index = 0; index < this.rightProcessors.size(); ++index)
            {
                FormattedCharSequence processor = this.rightProcessors.get(index);
                this.font.draw(stack, processor, this.leftPos + (this.imageWidth / 2.0F) + 14, this.topPos + 16 + index * 9, 0);
            }
        }

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private List<FormattedCharSequence> getPageContent(int index)
    {
        if(index >= this.pages.size())
            return new ArrayList<>();
        else
            return pages.get(index);
    }

    @Override
    public void blit(PoseStack stack, int x, int y, int u, int v, int width, int height)
    {
        blit(stack, x, y, this.getBlitOffset(), u, v, width, height, 512, 512);
    }

    public static class TurnPageButton extends Button
    {
        private final boolean isForward;

        public TurnPageButton(int x, int y, boolean isForward, OnPress onPressed)
        {
            super(x, y, 23, 13, TextComponent.EMPTY, onPressed);
            this.isForward = isForward;
        }

        @Override
        public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks)
        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
            int textureX = 128;
            int textureY = 192;
            if (this.isHovered())
                textureX += 23;

            if (!this.isForward)
                textureY += 13;

            this.blit(stack, this.x, this.y, textureX, textureY, 23, 13);
        }

        @Override
        public void playDownSound(SoundManager handler)
        {
            handler.play(SimpleSoundInstance.forUI(MineriaUtils.currentDateMatches(6, 26) ? MineriaSounds.APOTHECARIUM_CUSTOM_PAGE_TURN.get() : SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }

        @Override
        public void blit(PoseStack stack, int x, int y, int u, int v, int width, int height)
        {
            blit(stack, x, y, this.getBlitOffset(), u, v, width, height, 512, 512);
        }
    }

    @FunctionalInterface
    interface Page
    {
        @Nullable
        Component getText();

        default Component getNonNullText()
        {
            return getText() == null ? TextComponent.EMPTY : getText();
        }

        default List<FormattedCharSequence> process(List<FormattedCharSequence> processors, int pageIndex)
        {
            return processors;
        }

        default boolean hasBookmark()
        {
            return false;
        }

        default Component getTitle()
        {
            return TextComponent.EMPTY;
        }
    }

    static class SummaryPage implements Page
    {
        @Nullable
        @Override
        public Component getText()
        {
            return new TranslatableComponent("mineria.apothecarium.summary");
        }

        @Override
        public List<FormattedCharSequence> process(List<FormattedCharSequence> processors, int pageIndex)
        {
            if(pageIndex == 0)
            {
                List<FormattedCharSequence> processed = new ArrayList<>();

                processors.forEach(p -> {
                    processed.add(p);
                    processed.add(TextComponent.EMPTY.getVisualOrderText());
                });

                return processed;
            }
            return processors;
        }

        @Override
        public boolean hasBookmark()
        {
            return true;
        }

        @Override
        public Component getTitle()
        {
            return new TranslatableComponent("mineria.apothecarium.summary.title");
        }
    }

    /*class Bookmark extends AbstractButton
    {
        private final int targetPage;
        private final int index;

        public Bookmark(int xPos, int yPos, int width, int height, ITextComponent displayString, int targetPage, int bookmarkIndex)
        {
            super(xPos, yPos, width, height, displayString);
            this.targetPage = targetPage;
            this.index = bookmarkIndex;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
        {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.font;

            mc.getTextureManager().bind(BOOK_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();

            if(currentPage == targetPage)
                this.blit(stack, this.x, this.y, 128, 221, 140, 31);
            else if(currentPage < targetPage)
                this.blit(stack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            else
                this.blit(stack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

            this.renderBg(stack, mc, mouseX, mouseY);
            int j = getFGColor();
            drawCenteredString(stack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }

        @Override
        public void onPress()
        {
            setPage(this.targetPage);
        }
    }*/

    private static class Bookmark
    {
        private final int page;
        private final Component name;

        private Bookmark(int page, Component name)
        {
            this.page = page;
            this.name = name;
        }
    }

    private static class BookmarkButton extends Button
    {
        private final int u, v;
        private int hoverTickCount;
        private final boolean reversed;

        public BookmarkButton(int xPos, int yPos, int u, int v, int width, int height, boolean reversed, Component displayString, OnPress action)
        {
            super(xPos, yPos, width, height, displayString, action);
            this.u = u;
            this.v = v;
            this.reversed = reversed;
        }

        @Override
        public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks)
        {
            if(isHovered)
            {
                hoverTickCount = Mth.clamp(hoverTickCount + 2, 0, 80);
            } else
            {
                hoverTickCount = Mth.clamp(hoverTickCount - 2, 0, 80);
            }

            Minecraft mc = Minecraft.getInstance();
            Font font = mc.font;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int scaledPixels = hoverTickCount * 48 / 100;
            if(reversed)
                this.blit(stack, this.x - scaledPixels, this.y, u, v, width + scaledPixels, height);
            else
                this.blit(stack, this.x, this.y, u - scaledPixels, v, width + scaledPixels, height);

            if(hoverTickCount > 0)
            {
                float alpha = hoverTickCount / 80F;
                Component name = this.getMessage();
                int x = reversed ? this.x - scaledPixels + 8 : this.x + this.width + scaledPixels - 8 - font.width(name);
                drawString(stack, font, name, x, this.y + (this.height - 8) / 2, getFGColor() | Mth.ceil(alpha * 255.0F) << 24);
            }
        }

        @Override
        public void blit(PoseStack stack, int x, int y, int u, int v, int width, int height)
        {
            blit(stack, x, y, this.getBlitOffset(), u, v, width, height, 512, 512);
        }
    }

    private static class EntitledPage implements Page
    {
        private final Component title;
        private final MutableComponent text;

        public EntitledPage(MutableComponent title, MutableComponent subTitle, Component text)
        {
            this.title = title.copy();
            this.text = new TextComponent("").append(title.withStyle(style -> style.withColor(ChatFormatting.RED).withBold(true))).append("\n\n").append(subTitle.withStyle(ChatFormatting.UNDERLINE)).append("\n\n").append(text);
        }

        @Nullable
        @Override
        public Component getText()
        {
            return text;
        }

        @Override
        public Component getTitle()
        {
            return title;
        }

        @Override
        public boolean hasBookmark()
        {
            return true;
        }
    }

    private static class SubtitledPage implements Page
    {
        private final MutableComponent text;

        public SubtitledPage(MutableComponent subTitle, Component text)
        {
            this.text = new TextComponent("").append(subTitle.withStyle(ChatFormatting.UNDERLINE)).append("\n\n").append(text);
        }

        @Nullable
        @Override
        public Component getText()
        {
            return text;
        }
    }
}
