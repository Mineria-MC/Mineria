package com.mineria.mod.mixin;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.XpBlockContainer;
import com.mineria.mod.util.CooldownTrackerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
    @Shadow @Final private CooldownTracker cooldowns;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundNBT nbt, CallbackInfo ci)
    {
        nbt.put("CooldownTracker", serializeCooldownTracker(this.cooldowns));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundNBT nbt, CallbackInfo ci)
    {
        if(nbt.contains("CooldownTracker"))
            deserializeCooldownTracker(nbt.getCompound("CooldownTracker"), this.cooldowns);
    }

    private static CompoundNBT serializeCooldownTracker(CooldownTracker cooldownTracker)
    {
        CompoundNBT nbt = new CompoundNBT();
        getCooldowns(cooldownTracker).forEach(nbt::putInt);
        return nbt;
    }

    private static void deserializeCooldownTracker(CompoundNBT nbt, CooldownTracker cooldownTracker)
    {
        for(String key : nbt.getAllKeys())
        {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if(item != null)
            {
                putCooldown(cooldownTracker, item, nbt.getInt(key));
            }
        }
    }

    private static Map<String, Integer> getCooldowns(CooldownTracker tracker)
    {
        Map<String, Integer> result = new HashMap<>();

        try
        {
            Map<Item, ?> map = CooldownTrackerUtil.getCooldowns(tracker);

            for(Map.Entry<Item, ?> entry : map.entrySet())
            {
                int startTime = (int) CooldownTrackerUtil.getStartTimeField().get(entry.getValue());
                int endTime = (int) CooldownTrackerUtil.getEndTimeField().get(entry.getValue());
                result.put(entry.getKey().getRegistryName().toString(), endTime - startTime);
            }
        } catch (Exception e)
        {
            Mineria.LOGGER.error("Failed to serialize Cooldown Tracker !", e);
        }

        return result;
    }

    private static void putCooldown(CooldownTracker tracker, Item item, int time)
    {
        try
        {
            Class<?> cooldownClass = CooldownTracker.class.getDeclaredClasses()[0];
            Constructor<?> constructor = cooldownClass.getDeclaredConstructor(CooldownTracker.class, int.class, int.class);
            constructor.setAccessible(true);

            Map<Item, Object> map = CooldownTrackerUtil.getCooldowns(tracker);
            int tickCount = CooldownTrackerUtil.getTickCount(tracker);
            map.put(item, constructor.newInstance(tracker, tickCount, tickCount + time));
        } catch (Exception e)
        {
            Mineria.LOGGER.error("Failed to deserialize cooldown !", e);
        }
    }
}
