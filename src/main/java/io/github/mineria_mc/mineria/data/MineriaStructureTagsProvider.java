package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MineriaStructureTagsProvider extends StructureTagsProvider {
    public MineriaStructureTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
        super(packOutput, provider, Mineria.MODID, helper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        tag(MineriaStructures.Tags.ON_RITUAL_STRUCTURE_MAPS).add(MineriaStructures.RITUAL_STRUCTURE);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Mineria Structure Tags";
    }
}
