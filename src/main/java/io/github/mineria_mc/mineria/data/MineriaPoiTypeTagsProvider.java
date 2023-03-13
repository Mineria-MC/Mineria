package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaPOITypes;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MineriaPoiTypeTagsProvider extends PoiTypeTagsProvider {
    public MineriaPoiTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Mineria.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        addObjects(PoiTypeTags.ACQUIRABLE_JOB_SITE, MineriaPOITypes.APOTHECARY);
    }

    @SafeVarargs
    private TagAppender<PoiType> addObjects(TagKey<PoiType> tag, RegistryObject<PoiType>... types) {
        TagAppender<PoiType> appender = tag(tag);
        for(RegistryObject<PoiType> poiType : types) {
            appender.add(MineriaUtils.key(poiType));
        }
        return appender;
    }
}
