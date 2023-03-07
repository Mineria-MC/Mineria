package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumBookmarkInfo;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import io.github.mineria_mc.mineria.client.screens.apothecarium.page_sets.PageSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class PartTitlePage extends ApothecariumPage {
    private final Component title;
    private final Lazy<ItemStack> displayStack;

    public PartTitlePage(PageCreationContext ctx, Component title, Supplier<ItemStack> displayStack) {
        super(ctx);
        this.title = title;
        this.displayStack = Lazy.of(displayStack);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x) {
        stack.pushPose();
        float scaleFactor = findFittingScale(title, height / 90f, width / 12f);
        stack.translate(x + (width - font.width(title) * scaleFactor) / 2f, y + (height - (font.lineHeight - 1)) / 2f, 0);
        stack.scale(scaleFactor, scaleFactor, 1);
        font.draw(stack, title, 0, 0, 0);
        stack.popPose();
    }

    @Override
    public ApothecariumBookmarkInfo bookmarkInfo() {
        return new ApothecariumBookmarkInfo(title.plainCopy(), displayStack.get());
    }

    public static PageSet create(Component title, Supplier<ItemStack> displayStack) {
        return PageSet.singleton(ctx -> new PartTitlePage(ctx, title, displayStack), PageSet.PageStart.ODD);
    }
}
