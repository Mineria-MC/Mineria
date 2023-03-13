package io.github.mineria_mc.mineria.common.data.triggers;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;

public class ShotBlowgunTrigger extends SimpleCriterionTrigger<ShotBlowgunTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "shot_blowgun");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite andPredicate, DeserializationContext parser) {
        ItemPredicate blowgun = ItemPredicate.fromJson(json.get("blowgun"));
        ItemPredicate ammo = ItemPredicate.fromJson(json.get("ammo"));
        return new Instance(andPredicate, blowgun, ammo);
    }

    public void trigger(ServerPlayer player, ItemStack blowgun, ItemStack ammo) {
        this.trigger(player, instance -> instance.matches(blowgun, ammo));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate blowgun;
        private final ItemPredicate ammo;

        public Instance(EntityPredicate.Composite andPredicate, ItemPredicate blowgun, ItemPredicate ammo) {
            super(ID, andPredicate);
            this.blowgun = blowgun;
            this.ammo = ammo;
        }

        public static Instance poisonousAmmo(PoisonSource source) {
            return new Instance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, ItemPredicate.Builder.item().of(MineriaItems.BLOWGUN_REFILL.get()).hasNbt(Util.make(new CompoundTag(), nbt -> nbt.putString("PoisonSource", source.getId().toString()))).build());
        }

        private boolean matches(ItemStack blowgun, ItemStack ammo) {
            return this.blowgun.matches(blowgun) && this.ammo.matches(ammo);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject json = super.serializeToJson(serializer);
            json.add("blowgun", this.blowgun.serializeToJson());
            json.add("ammo", this.ammo.serializeToJson());
            return json;
        }
    }
}
