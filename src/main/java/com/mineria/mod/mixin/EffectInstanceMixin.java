package com.mineria.mod.mixin;

import com.mineria.mod.effects.IEffectInstanceSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin implements IForgeEffectInstance
{
    @Inject(method = "write", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    public void write(CompoundNBT nbt, CallbackInfoReturnable<CompoundNBT> cir)
    {
        nbt.putString("Serializer", "minecraft:default");
        nbt.putBoolean("ShouldRender", this.shouldRender());
    }

    @Inject(method = "readInternal", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
    private static void readInternal(Effect effect, CompoundNBT nbt, CallbackInfoReturnable<EffectInstance> cir)
    {
        if(nbt.contains("Serializer"))
            cir.setReturnValue(IEffectInstanceSerializer.getSerializer(new ResourceLocation(nbt.getString("Serializer"))).deserialize(effect, nbt));
    }
}
