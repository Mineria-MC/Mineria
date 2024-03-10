package io.github.mineria_mc.mineria.common.item;

import io.github.mineria_mc.mineria.util.MineriaUtil;
import net.minecraft.world.item.SwordItem;

public class LonsdaleiteSwordItem extends SwordItem {

    private String translationKey;

    public LonsdaleiteSwordItem() {
        super(MineriaItem.ItemTier.LONSDALEITE, 3, -2.4f, new Properties());
    }

    @Override
    protected String getOrCreateDescriptionId() {
        if(translationKey == null) {
            if(MineriaUtil.currentDateMatches(6, 26)) {
                translationKey = "item.mineria.mrlulu_sword";
            } else if(MineriaUtil.currentDateMatches(11, 10)) {
                translationKey = "item.mineria.mathys_craft_sword";
            } else {
                translationKey = super.getOrCreateDescriptionId();
            }
        }

        return translationKey;
    }
}
