package com.mineria.mod.mixin;

import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.extensions.IForgeMobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements IForgeMobEffectInstance
{
    @Inject(method = "save", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    public void write(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir)
    {
        nbt.putString("Serializer", "mineria:default");
//        nbt.putBoolean("ShouldRender", this.shouldRender());
    }

    @Inject(method = "loadSpecifiedEffect", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
    private static void readInternal(MobEffect effect, CompoundTag nbt, CallbackInfoReturnable<MobEffectInstance> cir)
    {
        if(nbt.contains("Serializer"))
            cir.setReturnValue(MineriaEffectInstanceSerializers.byName(new ResourceLocation(nbt.getString("Serializer"))).deserialize(effect, nbt));
    }
}
