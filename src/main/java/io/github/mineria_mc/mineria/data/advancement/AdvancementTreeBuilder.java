package io.github.mineria_mc.mineria.data.advancement;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class AdvancementTreeBuilder {
    private static final ExistingFileHelper.ResourceType ADVANCEMENTS = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", "advancements");

    private AdvancementBuilder root;
    @Nonnull
    private String group = "";
    @Nonnull
    private ResourceLocation background = new ResourceLocation("textures/block/dirt.png");

    public AdvancementTreeBuilder group(@Nonnull String group) {
        this.group = group;
        return this;
    }

    public AdvancementTreeBuilder background(@Nonnull ResourceLocation background) {
        this.background = background;
        return this;
    }

    public AdvancementTreeBuilder root(String id, Consumer<AdvancementBuilder> consumer) {
        return root(new ResourceLocation(Mineria.MODID, group + "/" + id), consumer);
    }

    public AdvancementTreeBuilder root(ResourceLocation id, Consumer<AdvancementBuilder> consumer) {
        AdvancementBuilder builder = new AdvancementBuilder(group, null, id);
        consumer.accept(builder);
        root = builder;
        return this;
    }

    public void saveAll(Consumer<Advancement> saver, ExistingFileHelper helper) {
        if(root != null) {
            List<Advancement> toSave = new ArrayList<>();
            Advancement rootAdvancement = root.buildRoot(background, toSave);
            Collections.reverse(toSave);
            helper.trackGenerated(rootAdvancement.getId(), ADVANCEMENTS);
            saver.accept(rootAdvancement);
            toSave.forEach(advancement -> {
                Advancement parent = advancement.getParent();
                if (parent != null && helper.exists(parent.getId(), ADVANCEMENTS)) {
                    helper.trackGenerated(advancement.getId(), ADVANCEMENTS);
                    saver.accept(advancement);
                }
            });
        }
    }
}
