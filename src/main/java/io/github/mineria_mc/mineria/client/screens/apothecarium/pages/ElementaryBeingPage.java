package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.PageSet;
import io.github.mineria_mc.mineria.common.entity.ElementaryGolemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ElementaryBeingPage<E extends ElementaryGolemEntity> extends PartitionedPage {
    private final Pair<EntityType<E>, ResourceLocation> entityType;
    @Nullable
    private final EntityStateUpdater<E> stateUpdater;
    private E entity;

    public ElementaryBeingPage(PageCreationContext ctx, EntityType<E> type, @Nullable EntityStateUpdater<E> stateUpdater) {
        super(ctx);
        this.entityType = Pair.of(type, ForgeRegistries.ENTITY_TYPES.getKey(type));
        this.stateUpdater = stateUpdater;
        if(client.level != null) {
            createEntity(client.level);
        }
    }

    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
    private static final ResourceLocation IRON_SWORD = new ResourceLocation("textures/item/iron_sword.png");

    private float time;

    @Override
    protected void initParts(List<RenderPart> parts) {
        if(client.level == null) {
            return;
        }
        if(entity == null) {
            createEntity(client.level);
        }

        // Title
        Component title = Component.translatable("mineria.apothecarium.elementary_beings." + entityType.getSecond().getPath()).withStyle(style -> style.withColor(entity.getCharacteristicColor()));
        font.tryApplyFont(title);
        float titleScale = height / 80f;
        parts.add(scaledText(title.getVisualOrderText(), x -> x + (width - font.width(title) * titleScale) / 2f, y, titleScale));

        // Details such as HP and Attack Damage
        float detailsScale = height * 0.75f / 10f;
        float detailsTextScale = detailsScale / (font.lineHeight - 1);
        float detailsY = y + height * 1.5f / 10f;
        int iDetailsScale = Mth.floor(detailsScale);
        int iDetailsY = Mth.floor(detailsY);
        Component healthComponent = Component.literal(floatToString(entity.getMaxHealth() / 2f));
        parts.add(partial((graphics, x) -> {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            graphics.blit(ICONS, x, iDetailsY, iDetailsScale, iDetailsScale, 16, 0, 9, 9, 256, 256);
            graphics.blit(ICONS, x, iDetailsY, iDetailsScale, iDetailsScale, 52, 0, 9, 9, 256, 256);

            PoseStack stack = graphics.pose();

            stack.pushPose();
            stack.translate(x + detailsScale * 1.1f, detailsY, 0);
            stack.scale(detailsTextScale, detailsTextScale, detailsTextScale);
            font.draw(graphics, healthComponent, 0, 0, 0);
            stack.popPose();
        }));
        Component attackDamageComponent = Component.literal(floatToString(entity.getMinAttackDamage())).append("-").append(floatToString(entity.getMaxAttackDamage()));
        int detailsWidth = Mth.floor(detailsScale * 1.1f + font.width(attackDamageComponent) * detailsTextScale);
        parts.add(partial((graphics, x) -> {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            graphics.blit(IRON_SWORD, x + width - detailsWidth, iDetailsY, iDetailsScale, iDetailsScale, 0, 0, 16, 16, 16, 16);

            PoseStack stack = graphics.pose();

            stack.pushPose();
            stack.translate(x + width - detailsWidth + detailsScale * 1.1f, detailsY, 0);
            stack.scale(detailsTextScale, detailsTextScale, detailsTextScale);
            font.draw(graphics, attackDamageComponent, 0, 0, 0);
            stack.popPose();
        }));

        // Entity
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        parts.add((graphics, mouseX, mouseY, partialTicks, x) -> {
            if(stateUpdater != null) {
                if(!Screen.hasControlDown()) {
                    time += partialTicks;
                    if(Math.floor(time) % 30 == 0) {
                        this.stateUpdater.changeEntityState(entity, time);
                    }
                }
            }
            renderEntity(entity, x + (width - dimensions.width) / 2f, y + height * 2.5f / 10f, (height * 4.5f / 10f) / dimensions.height);
        });

        // Description
        float descriptionTextScale = height / 250f;
        Component descriptionComponent = Component.translatable("mineria.apothecarium.elementary_beings.description").withStyle(ChatFormatting.DARK_BLUE).append(": ")
                .append(Component.translatable("mineria.apothecarium.elementary_beings." + entityType.getSecond().getPath() + ".description").withStyle(ChatFormatting.BLACK));
        List<FormattedCharSequence> descriptionContent = font.split(descriptionComponent, (int) (width / descriptionTextScale));
        for (int i = 0; i < descriptionContent.size(); i++) {
            if(i == 5) {
                break;
            }
            parts.add(scaledText(descriptionContent.get(i), y + height * 7.5f / 10f + (font.lineHeight - 1) * descriptionTextScale * i, descriptionTextScale));
        }

        // Ability
        float abilityTextScale = height / 260f;
        Component abilityComponent = Component.translatable("mineria.apothecarium.elementary_beings.ability").withStyle(ChatFormatting.RED).append(": ")
                .append(Component.translatable("mineria.apothecarium.elementary_beings." + entityType.getSecond().getPath() + ".ability").withStyle(ChatFormatting.BLACK));
        List<FormattedCharSequence> abilityContent = font.split(abilityComponent, (int) (width / abilityTextScale));
        for (int i = 0; i < abilityContent.size(); i++) {
            if(i == 5) {
                break;
            }
            parts.add(scaledText(abilityContent.get(i), y + height * 9f / 10f + (font.lineHeight - 1) * abilityTextScale * i, abilityTextScale));
        }
    }

    private void createEntity(@Nonnull Level level) {
        entity = entityType.getFirst().create(level);
        if(entity == null) {
            throw new RuntimeException("Entity type for '" + entityType.getSecond() + "' entity is broken! Can't create a new entity.");
        }
        entity.yBodyRot = 180 + 45;
        entity.setYRot(180F + 45);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
    }

    private static String floatToString(float value) {
        return value == Math.ceil(value) ? Integer.toString((int) value) : Float.toString(value);
    }

    public static <E extends ElementaryGolemEntity> PageSet create(Supplier<EntityType<E>> typeSupplier) {
        return create(typeSupplier, null);
    }

    public static <E extends ElementaryGolemEntity> PageSet create(Supplier<EntityType<E>> typeSupplier, @Nullable EntityStateUpdater<E> updater) {
        return PageSet.singleton(ctx -> new ElementaryBeingPage<>(ctx, typeSupplier.get(), updater));
    }

    @FunctionalInterface
    public interface EntityStateUpdater<E extends ElementaryGolemEntity> {
        void changeEntityState(E entity, float time);
    }
}
