package io.github.mineria_mc.mineria.common.effects.instances;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ModdedMobEffectInstance extends MobEffectInstance {
    protected MobEffect potion;
    protected int duration;
    protected int maxDuration;
    protected int amplifier;
    protected boolean ambient;
    protected boolean showParticles;
    protected boolean showIcon;
    protected List<ItemStack> curativeItems;
    protected boolean shouldRender;
    @Nullable
    protected final MobEffect parentEffect;

    public ModdedMobEffectInstance(MobEffect potion, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean shouldRender, @Nullable MobEffect parentEffect) {
        super(potion, duration, amplifier, ambient, showParticles, showIcon);
        this.potion = potion;
        this.duration = duration;
        this.maxDuration = maxDuration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
        this.curativeItems = new ArrayList<>();
        this.shouldRender = shouldRender;
        this.parentEffect = parentEffect;
    }

    public <T extends ModdedMobEffectInstance, B extends Builder<T, B>> ModdedMobEffectInstance(Builder<T, B> builder) {
        super(builder.effect, builder.duration, builder.amplifier, builder.ambient, builder.showParticles, builder.showIcon);
        this.potion = builder.effect;
        this.duration = builder.duration;
        this.maxDuration = builder.maxDuration;
        this.amplifier = builder.amplifier;
        this.ambient = builder.ambient;
        this.showParticles = builder.showParticles;
        this.showIcon = builder.showIcon;
        this.curativeItems = builder.curativeItems;
        this.shouldRender = builder.shouldRender;
        this.parentEffect = builder.parentEffect;
    }

    @Override
    public abstract boolean update(@Nonnull MobEffectInstance effectInstance);

    @Nonnull
    public MobEffect getEffect() {
        return this.potion;
    }

    public int getDuration() {
        return this.duration;
    }

    // Let's allow mutability to avoid creating new instances when we want to modify existing one.
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public void setAmbient(boolean ambient) {
        this.ambient = ambient;
    }

    public boolean isVisible() {
        return this.showParticles;
    }

    public void setVisible(boolean showParticles) {
        this.showParticles = showParticles;
    }

    public boolean showIcon() {
        return this.showIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public Optional<MobEffectInstance> getActiveParentEffect(LivingEntity living) {
        return Optional.ofNullable(this.parentEffect == null ? null : living.getEffect(this.parentEffect));
    }

    @Nullable
    public MobEffect getParentEffect() {
        return this.parentEffect;
    }

    public abstract ResourceLocation getSerializerId();

    @Override
    public boolean tick(@Nonnull LivingEntity living, @Nonnull Runnable onChangedPotionEffect) {
        if (this.duration > 0) {
            if (this.isReady())
                this.applyEffect(living);

            --this.duration;
        }

        return this.duration > 0;
    }

    public boolean isReady() {
        return this.potion.isDurationEffectTick(this.duration, this.amplifier);
    }

    @Override
    public void applyEffect(@Nonnull LivingEntity living) {
        if (duration > 0) {
            this.potion.applyEffectTick(living, this.amplifier);
        }
    }

    @Nonnull
    @Override
    public String getDescriptionId() {
        return this.potion.getDescriptionId();
    }

    @Nonnull
    @Override
    public String toString() {
        String text;
        if (this.amplifier > 0) {
            text = this.getDescriptionId() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration + ", Max Duration: " + this.maxDuration;
        } else {
            text = this.getDescriptionId() + ", Duration: " + this.duration + ", Max Duration: " + this.maxDuration;
        }

        if (!this.showParticles) {
            text = text + ", Particles: false";
        }

        if (!this.showIcon) {
            text = text + ", Show Icon: false";
        }

        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof ModdedMobEffectInstance effect)) {
            return false;
        }
        else {
            return this.duration == effect.duration && this.amplifier == effect.amplifier && this.ambient == effect.ambient && this.potion.equals(effect.potion);
        }
    }

    @Override
    public int hashCode() {
        int i = this.potion.hashCode();
        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        return 31 * i + (this.ambient ? 1 : 0);
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt("MaxDuration", this.getMaxDuration());
        nbt.putString("Serializer", this.getSerializerId().toString());
        nbt.putBoolean("ShouldRender", this.shouldRender());
        if (this.parentEffect != null) {
            nbt.putString("ParentEffect", ForgeRegistries.MOB_EFFECTS.getKey(parentEffect).toString());
        }
        return nbt;
    }

    public void drawPotionName(Font font, PoseStack matrixStack, float x, float y) {
        font.drawShadow(matrixStack, Component.translatable(this.getDescriptionId()), x, y, 16777215);
    }

    public int getColor() {
        return this.potion.getColor();
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return curativeItems;
    }

    @Override
    public void setCurativeItems(@Nonnull List<ItemStack> curativeItems) {
        this.curativeItems = curativeItems;
    }

    public abstract ModdedMobEffectInstance copy();

    public static MobEffectInstance copyEffect(MobEffectInstance effect) {
        return effect instanceof ModdedMobEffectInstance modded ? modded.copy() : new MobEffectInstance(effect);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <I extends ModdedMobEffectInstance> I getEffectSafe(LivingEntity living, MobEffect effect) {
        try {
            return (I) living.getEffect(effect);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends ModdedMobEffectInstance, B extends Builder<T, B>> {
        protected final MobEffect effect;
        protected int duration = 0;
        protected int maxDuration = 0;
        protected int amplifier = 0;
        protected boolean ambient;
        protected boolean showParticles = true;
        protected boolean showIcon = true;
        protected boolean shouldRender = true;
        protected List<ItemStack> curativeItems = new ArrayList<>();
        @Nullable
        protected MobEffect parentEffect;

        public Builder(MobEffect effect) {
            this.effect = effect;
        }

        public B duration(int duration) {
            this.duration = duration;
            this.maxDuration = duration;
            return (B) this;
        }

        public B maxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return (B) this;
        }

        public B amplifier(int amplifier) {
            this.amplifier = amplifier;
            return (B) this;
        }

        public B ambient() {
            return ambient(true);
        }

        public B ambient(boolean value) {
            this.ambient = value;
            return (B) this;
        }

        public B hideParticles() {
            return showParticles(false);
        }

        public B showParticles(boolean value) {
            this.showParticles = value;
            return (B) this;
        }

        public B hideIcon() {
            return showIcon(false);
        }

        public B showIcon(boolean value) {
            this.showIcon = value;
            return (B) this;
        }

        public B noInGuiRendering() {
            return shouldRenderInGui(false);
        }

        public B shouldRenderInGui(boolean value) {
            this.shouldRender = value;
            return (B) this;
        }

        public B withCurativeItems(ItemStack... stacks) {
            this.curativeItems.addAll(Arrays.asList(stacks));
            return (B) this;
        }

        public B curativeItems(List<ItemStack> stacks) {
            this.curativeItems = new ArrayList<>(stacks);
            return (B) this;
        }

        public B parentEffect(MobEffect effect) {
            this.parentEffect = effect;
            return (B) this;
        }

        public abstract T build();
    }
}
