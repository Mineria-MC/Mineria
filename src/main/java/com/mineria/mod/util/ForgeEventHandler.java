package com.mineria.mod.util;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.ritual_table.RitualTableTileEntity;
import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.capabilities.IPoisonExposure;
import com.mineria.mod.common.capabilities.provider.AttachedEntityProvider;
import com.mineria.mod.common.capabilities.provider.PoisonExposureProvider;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.entity.WizardEntity;
import com.mineria.mod.common.init.*;
import com.mineria.mod.common.world.gen.WorldGenerationHandler;
import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SCooldownPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
        event.getEntityLiving().getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(IPoisonExposure::increaseTicksSinceExposure);
    }

    // Cool way to disable client input

    /*@SubscribeEvent
    public static void modifyMovementInputs(InputUpdateEvent event)
    {
        LivingEntity living = event.getEntityLiving();

        boolean lockInputs = false;

        if(!PoisonEffect.isImmune(living))
        {
            if(living.hasEffect(Effects.POISON) && living.getEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                PoisonEffectInstance poison = (PoisonEffectInstance) living.getEffect(Effects.POISON);
                if(poison.doConvulsions())
                {
                    lockInputs = true;
                }
            }
        }

        if(lockInputs)
        {
            event.getMovementInput().forwardImpulse = 0;
            event.getMovementInput().leftImpulse = 0;
        }
    }*/

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
            event.addCapability(new ResourceLocation(Mineria.MODID, "attached_entity"), new AttachedEntityProvider(CapabilityRegistry.ATTACHED_ENTITY.getDefaultInstance()));
            event.addCapability(new ResourceLocation(Mineria.MODID, "poison_exposure"), new PoisonExposureProvider(CapabilityRegistry.POISON_EXPOSURE.getDefaultInstance()));
        }
    }

    private static Method GETCODEC_METHOD;

    @SubscribeEvent
    public static void addDimensionalSpacing(WorldEvent.Load event)
    {
        if(event.getWorld() instanceof ServerWorld)
        {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            try
            {
                if(GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                @SuppressWarnings("unchecked")
                ResourceLocation rl = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if(rl != null && rl.getNamespace().equals("terraforged"))
                    return;
            } catch (Exception e)
            {
                Mineria.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD))
                return;

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(MineriaStructures.WIZARD_LABORATORY.get(), DimensionStructuresSettings.DEFAULTS.get(MineriaStructures.WIZARD_LABORATORY.get()));
            tempMap.putIfAbsent(MineriaStructures.WIZARD_TOWER.get(), DimensionStructuresSettings.DEFAULTS.get(MineriaStructures.WIZARD_TOWER.get()));
            tempMap.putIfAbsent(MineriaStructures.PAGODA.get(), DimensionStructuresSettings.DEFAULTS.get(MineriaStructures.PAGODA.get()));
            tempMap.putIfAbsent(MineriaStructures.RITUAL_STRUCTURE.get(), DimensionStructuresSettings.DEFAULTS.get(MineriaStructures.RITUAL_STRUCTURE.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    @SubscribeEvent
    public static void onPlayerAttackTarget(AttackEntityEvent event)
    {
        PlayerEntity player = event.getPlayer();
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
        PlayerEntity player = event.getPlayer();

        if(player instanceof ServerPlayerEntity)
        {
            CooldownTracker cooldownTracker = player.getCooldowns();
            try
            {
                Map<Item, ?> cooldowns = CooldownTrackerUtil.getCooldowns(cooldownTracker);

                for(Map.Entry<Item, ?> entry : cooldowns.entrySet())
                {
                    int startTime = (int) CooldownTrackerUtil.getStartTimeField().get(entry.getValue());
                    int endTime = (int) CooldownTrackerUtil.getEndTimeField().get(entry.getValue());
                    ((ServerPlayerEntity) player).connection.send(new SCooldownPacket(entry.getKey(), Math.max(0, endTime - startTime)));
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
        Collection<EffectInstance> effects = event.getEffects();

        if (!effects.isEmpty() && effects.stream().anyMatch(CustomEffectInstance.class::isInstance))
        {
            float r = 0.0F;
            float g = 0.0F;
            float b = 0.0F;
            int density = 0;

            for (EffectInstance effect : effects)
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
        if(event.getEntityLiving() instanceof WitchEntity)
        {
            WitchEntity witch = (WitchEntity) event.getEntityLiving();
            DamageSource source = event.getSource();

            if(witch.level instanceof ServerWorld)
            {
                ServerWorld world = (ServerWorld) witch.level;
                LivingEntity living = witch.getTarget();
                if (living == null && source.getEntity() instanceof LivingEntity)
                    living = (LivingEntity) source.getEntity();

                int x = MathHelper.floor(witch.getX());
                int y = MathHelper.floor(witch.getY());
                int z = MathHelper.floor(witch.getZ());
                Random random = world.getRandom();

                if(living != null && random.nextFloat() < 0.05F && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING))
                {
                    WizardEntity wizard = MineriaEntities.WIZARD.get().create(world);

                    for(int count = 0; count < 50; ++count)
                    {
                        int x1 = x + MathHelper.nextInt(random, 1, 7) * MathHelper.nextInt(random, -1, 1);
                        int y1 = y + MathHelper.nextInt(random, 1, 7) * MathHelper.nextInt(random, -1, 1);
                        int z1 = z + MathHelper.nextInt(random, 1, 7) * MathHelper.nextInt(random, -1, 1);
                        BlockPos pos = new BlockPos(x1, y1, z1);
                        EntitySpawnPlacementRegistry.PlacementType placementType = EntitySpawnPlacementRegistry.getPlacementType(MineriaEntities.WIZARD.get());
                        if (WorldEntitySpawner.isSpawnPositionOk(placementType, world, pos, MineriaEntities.WIZARD.get())/* && EntitySpawnPlacementRegistry.checkSpawnRules(MineriaEntities.WIZARD.get(), world, SpawnReason.REINFORCEMENT, pos, random)*/)
                        {
                            wizard.setPos(x1, y1, z1);
                            if (!world.hasNearbyAlivePlayer(x1, y1, z1, 7.0D) && world.isUnobstructed(wizard) && world.noCollision(wizard) && !world.containsAnyLiquid(wizard.getBoundingBox()))
                            {
                                if(!(living instanceof PlayerEntity && ((PlayerEntity) living).abilities.instabuild))
                                    wizard.setTarget(living);
                                wizard.finalizeSpawn(world, world.getCurrentDifficultyAt(wizard.blockPosition()), SpawnReason.REINFORCEMENT, null, null);
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
        EffectInstance effect = event.getPotionEffect();

        if(!(effect instanceof CustomEffectInstance) && effect.getEffect().equals(Effects.POISON))
        {
            event.setResult(Event.Result.DENY);
            PoisonEffectInstance.applyPoisonEffect(event.getEntityLiving(), Math.min(effect.getAmplifier(), 2), effect.getDuration(), 0, PoisonSource.UNKNOWN);
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        IInventory craftingMatrix = event.getInventory();
        PlayerEntity player = event.getPlayer();

        if(craftingMatrix instanceof CraftingInventory && player instanceof ServerPlayerEntity)
        {
            MineriaCriteriaTriggers.SHAPED_RECIPE_USED.trigger((ServerPlayerEntity) player, (CraftingInventory) craftingMatrix);
        }
    }

    @SubscribeEvent
    public static void onItemForged(AnvilRepairEvent event)
    {
        if(event.getPlayer() instanceof ServerPlayerEntity)
        {
            MineriaCriteriaTriggers.USED_ANVIL.trigger((ServerPlayerEntity) event.getPlayer(), event.getItemInput(), event.getIngredientInput(), event.getItemResult());
        }
    }

    @SubscribeEvent
    public static void onItemBrewed(PlayerBrewedPotionEvent event)
    {
        if(event.getPlayer() instanceof ServerPlayerEntity)
        {
            MineriaCriteriaTriggers.BREWED_ITEM.trigger((ServerPlayerEntity) event.getPlayer(), event.getStack());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        if(!event.isCanceled())
        {
            IWorld world = event.getWorld();
            BlockPos pos = event.getPos();
            PlayerEntity player = event.getPlayer();

            if(!player.abilities.instabuild && !event.getState().is(MineriaBlocks.Tags.ALLOWED_BLOCKS_RITUAL_TABLE))
            {
                for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -3, -4), pos.offset(4, 3, 4)))
                {
                    TileEntity te = world.getBlockEntity(blockPos);
                    if(te instanceof RitualTableTileEntity && ((RitualTableTileEntity) te).isAreaProtected())
                    {
                        player.displayClientMessage(new TranslationTextComponent("msg.mineria.ritual_table.protected_area"), true);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();

        if(!player.abilities.instabuild && !stack.getItem().is(MineriaItems.Tags.ALLOWED_BLOCKS_RITUAL_TABLE))
        {
            for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -3, -4), pos.offset(4, 3, 4)))
            {
                TileEntity te = world.getBlockEntity(blockPos);
                if(te instanceof RitualTableTileEntity && ((RitualTableTileEntity) te).isAreaProtected() && !(event.getHitVec().getBlockPos().equals(blockPos) && ((RitualTableTileEntity) te).getPlacedItem().isEmpty() || stack.isEmpty()))
                {
                    player.displayClientMessage(new TranslationTextComponent("msg.mineria.ritual_table.protected_area"), true);
                    event.setCanceled(true);
                }
            }
        }
    }
}
