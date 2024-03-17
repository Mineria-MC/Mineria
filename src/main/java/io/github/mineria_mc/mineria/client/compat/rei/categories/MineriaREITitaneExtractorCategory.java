package io.github.mineria_mc.mineria.client.compat.rei.categories;

import java.util.LinkedList;
import java.util.List;

import io.github.mineria_mc.mineria.client.compat.rei.displays.MineriaREITitaneExtractorDisplay;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.util.Constants;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MineriaREITitaneExtractorCategory implements DisplayCategory<BasicDisplay> {
    public static final ResourceLocation TITANE_EXTRACTOR_GUI = new ResourceLocation(Constants.MODID, "textures/gui/titane_extractor.png");
    public static final CategoryIdentifier<MineriaREITitaneExtractorDisplay> TITANE_EXTRACTOR = CategoryIdentifier.of(Constants.MODID, "titane_extractor");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return TITANE_EXTRACTOR;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Titane Extractor");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(MineriaBlockRegistry.getItemFromBlock(MineriaBlockRegistry.TITANE_EXTRACTOR));
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 88, bounds.getCenterY() - 91);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TITANE_EXTRACTOR_GUI, new Rectangle(startPoint.x, startPoint.y, 176, 183)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y + 7)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 41, startPoint.y + 7)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 24, startPoint.y + 78)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 47)));

        return widgets;
    }
}
