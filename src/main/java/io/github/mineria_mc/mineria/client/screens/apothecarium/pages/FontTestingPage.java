package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.PageSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class FontTestingPage extends ApothecariumPage {
    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet," +
            "consectetur adipisicing elit. Adipisci animi aut blanditiis culpa cum distinctio dolore earum magnam molestiae neque nobis obcaecati perferendis," +
            "praesentium quis quod recusandae repellendus saepe! A autem, cum ea earum eos mollitia odio porro provident suscipit? Alias consequatur doloremque" +
            "eligendi impedit odit quae quaerat quidem, sequi.";
    private static final String ALPHABET = "a brown fox jumped over the lazy dogs quickly. A BROWN FOX JUMPED OVER THE LAZY DOGS QUICKLY. 1234567890.:,;'\"(!?)+-*/=";

    private final ResourceLocation fontLocation;
    private final Component text;

    public FontTestingPage(PageCreationContext ctx, ResourceLocation fontLocation) {
        super(ctx);
        this.fontLocation = fontLocation;
        this.text = Component.literal(ALPHABET).withStyle(style -> style.withFont(fontLocation));
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x) {
        font.draw(stack, Component.literal(fontLocation.getPath()), x, y, 0);

        float scaleFactor = height / 90f;
        int maxLength = (int) (width / scaleFactor);
        List<FormattedCharSequence> processed = font.split(text, maxLength);
        for (int i = 0; i < processed.size(); i++) {
            FormattedCharSequence chars = processed.get(i);
            stack.pushPose();
            stack.translate(x, y + 20 + (font.lineHeight - 1) * scaleFactor * i, 0);
            stack.scale(scaleFactor, scaleFactor, 1);
            font.draw(stack, chars, 0, 0, 0);
            stack.popPose();
        }
    }

    public static PageSet create(ResourceLocation fontLocation) {
        return PageSet.singleton(ctx -> new FontTestingPage(ctx, fontLocation));
    }
}
