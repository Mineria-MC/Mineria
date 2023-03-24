package io.github.mineria_mc.mineria.client.screens.apothecarium;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.AntiPoisonDescriptionPageSet;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.BossSummoningRitualPages;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.PageSet;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.PlantDescriptionPageSet;
import io.github.mineria_mc.mineria.client.screens.apothecarium.pages.*;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import io.github.mineria_mc.mineria.common.items.ApothecariumItem;
import io.github.mineria_mc.mineria.network.MineriaPacketHandler;
import io.github.mineria_mc.mineria.network.SavePlayerBookmarkMessageHandler;
import io.github.mineria_mc.mineria.util.MineriaConfig;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO: The Apothecarium is still work in progress.
 */
public class ApothecariumScreen extends Screen {
    public static final ResourceLocation FONT_LOCATION = getFontLocation();
    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/apothecarium_new.png");

    private static final ImmutableList<PageSet> PAGES = ImmutableList.of(
            // Font Testing pages
//            FontTestingPage.create(new ResourceLocation(Mineria.MODID, "lcallig")),
//            FontTestingPage.create(new ResourceLocation(Mineria.MODID, "lhandw")),
//            FontTestingPage.create(new ResourceLocation(Mineria.MODID, "pristina")),
//            FontTestingPage.create(new ResourceLocation(Mineria.MODID, "segoesc")),
//            FontTestingPage.create(new ResourceLocation(Mineria.MODID, "tempsitc")),
            PageSet.singleton(SummaryPage::new, PageSet.PageStart.ODD),
            PartTitlePage.create(Component.translatable("mineria.apothecarium.plants.part_name"), () -> new ItemStack(MineriaBlocks.PLANTAIN.get())),
            new PlantDescriptionPageSet(MineriaBlocks.PLANTAIN),
            new PlantDescriptionPageSet(MineriaBlocks.MINT),
            new PlantDescriptionPageSet(MineriaBlocks.THYME),
            new PlantDescriptionPageSet(MineriaBlocks.NETTLE),
            new PlantDescriptionPageSet(MineriaBlocks.PULMONARY),
            new PlantDescriptionPageSet(MineriaBlocks.RHUBARB),
            new PlantDescriptionPageSet(MineriaBlocks.SENNA),
            new PlantDescriptionPageSet(MineriaBlocks.SENNA_BUSH),
            new PlantDescriptionPageSet(MineriaBlocks.ELDERBERRY_BUSH, MineriaItems.ELDERBERRY),
            new PlantDescriptionPageSet(MineriaBlocks.BLACK_ELDERBERRY_BUSH, MineriaItems.BLACK_ELDERBERRY),
            new PlantDescriptionPageSet(MineriaBlocks.STRYCHNOS_TOXIFERA),
            new PlantDescriptionPageSet(MineriaBlocks.STRYCHNOS_NUX_VOMICA),
            new PlantDescriptionPageSet(MineriaBlocks.BELLADONNA),
            new PlantDescriptionPageSet(MineriaBlocks.MANDRAKE),
            new PlantDescriptionPageSet(MineriaBlocks.LYCIUM_CHINENSE, MineriaItems.GOJI),
            new PlantDescriptionPageSet(MineriaBlocks.SAUSSUREA_COSTUS),
            new PlantDescriptionPageSet(MineriaBlocks.SCHISANDRA_CHINENSIS, MineriaItems.FIVE_FLAVOR_FRUIT),
            new PlantDescriptionPageSet(MineriaBlocks.PULSATILLA_CHINENSIS),
            new PlantDescriptionPageSet(MineriaBlocks.SPRUCE_YEW_SAPLING, (ctx, block) ->
                    new ItemPage(ctx, Component.translatable("mineria.apothecarium.tree.spruce_yew_tree"), List.of(new ItemStack(block), new ItemStack(MineriaBlocks.SPRUCE_YEW_LEAVES.get()), new ItemStack(MineriaItems.YEW_BERRIES.get())))),
            PartTitlePage.create(Component.translatable("mineria.apothecarium.anti-poison.part_name"), () -> new ItemStack(MineriaItems.MIRACLE_ANTI_POISON.get())),
            new AntiPoisonDescriptionPageSet(MineriaItems.CATHOLICON),
            new AntiPoisonDescriptionPageSet(MineriaItems.CHARCOAL_ANTI_POISON),
            new AntiPoisonDescriptionPageSet(MineriaItems.MILK_ANTI_POISON),
            new AntiPoisonDescriptionPageSet(() -> Items.MILK_BUCKET),
            new AntiPoisonDescriptionPageSet(MineriaItems.NAUSEOUS_ANTI_POISON),
            new AntiPoisonDescriptionPageSet(MineriaItems.ANTI_POISON),
            new AntiPoisonDescriptionPageSet(MineriaItems.MIRACLE_ANTI_POISON),
            new AntiPoisonDescriptionPageSet(MineriaItems.MANDRAKE_ROOT_TEA),
            new AntiPoisonDescriptionPageSet(MineriaItems.SAUSSUREA_COSTUS_ROOT_TEA),
            new AntiPoisonDescriptionPageSet(MineriaItems.PULSATILLA_CHINENSIS_ROOT_TEA),
            new AntiPoisonDescriptionPageSet(MineriaItems.SCALPEL),
            PartTitlePage.create(Component.translatable("mineria.apothecarium.druid_rites.part_name"), () -> new ItemStack(MineriaBlocks.RITUAL_TABLE.get())),
            new BossSummoningRitualPages(),
            PageSet.singleton(DruidSummoningRitualPage::new),
            PartTitlePage.create(Component.translatable("mineria.apothecarium.elementary_beings.part_name"), () -> new ItemStack(MineriaItems.FIRE_GOLEM_SPAWN_EGG.get())),
            ElementaryBeingPage.create(MineriaEntities.FIRE_GOLEM),
            ElementaryBeingPage.create(MineriaEntities.DIRT_GOLEM),
            ElementaryBeingPage.create(MineriaEntities.AIR_SPIRIT),
            ElementaryBeingPage.create(MineriaEntities.WATER_SPIRIT, (entity, time) -> entity.setFrozen(Math.floor(time / 30) % 2 == 1))
    );
    private final SoundEvent turnPageSound = MineriaUtils.currentDateMatches(6, 26) ? MineriaSounds.APOTHECARIUM_CUSTOM_PAGE_TURN.get() : SoundEvents.BOOK_PAGE_TURN;
    private final List<ApothecariumPage> pages = new ArrayList<>();
    private final Int2ObjectMap<ApothecariumBookmarkInfo> bookmarks = new Int2ObjectArrayMap<>();

