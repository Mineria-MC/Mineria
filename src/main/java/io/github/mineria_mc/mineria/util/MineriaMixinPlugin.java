package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MineriaMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!mixinClassName.equals("io.github.mineria_mc.mineria.mixin.OverworldBiomeBuilderMixin")) {
            return true;
        }
        LoadingModList modList = LoadingModList.get();
        if(modList == null || modList.getModFileById("terrablender") != null) {
            return false;
        }
        try {
            return MineriaConfig.getValueFromFile(ModConfig.Type.COMMON, MineriaConfig.COMMON.enableChaoticBiomeGeneration, false);
        } catch (Throwable ignored) {
        }
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        if(!FMLLoader.isProduction()) {
            return List.of("DefaultPlayerSkinMixin");
        }
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if(mixinClassName.equals("io.github.mineria_mc.mineria.mixin.OverworldBiomeBuilderMixin")) {
            Mineria.LOGGER.warn("Applying mixin mineria$pickMiddleBiome in OverworldBiomeBuilderMixin...");
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
