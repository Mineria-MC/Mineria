package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.init.MineriaProfessions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Shadow
    public abstract VillagerData getVillagerData();

    @ModifyConstant(method = "updateTrades", constant = @Constant(intValue = 2))
    private int mineria$modify_maxTrades(int maxTrades) {
        return getMaxTrades(getVillagerData());
    }

    private static int getMaxTrades(VillagerData data) {
        VillagerProfession profession = data.getProfession();
        return profession == MineriaProfessions.APOTHECARY.get() ? 4 : 2;
    }
}