    private ApothecariumFontWrapper font;
    private int leftPos;
    private int topPos;
    private int bookWidth;
    private int bookHeight;
    private int currentPage;
    @Nullable
    private Integer playerBookmark;
    private TurnPageButton forwardButton;
    private TurnPageButton backButton;
    private PlayerBookmarkButton playerBookmarkButton;

    public ApothecariumScreen(int startPage, int pagesAmount) {
        super(GameNarrator.NO_TITLE);
        if(pagesAmount != PAGES.size()) {
            this.currentPage = -1;
        } else {
            this.currentPage = startPage;
            if(startPage > -1) {
                this.playerBookmark = startPage;
            }
        }
    }

    public ApothecariumScreen() {
        this(-1, -1);
    }

    private static final float WIDTH_RATIO = 854 / 2f;

    private int scaledWidth(float pixels) {
        float scaleFactor = width / WIDTH_RATIO;
        return (int) (pixels * scaleFactor);
    }

    private static final float HEIGHT_RATIO = 480 / 2f;

    private int scaledHeight(float pixels) {
        float scaleFactor = height / HEIGHT_RATIO;
        return (int) (pixels * scaleFactor);
    }

    public boolean setPage(int toPage) {
        int page = Mth.clamp(toPage, -1, getPagesCount());
        if (page != this.currentPage) {
            this.currentPage = page;
            this.updateButtonVisibility();
            return true;
        }
        return false;
    }

