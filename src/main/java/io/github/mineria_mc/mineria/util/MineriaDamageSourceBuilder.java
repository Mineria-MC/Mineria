package io.github.mineria_mc.mineria.util;

import com.google.common.collect.Maps;
import io.github.mineria_mc.mineria.common.init.MineriaDamageTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for custom DamageSources.
 */
public class MineriaDamageSourceBuilder {
    private static final Set<ResourceKey<DamageType>> INDEPENDENT_TYPES = Set.of(MineriaDamageTypes.SPIKE);

    private final Level level;
    private final Registry<DamageType> registry;
    private final Map<ResourceKey<DamageType>, DamageSource> damageSources;

    private MineriaDamageSourceBuilder(Level level) {
        this.level = level;
        this.registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        this.damageSources = Maps.toMap(INDEPENDENT_TYPES, input -> new DamageSource(registry.getHolderOrThrow(input)));
    }

    public DamageSource ofType(ResourceKey<DamageType> type) {
        return damageSources.get(type);
    }

    public DamageSource kunai(Entity kunai, @Nullable Entity owner) {
        return source(MineriaDamageTypes.KUNAI, kunai, owner);
    }

    public DamageSource elementalOrb(Entity owner) {
        return source(MineriaDamageTypes.ELEMENTAL_ORB, null, owner);
    }

    public DamageSource bambooBlowgun(Entity refill, @Nullable Entity owner) {
        return source(MineriaDamageTypes.BAMBOO_BLOWGUN, refill, owner);
    }

    private DamageSource source(ResourceKey<DamageType> type, @Nullable Entity entity, @Nullable Entity owner) {
        return new DamageSource(this.registry.getHolderOrThrow(type), entity, owner) {
            @Nonnull
            @Override
            public Component getLocalizedDeathMessage(@Nonnull LivingEntity victim) {
                String msg = "death.attack." + this.type().msgId();
                Entity causingEntity = getDirectEntity(), directEntity = getDirectEntity();

                if (causingEntity == null && directEntity == null) {
                    LivingEntity killer = victim.getKillCredit();
                    if (killer != null) {
                        return Component.translatable(msg + ".player", victim.getDisplayName(), killer.getDisplayName());
                    }
                    return Component.translatable(msg, victim.getDisplayName());
                }

                Component killerName = causingEntity == null ? directEntity.getDisplayName() : causingEntity.getDisplayName();
                ItemStack handStack = causingEntity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;

                if (!handStack.isEmpty() && handStack.hasCustomHoverName()) {
                    return Component.translatable(msg + ".item", victim.getDisplayName(), killerName, handStack.getDisplayName());
                }

                return Component.translatable(msg + ".player", victim.getDisplayName(), killerName);
            }
        };
    }

    private static MineriaDamageSourceBuilder instance;

    public static MineriaDamageSourceBuilder get(@Nonnull Level level) {
        if(instance == null || instance.level != level) {
            instance = new MineriaDamageSourceBuilder(level);
        }
        return instance;
    }
}
