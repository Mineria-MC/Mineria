package com.mineria.mod.util;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.capabilities.IPoisonExposure;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.entity.WizardEntity;
import com.mineria.mod.common.init.*;
import com.mineria.mod.common.world.gen.WorldGenerationHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionColorCalculationEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Hooks for every Forge event on common-side.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler
{
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
//        event.getEntityLiving().getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(IPoisonExposure::increaseTicksSinceExposure);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void loadBiomes(BiomeLoadingEvent event)
    {
        WorldGenerationHandler.loadVanillaBiomes(event);
        MineriaBiomes.loadModdedBiomes(event);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof LivingEntity)
        {
//            event.addCapability(new ResourceLocation(Mineria.MODID, "attached_entity"), new AttachedEntityProvider(CapabilityRegistry.ATTACHED_ENTITY.getDefaultInstance()));
//            event.addCapability(new ResourceLocation(Mineria.MODID, "poison_exposure"), new PoisonExposureProvider(CapabilityRegistry.POISON_EXPOSURE.getDefaultInstance()));
        }
    }

    private static Method GETCODEC_METHOD;

//    @SubscribeEvent TODO
    public static void addDimensionalSpacing(WorldEvent.Load event)
    {
        if(event.getWorld() instanceof ServerLevel serverWorld)
        {
            try
            {
                if(GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "m_6909_");
                @SuppressWarnings("unchecked")
                ResourceLocation rl = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if(rl != null && rl.getNamespace().equals("terraforged"))
                    return;
            } catch (Exception e)
            {
                Mineria.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource && serverWorld.dimension().equals(Level.OVERWORLD))
                return;

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(MineriaStructures.WIZARD_LABORATORY.get(), StructureSettings.DEFAULTS.get(MineriaStructures.WIZARD_LABORATORY.get()));
            tempMap.putIfAbsent(MineriaStructures.WIZARD_TOWER.get(), StructureSettings.DEFAULTS.get(MineriaStructures.WIZARD_TOWER.get()));
            tempMap.putIfAbsent(MineriaStructures.PAGODA.get(), StructureSettings.DEFAULTS.get(MineriaStructures.PAGODA.get()));
            tempMap.putIfAbsent(MineriaStructures.RITUAL_STRUCTURE.get(), StructureSettings.DEFAULTS.get(MineriaStructures.RITUAL_STRUCTURE.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    @SubscribeEvent
    public static void onPlayerAttackTarget(AttackEntityEvent event)
    {
        Player player = event.getPlayer();
        Entity target = event.getTarget();

        if(player.hasEffect(MineriaEffects.VAMPIRE.get()))
        {
            if(target.isAttackable() && !target.skipAttackInteraction(player) && target instanceof LivingEntity)
            {
                int amplifier = player.getEffect(MineriaEffects.VAMPIRE.get()).getAmplifier() + 1;
                float attackDamage = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2;
                int amountToHeal = (int) Math.min((attackDamage / 10) * amplifier, attackDamage);
                player.heal(amountToHeal);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getPlayer();

        if(player instanceof ServerPlayer)
        {
            ItemCooldowns cooldownTracker = player.getCooldowns();
            try
            {
                Map<Item, ?> cooldowns = ItemCooldownsUtil.getCooldowns(cooldownTracker);

                for(Map.Entry<Item, ?> entry : cooldowns.entrySet())
                {
                    int startTime = (int) ItemCooldownsUtil.getStartTimeField().get(entry.getValue());
                    int endTime = (int) ItemCooldownsUtil.getEndTimeField().get(entry.getValue());
                    ((ServerPlayer) player).connection.send(new ClientboundCooldownPacket(entry.getKey(), Math.max(0, endTime - startTime)));
                }
            }
            catch (IllegalAccessException | NoSuchFieldException e)
            {
                Mineria.LOGGER.error("Caught an error when trying to send cooldown to client !", e);
            }
        }
    }

    @SubscribeEvent
    public static void potionColorCalculation(PotionColorCalculationEvent event)
    {
        Collection<MobEffectInstance> effects = event.getEffects();

        if (!effects.isEmpty() && effects.stream().anyMatch(CustomEffectInstance.class::isInstance))
        {
            float r = 0.0F;
            float g = 0.0F;
            float b = 0.0F;
            int density = 0;

            for (MobEffectInstance effect : effects)
            {
                if (effect.isVisible())
                {
                    int color = effect instanceof CustomEffectInstance ? ((CustomEffectInstance) effect).getColor() : effect.getEffect().getColor();
                    int force = effect.getAmplifier() + 1;
                    r += (float) (force * (color >> 16 & 255)) / 255.0F;
                    g += (float) (force * (color >> 8 & 255)) / 255.0F;
                    b += (float) (force * (color & 255)) / 255.0F;
                    density += force;
                }
            }

            if (density == 0)
            {
                event.setColor(0);
            } else
            {
                r = r / (float) density * 255.0F;
                g = g / (float) density * 255.0F;
                b = b / (float) density * 255.0F;
                event.setColor((int) r << 16 | (int) g << 8 | (int) b);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        if(event.getEntityLiving() instanceof Witch witch)
        {
            DamageSource source = event.getSource();

            if(witch.level instanceof ServerLevel world)
            {
                LivingEntity living = witch.getTarget();
                if (living == null && source.getEntity() instanceof LivingEntity)
                    living = (LivingEntity) source.getEntity();

                int x = Mth.floor(witch.getX());
                int y = Mth.floor(witch.getY());
                int z = Mth.floor(witch.getZ());
                Random random = world.getRandom();

                if(living != null && random.nextFloat() < 0.05F && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING))
                {
                    WizardEntity wizard = MineriaEntities.WIZARD.get().create(world);

                    for(int count = 0; count < 50; ++count)
                    {
                        int x1 = x + Mth.nextInt(random, 1, 7) * Mth.nextInt(random, -1, 1);
                        int y1 = y + Mth.nextInt(random, 1, 7) * Mth.nextInt(random, -1, 1);
                        int z1 = z + Mth.nextInt(random, 1, 7) * Mth.nextInt(random, -1, 1);
                        BlockPos pos = new BlockPos(x1, y1, z1);
                        SpawnPlacements.Type placementType = SpawnPlacements.getPlacementType(MineriaEntities.WIZARD.get());
                        if (NaturalSpawner.isSpawnPositionOk(placementType, world, pos, MineriaEntities.WIZARD.get())/* && EntitySpawnPlacementRegistry.checkSpawnRules(MineriaEntities.WIZARD.get(), world, SpawnReason.REINFORCEMENT, pos, random)*/)
                        {
                            wizard.setPos(x1, y1, z1);
                            if (!world.hasNearbyAlivePlayer(x1, y1, z1, 7.0D) && world.isUnobstructed(wizard) && world.noCollision(wizard) && !world.containsAnyLiquid(wizard.getBoundingBox()))
                            {
                                if(!(living instanceof Player && ((Player) living).getAbilities().instabuild))
                                    wizard.setTarget(living);
                                wizard.finalizeSpawn(world, world.getCurrentDifficultyAt(wizard.blockPosition()), MobSpawnType.REINFORCEMENT, null, null);
                                world.addFreshEntityWithPassengers(wizard);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEffectCheck(PotionEvent.PotionApplicableEvent event)
    {
        MobEffectInstance effect = event.getPotionEffect();

        if(!(effect instanceof CustomEffectInstance) && effect.getEffect().equals(MobEffects.POISON))
        {
            event.setResult(Event.Result.DENY);
            PoisonEffectInstance.applyPoisonEffect(event.getEntityLiving(), Math.min(effect.getAmplifier(), 2), effect.getDuration(), 0, PoisonSource.UNKNOWN);
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        Container craftingMatrix = event.getInventory();
        Player player = event.getPlayer();

        if(craftingMatrix instanceof CraftingContainer && player instanceof ServerPlayer)
        {
            MineriaCriteriaTriggers.SHAPED_RECIPE_USED.trigger((ServerPlayer) player, (CraftingContainer) craftingMatrix);
        }
    }

    @SubscribeEvent
    public static void onItemForged(AnvilRepairEvent event)
    {
        if(event.getPlayer() instanceof ServerPlayer)
        {
            MineriaCriteriaTriggers.USED_ANVIL.trigger((ServerPlayer) event.getPlayer(), event.getItemInput(), event.getIngredientInput(), event.getItemResult());
        }
    }

    @SubscribeEvent
    public static void onItemBrewed(PlayerBrewedPotionEvent event)
    {
        if(event.getPlayer() instanceof ServerPlayer)
        {
            MineriaCriteriaTriggers.BREWED_ITEM.trigger((ServerPlayer) event.getPlayer(), event.getStack());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        if(!event.isCanceled())
        {
            LevelAccessor world = event.getWorld();
            BlockPos pos = event.getPos();
            Player player = event.getPlayer();

            if(!player.getAbilities().instabuild && !event.getState().is(MineriaBlocks.Tags.ALLOWED_BLOCKS_RITUAL_TABLE))
            {
                for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -3, -4), pos.offset(4, 3, 4)))
                {
                    BlockEntity te = world.getBlockEntity(blockPos);
                    if(te instanceof RitualTableTileEntity && ((RitualTableTileEntity) te).isAreaProtected())
                    {
                        player.displayClientMessage(new TranslatableComponent("msg.mineria.ritual_table.protected_area"), true);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        Level world = event.getWorld();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        ItemStack stack = event.getItemStack();

        if(!player.getAbilities().instabuild && !stack.is(MineriaItems.Tags.ALLOWED_BLOCKS_RITUAL_TABLE))
        {
            for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -3, -4), pos.offset(4, 3, 4)))
            {
                BlockEntity te = world.getBlockEntity(blockPos);
                if(te instanceof RitualTableTileEntity && ((RitualTableTileEntity) te).isAreaProtected() && !(event.getHitVec().getBlockPos().equals(blockPos) && ((RitualTableTileEntity) te).getPlacedItem().isEmpty() || stack.isEmpty()))
                {
                    player.displayClientMessage(new TranslatableComponent("msg.mineria.ritual_table.protected_area"), true);
                    event.setCanceled(true);
                }
            }
        }
    }
}
