package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class DruidSummoningRitualPage extends PartitionedPage {
    private LivingEntity druid;

    public DruidSummoningRitualPage(PageCreationContext ctx) {
        super(ctx);
        if(client.level != null) {
            createDruid(client.level);
        }
    }

    private static Component stepText(int step) {
        return Component.translatable("mineria.apothecarium.druid_rites.step", step).withStyle(ChatFormatting.RED)
                .append(": ")
                .append(Component.translatable("mineria.apothecarium.druid_rites.druid_summoning.step_" + step).withStyle(ChatFormatting.BLACK));
    }

    @Override
    protected void initParts(List<RenderPart> parts) {
        // Title
        Component title = Component.translatable("mineria.apothecarium.druid_rites.druid_summoning.title").withStyle(ChatFormatting.DARK_RED);
        float titleScale = findFittingScale(title, height / 80f, width / 12f);
        parts.add(scaledText(title.getVisualOrderText(), x -> x + (width - font.width(title) * titleScale) / 2f, y, titleScale));

        // Step 1
        float textScale = height / 180f;
        List<FormattedCharSequence> step1Content = font.split(stepText(1), (int) (width / textScale));
        for (int i = 0; i < step1Content.size(); i++) {
            if(i == 2) {
                break;
            }
            parts.add(scaledText(step1Content.get(i), y + height * 2 / 10f + (font.lineHeight - 1) * textScale * i, textScale));
        }

        // Step 2
        List<FormattedCharSequence> step2Content = font.split(stepText(2), (int) (width / textScale));
        for (int i = 0; i < step2Content.size(); i++) {
            if(i == 3) {
                break;
            }
            parts.add(scaledText(step2Content.get(i), y + height * 3 / 10f + (font.lineHeight - 1) * textScale * i, textScale));
        }

        // Note
        float noteTextScale = height / 240f;
        Component noteText = Component.translatable("mineria.apothecarium.druid_rites.note").withStyle(ChatFormatting.DARK_AQUA).append(": ")
                .append(Component.translatable("mineria.apothecarium.druid_rites.druid_summoning.step_2.note").withStyle(ChatFormatting.BLACK));
        List<FormattedCharSequence> note2Content = font.split(noteText, (int) (width / noteTextScale));
        for (int i = 0; i < note2Content.size(); i++) {
            if(i == 3) {
                break;
            }
            parts.add(scaledText(note2Content.get(i), y + height * 4.5f / 10f + (font.lineHeight - 1) * noteTextScale * i, noteTextScale));
        }

        // Druid
        if(client.level != null) {
            if(druid == null) {
                createDruid(client.level);
            }
            parts.add(partial((stack, x) -> renderEntity(druid, x + width / 2f, y + height * 6f / 10f, (height * 4f / 10f) / druid.getDimensions(druid.getPose()).height)));
        }
    }

    private void createDruid(@Nonnull Level level) {
        druid = Optional.ofNullable(ForgeRegistries.ENTITY_TYPES.tags())
                .flatMap(tags -> tags.getTag(MineriaEntities.Tags.DRUIDS).getRandomElement(level.random))
                .map(entityType -> entityType.create(level))
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast)
                .orElseGet(() -> MineriaEntities.DRUID.get().create(level));
        if(druid == null) {
            throw new RuntimeException("Entity type for 'mineria:druid' entity is broken! Can't create a new entity.");
        }
        druid.yBodyRot = 180 + 45;
        druid.setYRot(180F + 45);
        druid.yHeadRot = druid.getYRot();
        druid.yHeadRotO = druid.getYRot();
    }
}
