package com.mineria.mod.util;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mineria.mod.Mineria;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Still work in progress
// There isn't a lot to add mineria config yet
public class MineriaConfig
{
    private static final Map<Type, Pair<IConfig, ForgeConfigSpec>> CONFIG_TO_SPEC = generateConfigurations(Common::new, Client::new, Server::new);

    public static final Common COMMON = getConfig(Type.COMMON);

    /*static
    {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }*/

    public static class Common implements IConfig
    {
        public final BooleanValue enableDynamicEasterEggs;

        private Common(Builder builder)
        {
            builder.comment("Common side config for Mineria.")
                    .push("common");

            enableDynamicEasterEggs = builder.comment("Set to true if you want Dynamic Easter Eggs enabled on Mineria.",
                            "Dynamic Easter Eggs are objects which are registered under conditions like the current date.",
                            "If this is enabled some objects of your world may not get correct ids (no worry forge will automatically patch them)."
                    )
                    .define("enableDynamicEasterEggs", false);

            builder.pop();
        }

        @Override
        public Type type()
        {
            return Type.COMMON;
        }
    }

    public static final Client CLIENT = getConfig(Type.CLIENT);

    /*static
    {
        Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }*/

    public static class Client implements IConfig
    {
        public final BooleanValue enableTERAnimations;
        public final BooleanValue useHallucinationsShader;

        private Client(Builder builder)
        {
            builder.comment("Client side config for Mineria.")
                    .push("client");

            enableTERAnimations = builder.comment("Toggle off to disable Tile Entity Renderers animations such as the gear of the extractor rotation.")
                    .define("enableTERAnimations", true);

            useHallucinationsShader = builder.comment("If set to true, the hallucinations effect overlay will be disabled and replaced with a minecraft shader named 'wobble'.",
                            "EPILEPSY WARNING")
                    .define("useHallucinationsShader", false);

            builder.pop();
        }

        @Override
        public Type type()
        {
            return Type.CLIENT;
        }
    }

    public static final Server SERVER = getConfig(Type.SERVER);

    /*static
    {
        Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }*/

    public static class Server implements IConfig
    {
        private Server(Builder builder)
        {

        }

        @Override
        public Type type()
        {
            return Type.SERVER;
        }
    }

    public static void registerConfigSpecs(ModLoadingContext ctx)
    {
        ctx.registerConfig(Type.CLIENT, getSpec(Type.CLIENT), CLIENT.getConfigFile());
//        ctx.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
//        ctx.registerConfig(Type.COMMON, getSpec(Type.COMMON), COMMON.getConfigFile());
    }

    private static FileConfig fileConfig;

    public static <T> T getValueFromFile(Type type, ForgeConfigSpec.ConfigValue<T> value)
    {
        if(fileConfig == null)
        {
            fileConfig = FileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(getConfig(type).getConfigFile()), TomlFormat.instance()).onFileNotFound(FileNotFoundAction.READ_NOTHING).build();
            fileConfig.load();
        }

        return fileConfig.getOrElse(value.getPath(), value.get());
    }

    private static Map<Type, Pair<IConfig, ForgeConfigSpec>> generateConfigurations(ConfigFactory... factories)
    {
        Map<Type, Pair<IConfig, ForgeConfigSpec>> result = new HashMap<>();

        for(ConfigFactory factory : factories)
        {
            Pair<IConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(factory);
            result.put(specPair.getLeft().type(), specPair);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T extends IConfig> T getConfig(Type type)
    {
        return (T) CONFIG_TO_SPEC.get(type).getLeft();
    }

    @Nullable
    private static ForgeConfigSpec getSpec(Type type)
    {
        return CONFIG_TO_SPEC.containsKey(type) ? CONFIG_TO_SPEC.get(type).getRight() : null;
    }

    private interface IConfig
    {
        Type type();

        default String getConfigFile()
        {
            return String.format("%s-%s.toml", Mineria.MODID, type().extension());
        }
    }

    @FunctionalInterface
    private interface ConfigFactory extends Function<Builder, IConfig> {}
}
