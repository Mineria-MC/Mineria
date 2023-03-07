package io.github.mineria_mc.mineria.common.data.predicates;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nullable;

public class FluidBarrelCapacityPredicate {
    public static final FluidBarrelCapacityPredicate ANY = new FluidBarrelCapacityPredicate(-1, -1, false);

    private final int capacity;
    private final int buckets;
    private final boolean shouldBeFull;

    public FluidBarrelCapacityPredicate(int capacity, int buckets, boolean shouldBeFull) {
        this.capacity = capacity;
        this.buckets = buckets;
        this.shouldBeFull = shouldBeFull;
    }

    private boolean notValid() {
        return (capacity < 0 || buckets < 0) && !shouldBeFull;
    }

    public boolean matches(int capacity, int buckets) {
        if (this.notValid())
            return true;
        else if (capacity == buckets && this.shouldBeFull)
            return true;
        else if (this.buckets < 0)
            return this.capacity == capacity;
        else if (this.capacity < 0)
            return this.buckets == buckets;
        else
            return this.capacity == capacity && this.buckets == buckets;
    }

    public JsonElement serializeToJson() {
        if (notValid())
            return JsonNull.INSTANCE;
        else {
            JsonObject json = new JsonObject();
            json.addProperty("capacity", this.capacity);
            json.addProperty("buckets", this.buckets);
            json.addProperty("shouldBeFull", this.shouldBeFull);
            return json;
        }
    }

    public static FluidBarrelCapacityPredicate fromJson(@Nullable JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            JsonObject json = jsonElement.getAsJsonObject();
            boolean shouldBeFull = json.has("shouldBeFull") && GsonHelper.getAsBoolean(json, "shouldBeFull");
            int capacity = json.has("capacity") && !shouldBeFull ? GsonHelper.getAsInt(json, "capacity") : -1;
            int buckets = json.has("buckets") && !shouldBeFull ? GsonHelper.getAsInt(json, "buckets") : -1;
            return new FluidBarrelCapacityPredicate(capacity, buckets, shouldBeFull);
        }
        return ANY;
    }
}
