package com.mineria.mod.common.data.triggers;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class ShotBlowgunTrigger extends AbstractCriterionTrigger<ShotBlowgunTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "shot_blowgun");

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate andPredicate, ConditionArrayParser parser)
    {
        ItemPredicate blowgun = ItemPredicate.fromJson(json.get("blowgun"));
        ItemPredicate ammo = ItemPredicate.fromJson(json.get("ammo"));
        return new Instance(andPredicate, blowgun, ammo);
    }

    public void trigger(ServerPlayerEntity player, ItemStack blowgun, ItemStack ammo)
    {
        this.trigger(player, instance -> instance.matches(blowgun, ammo));
    }

    public static class Instance extends CriterionInstance
    {
        private final ItemPredicate blowgun;
        private final ItemPredicate ammo;

        public Instance(EntityPredicate.AndPredicate andPredicate, ItemPredicate blowgun, ItemPredicate ammo)
        {
            super(ID, andPredicate);
            this.blowgun = blowgun;
            this.ammo = ammo;
        }

        private boolean matches(ItemStack blowgun, ItemStack ammo)
        {
            return this.blowgun.matches(blowgun) && this.ammo.matches(ammo);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer serializer)
        {
            JsonObject json = super.serializeToJson(serializer);
            json.add("blowgun", this.blowgun.serializeToJson());
            json.add("ammo", this.ammo.serializeToJson());
            return json;
        }
    }
}
