package com.mineria.mod.mixin;

import com.mineria.mod.Mineria;
import com.mineria.mod.util.ItemCooldownsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
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

@Mixin(Player.class)
public class PlayerMixin
{
    @Shadow @Final private ItemCooldowns cooldowns;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci)
    {
        nbt.put("CooldownTracker", serializeCooldownTracker(this.cooldowns));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci)
    {
        if(nbt.contains("CooldownTracker"))
            deserializeCooldownTracker(nbt.getCompound("CooldownTracker"), this.cooldowns);
    }

    private static CompoundTag serializeCooldownTracker(ItemCooldowns cooldownTracker)
    {
        CompoundTag nbt = new CompoundTag();
        getCooldowns(cooldownTracker).forEach(nbt::putInt);
        return nbt;
    }

    private static void deserializeCooldownTracker(CompoundTag nbt, ItemCooldowns cooldownTracker)
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

    private static Map<String, Integer> getCooldowns(ItemCooldowns tracker)
    {
        Map<String, Integer> result = new HashMap<>();

        try
        {
            Map<Item, ?> map = ItemCooldownsUtil.getCooldowns(tracker);

            for(Map.Entry<Item, ?> entry : map.entrySet())
            {
                int startTime = (int) ItemCooldownsUtil.getStartTimeField().get(entry.getValue());
                int endTime = (int) ItemCooldownsUtil.getEndTimeField().get(entry.getValue());
                result.put(entry.getKey().getRegistryName().toString(), endTime - startTime);
            }
        } catch (Exception e)
        {
            Mineria.LOGGER.error("Failed to serialize Cooldown Tracker !", e);
        }

        return result;
    }

    private static void putCooldown(ItemCooldowns tracker, Item item, int time)
    {
        try
        {
            Class<?> cooldownClass = ItemCooldowns.class.getDeclaredClasses()[0];
            Constructor<?> constructor = cooldownClass.getDeclaredConstructor(ItemCooldowns.class, int.class, int.class);
            constructor.setAccessible(true);

            Map<Item, Object> map = ItemCooldownsUtil.getCooldowns(tracker);
            int tickCount = ItemCooldownsUtil.getTickCount(tracker);
            map.put(item, constructor.newInstance(tracker, tickCount, tickCount + time));
        } catch (Exception e)
        {
            Mineria.LOGGER.error("Failed to deserialize cooldown !", e);
        }
    }
}