    @Override
    protected void init() {
        this.font = new ApothecariumFontWrapper(super.font, FONT_LOCATION);
        this.bookWidth = scaledWidth(279);
        this.bookHeight = scaledHeight(180);
        this.leftPos = (this.width - bookWidth) / 2;
        this.topPos = (this.height - bookHeight - 20) / 2;
        initPages();
        createMenuControls();
        createPageControlButtons();
        createBookmarks();
    }

    @Override
    public void resize(@Nonnull Minecraft minecraft, int width, int height) {
        int page = currentPage;
        super.resize(minecraft, width, height);
        setPage(page);
    }

    protected void initPages() {
        this.bookmarks.clear();
        this.pages.clear();

        final int pageY = this.topPos + scaledHeight(16);
        final int pageWidth = scaledWidth(122);
        final int pageHeight = scaledHeight(144);
        PageCreationContext ctx = new PageCreationContext(this.minecraft, this.font, pageY, pageWidth, pageHeight, this);

        this.pages.add(new EmptyPage(ctx));

        for (PageSet pageSet : PAGES) {
            if(!pageSet.pageStart().validPageIndex(pages.size())) {
                this.pages.add(new EmptyPage(ctx));
            }

            for (ApothecariumPage page : pageSet.pages(ctx)) {
                pages.add(page);
                ApothecariumBookmarkInfo bookmarkInfo = page.bookmarkInfo();
                if(bookmarkInfo != null) {
                    bookmarks.put(pages.indexOf(page), bookmarkInfo);
                }
            }
        }
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (btn) -> this.minecraft.setScreen(null))
                .bounds(this.leftPos + (bookWidth - 200) / 2, this.topPos + bookHeight + scaledHeight(8), 200, 20)
                .build());
    }

    protected void createPageControlButtons() {
        this.forwardButton = this.addRenderableWidget(new TurnPageButton(this.leftPos + this.bookWidth, this.topPos + scaledHeight(172), true, btn -> this.pageForward()));
        this.backButton = this.addRenderableWidget(new TurnPageButton(this.leftPos - scaledWidth(24), this.topPos + scaledHeight(172), false, btn -> this.pageBack()));
        this.playerBookmarkButton = this.addRenderableWidget(new PlayerBookmarkButton(this.leftPos + scaledWidth(136), this.topPos + scaledHeight(170), btn -> {
            if(playerBookmark == null) {
                playerBookmark = currentPage;
                return;
            }
            if(currentPage == playerBookmark) {
                playerBookmark = null;
                return;
            }
            if(Screen.hasShiftDown()) {
                playerBookmark = currentPage;
                return;
            }
            setPage(playerBookmark);
        }));
        this.updateButtonVisibility();
    }

    protected void createBookmarks() {
        Set<GuiEventListener> toRemove = this.children().stream().filter(BookmarkButton.class::isInstance).collect(Collectors.toSet());
        toRemove.forEach(this::removeWidget);

        if(currentPage < 0 || currentPage >= getPagesCount()) {
            return;
        }

        List<Int2ObjectMap.Entry<ApothecariumBookmarkInfo>> entries = bookmarks.int2ObjectEntrySet().stream().sorted(Map.Entry.comparingByKey(Integer::compareTo)).toList();
        for (int i = 0; i < entries.size(); i++) {
            int bookmarkPage = entries.get(i).getIntKey() / 2;
            ApothecariumBookmarkInfo bookmarkInfo = entries.get(i).getValue();
            float xOffset = 5f / entries.size();

            if (currentPage <= bookmarkPage) {
                this.addRenderableWidget(new BookmarkButton(this.leftPos + this.bookWidth - scaledWidth(13 - i * xOffset), this.topPos + scaledHeight(12 + 23 * i), false, bookmarkInfo, btn -> setPage(bookmarkPage)));
            }
            else {
                this.addRenderableWidget(new BookmarkButton(this.leftPos - scaledWidth(13 - i * xOffset), this.topPos + scaledHeight(12 + 23 * i), true, bookmarkInfo, btn -> setPage(bookmarkPage)));
            }
        }
    }

    protected void pageBack() {
        if (this.currentPage > -1) {
            --this.currentPage;
            if(minecraft != null) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(turnPageSound, 1.0F));
            }
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < getPagesCount()) {
            ++this.currentPage;
            if(minecraft != null) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(turnPageSound, 1.0F));
            }
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < getPagesCount();
        this.backButton.visible = this.currentPage > -1;
        this.playerBookmarkButton.visible = this.currentPage > -1 && this.currentPage < getPagesCount();
        createBookmarks();
    }

    protected int getPagesCount() {
        return Math.round(this.pages.size() / 2.0F);
    }

    public Int2ObjectMap<ApothecariumBookmarkInfo> getBookmarks() {
        return bookmarks;
    }

    @Override
    public boolean keyPressed(int keyCode, int mouseX, int mouseY) {
        if (super.keyPressed(keyCode, mouseX, mouseY)) {
            return true;
        }
        return switch (keyCode) {
            case 266 -> {
                this.backButton.onPress();
                yield true;
            }
            case 267 -> {
                this.forwardButton.onPress();
                yield true;
            }
            default -> false;
        };
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double offset) {
        if (super.mouseScrolled(mouseX, mouseY, offset)) {
            return true;
        } else {
            if (offset < 0 && this.currentPage < getPagesCount()) {
                pageForward();
                return true;
            } else if (offset > 0 && this.currentPage > -1) {
                pageBack();
                return true;
            }
            return false;
        }
    }

    @Override
    public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURE);

        if (currentPage < 0) {
            blit(stack, leftPos + bookWidth - scaledWidth(146), topPos, 0, 0, 146, 180);
        } else if(currentPage >= getPagesCount()) {
            blit(stack, leftPos, topPos, 147, 0, 146, 180);
        } else {
            blit(stack, leftPos, topPos, 0, 181, 279, 180);
            if(Integer.valueOf(currentPage).equals(playerBookmark)) {
                blit(stack, leftPos + scaledWidth(136), topPos + scaledHeight(8), 280, 181, 7, 162);
            }
            pages.get(this.currentPage * 2).render(stack, mouseX, mouseY, partialTicks, this.leftPos + scaledWidth(14));
            pages.get(this.currentPage * 2 + 1).render(stack, mouseX, mouseY, partialTicks, this.leftPos + scaledWidth(143));
        }

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void blit(@Nonnull PoseStack stack, int x, int y, int u, int v, int width, int height) {
        blit(stack, x, y, scaledWidth(width), scaledHeight(height), u, v, width, height, 512, 512);
    }

    public static void fillGradient(@Nonnull PoseStack stack, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int blitOffset) {
        GuiComponent.fillGradient(stack, x1, y1, x2, y2, colorFrom, colorTo, blitOffset);
    }

    @Override
    public void removed() {
        super.removed();

        int bookmarkedPage = playerBookmark == null ? -1 : playerBookmark;
        int pagesAmount = PAGES.size();

        if(minecraft != null && minecraft.player != null) {
            Player player = minecraft.player;
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack heldItem = player.getItemInHand(hand);
                if(!heldItem.is(MineriaItems.APOTHECARIUM.get())) {
                    continue;
                }
                ApothecariumItem.savePlayerBookmarkData(heldItem, bookmarkedPage, pagesAmount);
                break;
            }
        }
        MineriaPacketHandler.PACKET_HANDLER.sendToServer(new SavePlayerBookmarkMessageHandler.SavePlayerBookmarkMessage(bookmarkedPage, pagesAmount));
    }

    private static final ResourceLocation COMIC_SANS_MS = new ResourceLocation(Mineria.MODID, "comic");

    private static ResourceLocation getFontLocation() {
        return MineriaUtils.currentDateMatches(4, 1) ? COMIC_SANS_MS : getFontFromConfig();
    }

    private static ResourceLocation getFontFromConfig() {
        String font;
        try {
            font = MineriaConfig.CLIENT.apothecariumFont.get();
        } catch (Throwable t) {
            font = "lcallig";
        }
        return "default".equals(font) ? new ResourceLocation(font) : new ResourceLocation(Mineria.MODID, font);
    }

    public class TurnPageButton extends Button {
        private final boolean isForward;

        public TurnPageButton(int x, int y, boolean isForward, OnPress onPressed) {
            super(Button.builder(Component.empty(), onPressed).bounds(x, y, scaledWidth(23), scaledHeight(13)));
            this.isForward = isForward;
        }

        @Override
        public void renderButton(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
            int textureX = 294;
            int textureY = 0;
            if (this.isHovered) {
                textureX += 23;
            }
            if (!this.isForward) {
                textureY += 13;
            }

            ApothecariumScreen.this.blit(stack, this.getX(), this.getY(), textureX, textureY, 23, 13);
        }

        @Override
        public void playDownSound(@Nonnull SoundManager handler) {
        }
    }

    public class BookmarkButton extends Button {
        private final boolean reversed;
        private final ApothecariumBookmarkInfo bookmarkInfo;

        public BookmarkButton(int x, int y, boolean reversed, ApothecariumBookmarkInfo bookmarkInfo, OnPress action) {
            super(Button.builder(bookmarkInfo.displayName(), action).bounds(x, y, scaledWidth(21), scaledHeight(22)));
            this.reversed = reversed;
            this.bookmarkInfo = bookmarkInfo;
        }

        @Override
        public void renderButton(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int x = getX();
            int y = getY();
            int width = 21;
            int textureX = 294;
            int textureY = 29;
            if(isHovered) {
                if(reversed) {
                    x -= scaledWidth(2);
                }
                width += 2;
                textureX += 22;
            }
            if(reversed) {
                textureY += 23;
            }

            ApothecariumScreen.this.blit(stack, x, y, textureX, textureY, width, 22);

            float itemSize = scaledWidth(21) / 1.5f;
            if (ApothecariumScreen.this.minecraft != null) {
                ItemPage.renderGuiItem(ApothecariumScreen.this.minecraft, bookmarkInfo.displayStack(), x + (scaledWidth(width) - itemSize) / 2f, y + (scaledHeight(22) - itemSize) / 2f, itemSize);
            }

            if(isHovered) {
                renderTooltip(stack, bookmarkInfo.displayName().copy().withStyle(style -> style.withFont(Style.DEFAULT_FONT)), mouseX, mouseY);
            }
        }
    }

    public class PlayerBookmarkButton extends Button {
        public PlayerBookmarkButton(int x, int y, OnPress onPressed) {
            super(Button.builder(Component.empty(), onPressed).bounds(x, y, scaledWidth(7), scaledHeight(16)));
        }

        @Override
        public void renderButton(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int textureY = 344;
            int height = 16;
            if(isHovered) {
                textureY = 342;
                height = 18;
            }

            ApothecariumScreen.this.blit(stack, getX(), getY(), 280, textureY, 7, height);

            if(isHovered) {
                Component tooltip;
                if(ApothecariumScreen.this.playerBookmark == null) {
                    tooltip = Component.translatable("mineria.apothecarium.player_bookmark.mark");
                } else if(ApothecariumScreen.this.playerBookmark == ApothecariumScreen.this.currentPage) {
                    tooltip = Component.translatable("mineria.apothecarium.player_bookmark.unmark");
                } else if(Screen.hasShiftDown()) {
                    tooltip = Component.translatable("mineria.apothecarium.player_bookmark.mark");
                } else {
                    tooltip = Component.translatable("mineria.apothecarium.player_bookmark.go_to_page");
                }
                renderTooltip(stack, tooltip, mouseX, mouseY);
            }
        }
    }
}
