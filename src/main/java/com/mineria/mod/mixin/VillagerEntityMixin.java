package com.mineria.mod.mixin;

import com.mineria.mod.init.ProfessionsInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends AbstractVillagerEntity
{
    @Shadow public abstract VillagerData getVillagerData();

    public VillagerEntityMixin(EntityType<? extends AbstractVillagerEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    @Inject(method = "populateTradeData", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/merchant/villager/VillagerEntity;addTrades(Lnet/minecraft/item/MerchantOffers;[Lnet/minecraft/entity/merchant/villager/VillagerTrades$ITrade;I)V", shift = At.Shift.BEFORE), cancellable = true)
    public void populateTradeData(CallbackInfo ci)
    {
        this.addTrades(this.getOffers(), VillagerTrades.VILLAGER_DEFAULT_TRADES.get(this.getVillagerData().getProfession()).get(this.getVillagerData().getLevel()), getMaxTrades(this.getVillagerData().getProfession()));
        ci.cancel();
    }

    private static int getMaxTrades(VillagerProfession profession)
    {
        if(profession == ProfessionsInit.APOTHECARY.get())
            return 4;
        return 2;
    }
}
