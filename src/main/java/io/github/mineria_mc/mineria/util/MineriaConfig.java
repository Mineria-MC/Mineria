package io.github.mineria_mc.mineria.util;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.toml.TomlFormat;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

// Still work in progress
// There isn't a lot to add in mineria config yet
public class MineriaConfig {
    private static final Map<Type, Pair<Config, ForgeConfigSpec>> CONFIG_TO_SPEC = generateConfigurations(Common::new, Client::new, Server::new);

    public static final Common COMMON = getConfig(Type.COMMON);

    public static final class Common implements Config {
        public final BooleanValue enableChaoticBiomeGeneration;

        private Common(Builder builder) {
            builder.comment("Common side config for Mineria.")
                    .push("common");

            enableChaoticBiomeGeneration = builder.comment("Set to true to enable chaotic biome generation.",
                    "Chaotic biome generation is an alternative to the TerraBlender mod library.",
                    "WARNING: Custom Mineria biomes are integrated to the overworld in ways that might mess up compatibility as well as performance (that's why its called 'chaotic').",
                    "Use this option at your own risk!"
            ).define("enableChaoticBiomeGeneration", false);

            builder.pop();
        }

        @Override
        public Type type() {
            return Type.COMMON;
        }
    }

    public static final Client CLIENT = getConfig(Type.CLIENT);

    public static final class Client implements Config {
        public final BooleanValue enableTERAnimations;
        public final BooleanValue useHallucinationsShader;
        public final BooleanValue renderFourElementsFP;
        public final ConfigValue<String> apothecariumFont;

        private Client(Builder builder) {
            builder.comment("Client side config for Mineria.")
                    .push("client");

            enableTERAnimations = builder.comment("Toggle off to disable Tile Entity Renderers animations such as the gear of the extractor rotation.")
                    .define("enableTERAnimations", true);

            useHallucinationsShader = builder.comment("If set to true, the hallucinations effect overlay will be disabled and replaced with a minecraft shader named 'wobble'.",
                            "EPILEPSY WARNING")
                    .define("useHallucinationsShader", false);

            renderFourElementsFP = builder.comment("Whether the Four Elements enchantment's orbs should be rendered when playing in First Person.",
                            "(This option exists because we found a rendering bug with Optifine shaders. If you also encounter this bug turn this off.)")
                    .define("renderFourElementsFP", true);

            // TODO: fix other fonts
            Set<String> availableFonts = Set.of("default", "lcallig", "lhandw", "pristina", "segoesc", "tempsitc", "comic");
            apothecariumFont = builder.comment(
                    "Font of the Apothecarium book. Note: fonts that are not the default one are not guaranteed to render properly in the book.",
                    "Available fonts: 'default' (vanilla), 'lcallig' (Lucida Calligraphy), 'lhandw' (Lucida Handwriting), ",
                    "'pristina', 'segoesc' (Segoe Script), 'tempsitc' (Tempus Sans ITC)."
            ).define("apothecariumFont", "lcallig", o -> o instanceof String str && availableFonts.contains(str));

            builder.pop();
        }

        @Override
        public Type type() {
            return Type.CLIENT;
        }
    }

    public static final Server SERVER = getConfig(Type.SERVER);

    public static final class Server implements Config {
        private Server(Builder builder) {
        }

        @Override
        public Type type() {
            return Type.SERVER;
        }
    }

    public static void registerConfigSpecs(ModLoadingContext ctx) {
        ctx.registerConfig(Type.CLIENT, getSpec(Type.CLIENT), CLIENT.getConfigFileName());
//        ctx.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
        ctx.registerConfig(Type.COMMON, getSpec(Type.COMMON), COMMON.getConfigFileName());
    }

    private static FileConfig fileConfig;

    public static <T> T getValueFromFile(Type type, ForgeConfigSpec.ConfigValue<T> value, T fallback) {
        if (fileConfig == null) {
            fileConfig = FileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(getConfig(type).getConfigFileName()), TomlFormat.instance()).onFileNotFound(FileNotFoundAction.READ_NOTHING).build();
            fileConfig.load();
        }

        return fileConfig.getOrElse(value.getPath(), fallback);
    }

    private static Map<Type, Pair<Config, ForgeConfigSpec>> generateConfigurations(ConfigFactory... factories) {
        Map<Type, Pair<Config, ForgeConfigSpec>> result = new HashMap<>();

        for (ConfigFactory factory : factories) {
            Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(factory);
            result.put(specPair.getLeft().type(), specPair);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Config> T getConfig(Type type) {
        return (T) CONFIG_TO_SPEC.get(type).getLeft();
    }

    @Nullable
    private static ForgeConfigSpec getSpec(Type type) {
        return CONFIG_TO_SPEC.containsKey(type) ? CONFIG_TO_SPEC.get(type).getRight() : null;
    }

    private sealed interface Config permits Common, Client, Server {
        Type type();

        default String getConfigFileName() {
            return String.format("%s-%s.toml", Mineria.MODID, type().extension());
        }
    }

    @FunctionalInterface
    private interface ConfigFactory extends Function<Builder, Config> {
    }
}
