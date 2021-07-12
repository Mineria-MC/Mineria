package com.mineria.mod.mixin;

import com.mineria.mod.effects.CustomEffectInstance;
import com.mineria.mod.effects.PoisonEffectInstance;
import com.mineria.mod.effects.PoisonSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.extensions.IForgeEffectInstance;
import net.minecraftforge.common.util.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin implements IForgeEffectInstance
{
    @Inject(method = "readInternal", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
    private static void readInternal(Effect effect, CompoundNBT nbt, CallbackInfoReturnable<EffectInstance> cir)
    {
        int amplifier = nbt.getByte("Amplifier");
        int duration = nbt.getInt("Duration");
        boolean ambient = nbt.getBoolean("Ambient");

        boolean showParticles = true;
        if (nbt.contains("ShowParticles", 1))
        {
            showParticles = nbt.getBoolean("ShowParticles");
        }

        boolean showIcon = showParticles;
        if (nbt.contains("ShowIcon", 1))
        {
            showIcon = nbt.getBoolean("ShowIcon");
        }

        if (nbt.contains("Custom") && nbt.getBoolean("Custom"))
        {
            int maxDuration = nbt.getInt("MaxDuration");

            if (nbt.contains("PotionClass"))
            {
                int potionClass = nbt.getInt("PotionClass");
                PoisonSource source = PoisonSource.byId(nbt.getInt("PoisonSource"));
                cir.setReturnValue(new PoisonEffectInstance(potionClass, duration, maxDuration, amplifier, source));
            }
            else
                cir.setReturnValue(readCurativeItems(CustomEffectInstance.makeCustomEffectInstance(effect, duration, maxDuration, amplifier, ambient, showParticles, showIcon, nbt.getBoolean("ShouldRender")), nbt));
        }
    }

    private static EffectInstance readCurativeItems(EffectInstance effect, CompoundNBT nbt)
    {
        if (nbt.contains("CurativeItems", Constants.NBT.TAG_LIST))
        {
            List<ItemStack> items = new ArrayList<>();
            ListNBT list = nbt.getList("CurativeItems", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++)
            {
                items.add(ItemStack.read(list.getCompound(i)));
            }
            effect.setCurativeItems(items);
        }

        return effect;
    }
}
