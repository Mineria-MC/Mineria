package com.mineria.mod.mixin;

import com.mineria.mod.common.entity.profession.IMineriaProfession;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin
{
    @Shadow public abstract VillagerData getVillagerData();

    @ModifyConstant(method = "updateTrades", constant = @Constant(intValue = 2))
    private int getMaxTrades(int maxTrades)
    {
        VillagerProfession profession = this.getVillagerData().getProfession();
        int level = this.getVillagerData().getLevel();
        return profession instanceof IMineriaProfession ? ((IMineriaProfession) profession).getMaxTrades(level) : 2;
    }
}
