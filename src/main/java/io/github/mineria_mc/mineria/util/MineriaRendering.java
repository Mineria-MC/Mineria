package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.mixin_extensions.PlayerItemInHandLayerExtension;
import io.github.mineria_mc.mineria.client.models.BlowgunModel;
import io.github.mineria_mc.mineria.client.models.ExtractorGearModel;
import io.github.mineria_mc.mineria.client.models.entity.AirSpiritModel;
import io.github.mineria_mc.mineria.client.models.entity.DirtGolemModel;
import io.github.mineria_mc.mineria.client.models.entity.WaterSpiritModel;
import io.github.mineria_mc.mineria.client.overlay.FastFreezingOverlay;
import io.github.mineria_mc.mineria.client.overlay.HallucinationsOverlay;
import io.github.mineria_mc.mineria.client.overlay.PoisonOverlay;
import io.github.mineria_mc.mineria.client.renderers.ExtractorTileEntityRenderer;
import io.github.mineria_mc.mineria.client.renderers.RitualTableTileEntityRenderer;
import io.github.mineria_mc.mineria.client.renderers.entity.*;
import io.github.mineria_mc.mineria.client.screens.*;
import io.github.mineria_mc.mineria.common.effects.potions.MineriaPotion;
import io.github.mineria_mc.mineria.common.init.*;
import io.github.mineria_mc.mineria.common.items.JarItem;
import io.github.mineria_mc.mineria.common.items.MineriaBow;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * A client class where everything related to rendering is registered.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class MineriaRendering {
    public static void registerScreenFactories() {
        MenuScreens.register(MineriaMenuTypes.TITANE_EXTRACTOR.get(), TitaneExtractorScreen::new);
        MenuScreens.register(MineriaMenuTypes.INFUSER.get(), InfuserScreen::new);
        MenuScreens.register(MineriaMenuTypes.XP_BLOCK.get(), XpBlockScreen::new);
        MenuScreens.register(MineriaMenuTypes.COPPER_WATER_BARREL.get(), CopperWaterBarrelScreen::new);
        MenuScreens.register(MineriaMenuTypes.GOLDEN_WATER_BARREL.get(), GoldenWaterBarrelScreen::new);
        MenuScreens.register(MineriaMenuTypes.DIAMOND_FLUID_BARREL.get(), DiamondFluidBarrelScreen::new);
        MenuScreens.register(MineriaMenuTypes.EXTRACTOR.get(), ExtractorScreen::new);
        MenuScreens.register(MineriaMenuTypes.DISTILLER.get(), DistillerScreen::new);
        MenuScreens.register(MineriaMenuTypes.APOTHECARY_TABLE.get(), ApothecaryTableScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        registerEntityRenders(event);
        registerTileEntityRenderers(event);
    }

    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MineriaEntities.GOLDEN_SILVERFISH.get(), GoldenSilverfishRenderer::new);
        event.registerEntityRenderer(MineriaEntities.WIZARD.get(), WizardRenderer::new);
        event.registerEntityRenderer(MineriaEntities.KUNAI.get(), KunaiRenderer::new);
        event.registerEntityRenderer(MineriaEntities.MINERIA_POTION.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MineriaEntities.MINERIA_AREA_EFFECT_CLOUD.get(), MineriaAreaEffectCloudRenderer::new);
        event.registerEntityRenderer(MineriaEntities.DRUID.get(), DruidRenderer::new);
        event.registerEntityRenderer(MineriaEntities.OVATE.get(), DruidRenderer::new);
        event.registerEntityRenderer(MineriaEntities.BARD.get(), DruidRenderer::new);
        event.registerEntityRenderer(MineriaEntities.FIRE_GOLEM.get(), FireGolemRenderer::new);
        event.registerEntityRenderer(MineriaEntities.DIRT_GOLEM.get(), DirtGolemRenderer::new);
        event.registerEntityRenderer(MineriaEntities.AIR_SPIRIT.get(), AirSpiritRenderer::new);
        event.registerEntityRenderer(MineriaEntities.WATER_SPIRIT.get(), WaterSpiritRenderer::new);
        event.registerEntityRenderer(MineriaEntities.DART.get(), BlowgunRefillRenderer::new);
        event.registerEntityRenderer(MineriaEntities.JAR.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MineriaEntities.DRUIDIC_WOLF.get(), DruidicWolfRenderer::new);
        event.registerEntityRenderer(MineriaEntities.BROWN_BEAR.get(), BrownBearRenderer::new);
        event.registerEntityRenderer(MineriaEntities.GREAT_DRUID_OF_GAULS.get(), GreatDruidOfGaulsRenderer::new);
        event.registerEntityRenderer(MineriaEntities.MINERIA_LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(MineriaEntities.BUDDHIST.get(), BuddhistRenderer::new);
        event.registerEntityRenderer(MineriaEntities.ASIATIC_HERBALIST.get(), AsiaticHerbalistRenderer::new);
        event.registerEntityRenderer(MineriaEntities.TEMPORARY_ITEM_FRAME.get(), ItemFrameRenderer::new);
    }

    private static void registerTileEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MineriaTileEntities.EXTRACTOR.get(), ExtractorTileEntityRenderer::new);
        event.registerBlockEntityRenderer(MineriaTileEntities.RITUAL_TABLE.get(), RitualTableTileEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AsiaticHerbalistRenderer.LAYER, () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));
        event.registerLayerDefinition(WizardRenderer.LAYER, WitchModel::createBodyLayer);
        event.registerLayerDefinition(AirSpiritRenderer.LAYER, AirSpiritModel::createBody);
        event.registerLayerDefinition(BrownBearRenderer.LAYER, PolarBearModel::createBodyLayer);
        event.registerLayerDefinition(WaterSpiritRenderer.LAYER, WaterSpiritModel::createBodyLayer);
        event.registerLayerDefinition(DirtGolemRenderer.LAYER, DirtGolemModel::createBodyLayer);
        event.registerLayerDefinition(FireGolemRenderer.LAYER, IronGolemModel::createBodyLayer);
        event.registerLayerDefinition(DruidicWolfRenderer.LAYER, WolfModel::createBodyLayer);
        event.registerLayerDefinition(DruidRenderer.LAYER, IllagerModel::createBodyLayer);
        event.registerLayerDefinition(GreatDruidOfGaulsRenderer.LAYER, IllagerModel::createBodyLayer);
        event.registerLayerDefinition(BuddhistRenderer.LAYER, () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));

        event.registerLayerDefinition(ExtractorTileEntityRenderer.LAYER, ExtractorGearModel::createLayerDefinition);
        event.registerLayerDefinition(BlowgunModel.LAYER, BlowgunModel::createLayerDefinition);
    }

    @SubscribeEvent
    public static void registerInGameOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), "poison_effect_overlay", new PoisonOverlay());
        event.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), "fast_freezing_effect_overlay", new FastFreezingOverlay());
        event.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), "hallucinations_effect_overlay", new HallucinationsOverlay());
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            LivingEntityRenderer<?, ?> renderer = event.getSkin(skin);
            RenderLayer<?, ?> itemInHandLayer = getRenderLayer(renderer, 1);
            if(itemInHandLayer instanceof PlayerItemInHandLayerExtension extension) {
                extension.mineria$createBlowgunModel(event.getEntityModels());
            } else {
                Mineria.LOGGER.debug("Failed to create blowgun model; PlayerItemInHandLayerExtension didn't apply on {}", itemInHandLayer);
            }
        }
    }

    private static Field LAYERS;

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends LivingEntity, M extends EntityModel<T>> RenderLayer<T, M> getRenderLayer(LivingEntityRenderer<T, M> renderer, int index) {
        if(LAYERS == null) {
            LAYERS = ObfuscationReflectionHelper.findField(LivingEntityRenderer.class, "f_115291_");
        }
        try {
            List<RenderLayer<T, M>> lst = (List<RenderLayer<T, M>>) LAYERS.get(renderer);
            return lst.get(index);
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static void registerItemModelsProperties() {
        DeferredRegisterUtil.filter(MineriaItems.ITEMS, MineriaBow.class).forEach(item -> {
            ItemProperties.register(item, new ResourceLocation("pull"), (stack, world, living, entityId) ->
                    living == null || living.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20.0F);
            ItemProperties.register(item, new ResourceLocation("pulling"),
                    (stack, world, living, entityId) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
        });

        ItemProperties.register(MineriaBlocks.getItemFromBlock(MineriaBlocks.GOLDEN_WATER_BARREL.get()), new ResourceLocation(Mineria.MODID, "potions"), (stack, world, living, entityId) -> {
            CompoundTag blockEntityTag = BlockItem.getBlockEntityData(stack);
            if (blockEntityTag != null && blockEntityTag.contains("Potions")) {
                return blockEntityTag.getInt("Potions");
            }
            return 0;
        });
        ItemProperties.register(MineriaBlocks.getItemFromBlock(MineriaBlocks.DIAMOND_FLUID_BARREL.get()), new ResourceLocation(Mineria.MODID, "has_netherite_look"), (stack, level, entity, seed) -> {
            CompoundTag data = BlockItem.getBlockEntityData(stack);
            return data != null && data.getBoolean("NetheriteLook") ? 1 : 0;
        });

        ItemProperties.register(MineriaItems.BLOWGUN_REFILL.get(), new ResourceLocation(Mineria.MODID, "has_poison"), (stack, world, living, entityId) -> JarItem.containsPoisonSource(stack) ? 1 : 0);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(Mineria.MODID, "mrlulu_sword", "inventory"));
        event.register(new ModelResourceLocation(Mineria.MODID, "mathys_craft_sword", "inventory"));
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        DeferredRegisterUtil.presentEntries(MineriaRecipeTypes.RECIPE_TYPES).forEach(type -> event.registerRecipeCategoryFinder(type, recipe -> RecipeBookCategories.UNKNOWN));
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : MineriaPotion.getColor(stack), MineriaItems.MINERIA_POTION.get(), MineriaItems.MINERIA_SPLASH_POTION.get(), MineriaItems.MINERIA_LINGERING_POTION.get());
        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), MineriaItems.JAR.get());
    }
}
