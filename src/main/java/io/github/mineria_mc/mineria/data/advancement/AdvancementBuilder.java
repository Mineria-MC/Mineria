package io.github.mineria_mc.mineria.data.advancement;

import com.google.common.collect.Maps;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.Util;
import net.minecraft.advancements.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AdvancementBuilder {
    private final String group;
    @Nullable
    private final ResourceLocation parentId;
    private final ResourceLocation id;
    private final CriteriaBuilder criteria = new CriteriaBuilder();
    private final List<AdvancementBuilder> children = new ArrayList<>();
    @Nonnull
    private ItemStack icon = ItemStack.EMPTY;
    @Nonnull
    private Component title, description;
    @Nonnull
    private FrameType frame = FrameType.TASK;
    @Nullable
    private Boolean showToast, announceChat, hidden;
    @Nonnull
    private AdvancementRewards rewards = AdvancementRewards.EMPTY;

    AdvancementBuilder(String group, @Nullable ResourceLocation parentId, ResourceLocation id) {
        this.group = group;
        this.parentId = parentId;
        this.id = id;
        this.title = Component.translatable("advancements." + id.getNamespace() + "." + id.getPath().substring(id.getPath().lastIndexOf('/') + 1) + ".title");
        this.description = Component.translatable("advancements." + id.getNamespace() + "." + id.getPath().substring(id.getPath().lastIndexOf('/') + 1) + ".description");
    }

    public AdvancementBuilder title(@Nonnull Component title) {
        this.title = title;
        return this;
    }

    public AdvancementBuilder description(@Nonnull Component description) {
        this.description = description;
        return this;
    }

    public AdvancementBuilder icon(@Nonnull ItemStack stack) {
        this.icon = stack;
        return this;
    }

    public AdvancementBuilder icon(@Nonnull ItemLike item) {
        return icon(new ItemStack(item));
    }

    public AdvancementBuilder icon(@Nonnull RegistryObject<? extends ItemLike> obj) {
        return icon(obj.map(ItemStack::new).orElse(ItemStack.EMPTY));
    }

    public AdvancementBuilder frame(@Nonnull FrameType frame) {
        this.frame = frame;
        return this;
    }

    public AdvancementBuilder showToast(@Nullable Boolean showToast) {
        this.showToast = showToast;
        return this;
    }

    public AdvancementBuilder announceChat(@Nullable Boolean announceChat) {
        this.announceChat = announceChat;
        return this;
    }

    public AdvancementBuilder hidden(@Nullable Boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public AdvancementBuilder criteria(Consumer<CriteriaBuilder.Empty> builder) {
        builder.accept(criteria.getEmptyBuilder());
        return this;
    }

    public AdvancementBuilder singleCriterion(CriterionTriggerInstance instance) {
        return criteria(empty -> empty.add("criterion", instance));
    }

    public AdvancementBuilder rewards(Consumer<AdvancementRewards.Builder> builder) {
        this.rewards = Util.make(new AdvancementRewards.Builder(), builder).build();
        return this;
    }

    public AdvancementBuilder children(Consumer<BuilderGetter> adder) {
        adder.accept(new BuilderGetter() {
            @Override
            public AdvancementBuilder create(ResourceLocation id) {
                return Util.make(new AdvancementBuilder(group, AdvancementBuilder.this.id, id), children::add);
            }

            @Override
            public AdvancementBuilder create(String id) {
                return create(new ResourceLocation(Mineria.MODID, group + "/" + id));
            }
        });
        return this;
    }

    protected Advancement build(List<Advancement> advancements) {
        children.forEach(child -> child.build(advancements));
        return Util.make(internalBuild(null), advancements::add);
    }

    public Advancement buildRoot(@Nonnull ResourceLocation background, List<Advancement> advancements) {
        children.forEach(child -> child.build(advancements));
        return internalBuild(background);
    }

    private Advancement internalBuild(@Nullable ResourceLocation background) {
        Advancement parent = parentId != null ? new Advancement(parentId, null, null, AdvancementRewards.EMPTY, Maps.newHashMap(), new String[0][], false) : null;
        return new Advancement(id, parent, makeDisplay(background), rewards, criteria.getCriteria(), criteria.getRequirements(), false);
    }

    private DisplayInfo makeDisplay(@Nullable ResourceLocation background) {
        return new DisplayInfo(icon, title, description, background, frame, showToast == null || showToast, announceChat == null || announceChat, hidden != null && hidden);
    }

    public interface BuilderGetter {
        AdvancementBuilder create(ResourceLocation id);

        AdvancementBuilder create(String id);
    }
}
