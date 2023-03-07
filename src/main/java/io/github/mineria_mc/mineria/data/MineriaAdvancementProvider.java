package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MineriaAdvancementProvider extends ForgeAdvancementProvider {
    public MineriaAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper) {
        super(output, registries, helper, List.of());
    }

    private static class MineriaAdvancements implements AdvancementGenerator {
        private static final ResourceLocation ADVANCEMENTS_BACKGROUND = new ResourceLocation(Mineria.MODID, "textures/block/blue_glowstone.png");
        private Consumer<Advancement> saver;
        private ExistingFileHelper helper;

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper helper) {
            this.saver = saver;
            this.helper = helper;
            save("root",
                    info -> info.setIcon(MineriaBlocks.LONSDALEITE_BLOCK.get()).setShowToast(false).setAnnounceChat(false).setBackground(ADVANCEMENTS_BACKGROUND),
                    builder -> builder
                            .addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRAFTING_TABLE))
            );
        }

        private Advancement save(String id, Consumer<MutableDisplayInfo> displayConsumer, Consumer<Advancement.Builder> builderConsumer) {
            if(saver == null || helper == null) {
                throw new RuntimeException();
            }

            Advancement.Builder builder = Advancement.Builder.advancement();
            MutableDisplayInfo display = MutableDisplayInfo.fromId(id);
            displayConsumer.accept(display);
            builder.display(display.toImmutable());
            builderConsumer.accept(builder);
            return builder.save(saver, new ResourceLocation(Mineria.MODID, id), helper);
        }
    }

    private static class MutableDisplayInfo extends DisplayInfo {
        private ItemStack icon;
        private Component title;
        private Component description;
        @Nullable
        private ResourceLocation background;
        private FrameType frame;
        private boolean showToast;
        private boolean announceChat;
        private boolean hidden;

        public MutableDisplayInfo(ItemStack icon, Component title, Component desc, @Nullable ResourceLocation bg, FrameType frame, boolean showToast, boolean announce, boolean hidden) {
            super(icon, title, desc, bg, frame, showToast, announce, hidden);
            this.icon = icon;
            this.title = title;
            this.description = desc;
            this.background = bg;
            this.frame = frame;
            this.showToast = showToast;
            this.announceChat = announce;
            this.hidden = hidden;
        }

        @Nonnull
        @Override
        public ItemStack getIcon() {
            return icon;
        }

        @Nonnull
        @Override
        public Component getTitle() {
            return title;
        }

        @Nonnull
        @Override
        public Component getDescription() {
            return description;
        }

        @Nullable
        @Override
        public ResourceLocation getBackground() {
            return background;
        }

        @Nonnull
        @Override
        public FrameType getFrame() {
            return frame;
        }

        @Override
        public boolean shouldShowToast() {
            return showToast;
        }

        @Override
        public boolean shouldAnnounceChat() {
            return announceChat;
        }

        @Override
        public boolean isHidden() {
            return hidden;
        }

        public MutableDisplayInfo setIcon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public MutableDisplayInfo setIcon(ItemLike icon) {
            return setIcon(new ItemStack(icon));
        }

        public MutableDisplayInfo setTitle(Component title) {
            this.title = title;
            return this;
        }

        public MutableDisplayInfo setDescription(Component description) {
            this.description = description;
            return this;
        }

        public MutableDisplayInfo setBackground(@Nullable ResourceLocation background) {
            this.background = background;
            return this;
        }

        public MutableDisplayInfo setFrame(FrameType frame) {
            this.frame = frame;
            return this;
        }

        public MutableDisplayInfo setShowToast(boolean showToast) {
            this.showToast = showToast;
            return this;
        }

        public MutableDisplayInfo setAnnounceChat(boolean announceChat) {
            this.announceChat = announceChat;
            return this;
        }

        public MutableDisplayInfo setHidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public DisplayInfo toImmutable() {
            return new DisplayInfo(icon, title, description, background, frame, showToast, announceChat, hidden);
        }

        public static MutableDisplayInfo fromId(String id) {
            String advancementPrefix = "advancements." + Mineria.MODID + "." + id;
            return new MutableDisplayInfo(ItemStack.EMPTY, Component.translatable(advancementPrefix + ".title"), Component.translatable(advancementPrefix + ".description"), null, FrameType.TASK, true, true, false);
        }
    }
}
