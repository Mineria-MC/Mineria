package io.github.mineria_mc.mineria.data.tags;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static io.github.mineria_mc.mineria.common.init.MineriaEntities.*;
import static io.github.mineria_mc.mineria.common.init.MineriaEntities.Tags.DRUIDS;
import static net.minecraft.tags.EntityTypeTags.IMPACT_PROJECTILES;

public class MineriaEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public MineriaEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Mineria.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        bind(IMPACT_PROJECTILES, DART);
        bind(DRUIDS, DRUID, OVATE, BARD);
        bind(EntityTypeTags.FALL_DAMAGE_IMMUNE, FIRE_GOLEM, DIRT_GOLEM, AIR_SPIRIT, WATER_SPIRIT);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Mineria EntityType Tags";
    }

    private IntrinsicTagAppender<EntityType<?>> bind(TagKey<EntityType<?>> tag, RegistryObject<?>... objects) {
        IntrinsicTagAppender<EntityType<?>> appender = tag(tag);
        Arrays.stream(objects).map(RegistryObject::getId).map(TagEntry::element).forEach(appender::add);
        return appender;
    }
}
