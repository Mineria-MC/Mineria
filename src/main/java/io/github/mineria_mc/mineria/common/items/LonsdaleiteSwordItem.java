package io.github.mineria_mc.mineria.common.items;

import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.world.item.SwordItem;

import javax.annotation.Nonnull;

public class LonsdaleiteSwordItem extends SwordItem {
    private String translationKey;

    public LonsdaleiteSwordItem() {
        super(MineriaItem.ItemTier.LONSDALEITE, 3, -2.4F, new Properties());
    }

    @Nonnull
    @Override
    protected String getOrCreateDescriptionId() {
        if(translationKey == null) {
            if(MineriaUtils.currentDateMatches(6, 26)) {
                translationKey = "item.mineria.mrlulu_sword";
            } else if(MineriaUtils.currentDateMatches(11, 10)) {
                translationKey = "item.mineria.mathys_craft_sword";
            } else {
                translationKey = super.getOrCreateDescriptionId();
            }
        }

        return translationKey;
    }
}
