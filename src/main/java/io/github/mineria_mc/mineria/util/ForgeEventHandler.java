package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.ritual_table.RitualTableBlockEntity;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.capabilities.ticking_data.ITickingDataCapability;
import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.entity.WizardEntity;
import io.github.mineria_mc.mineria.common.init.*;
import io.github.mineria_mc.mineria.common.items.DrinkItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Map;

/**
 * Hooks for every Forge event on common-side.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler {
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        event.getEntity().getCapability(MineriaCapabilities.TICKING_DATA).ifPresent(ITickingDataCapability::tick);
    }

    @SubscribeEvent
    public static void onPlayerAttackTarget(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (player.hasEffect(MineriaEffects.VAMPIRE.get())) {
            if (target.isAttackable() && !target.skipAttackInteraction(player) && target instanceof LivingEntity) {
                int amplifier = player.getEffect(MineriaEffects.VAMPIRE.get()).getAmplifier() + 1;
                float attackDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2;
                int amountToHeal = (int) Math.min((attackDamage / 10) * amplifier, attackDamage);
                player.heal(amountToHeal);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player instanceof ServerPlayer) {
            ItemCooldowns cooldownTracker = player.getCooldowns();
            try {
                Map<Item, ?> cooldowns = ItemCooldownsUtil.getCooldowns(cooldownTracker);

                for (Map.Entry<Item, ?> entry : cooldowns.entrySet()) {
                    int startTime = (int) ItemCooldownsUtil.getStartTimeField().get(entry.getValue());
                    int endTime = (int) ItemCooldownsUtil.getEndTimeField().get(entry.getValue());
                    ((ServerPlayer) player).connection.send(new ClientboundCooldownPacket(entry.getKey(), Math.max(0, endTime - startTime)));
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                Mineria.LOGGER.error("Caught an error when trying to send cooldown to client !", e);
            }
        }
    }

    @SubscribeEvent
    public static void potionColorCalculation(PotionColorCalculationEvent event) {
        Collection<MobEffectInstance> effects = event.getEffects();

        if (!effects.isEmpty() && effects.stream().anyMatch(ModdedMobEffectInstance.class::isInstance)) {
            float r = 0.0F;
            float g = 0.0F;
            float b = 0.0F;
            int density = 0;

            for (MobEffectInstance effect : effects) {
                if (effect.isVisible()) {
                    int color = effect instanceof ModdedMobEffectInstance ? ((ModdedMobEffectInstance) effect).getColor() : effect.getEffect().getColor();
                    int force = effect.getAmplifier() + 1;
                    r += (float) (force * (color >> 16 & 255)) / 255.0F;
                    g += (float) (force * (color >> 8 & 255)) / 255.0F;
                    b += (float) (force * (color & 255)) / 255.0F;
                    density += force;
                }
            }

            if (density == 0) {
                event.setColor(0);
            } else {
                r = r / (float) density * 255.0F;
                g = g / (float) density * 255.0F;
                b = b / (float) density * 255.0F;
                event.setColor((int) r << 16 | (int) g << 8 | (int) b);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Witch witch) {
            DamageSource source = event.getSource();

            if (witch.level() instanceof ServerLevel world) {
                LivingEntity target = witch.getTarget();
                if (target == null && source.getEntity() instanceof LivingEntity)
                    target = (LivingEntity) source.getEntity();

                int x = Mth.floor(witch.getX());
                int y = Mth.floor(witch.getY());
                int z = Mth.floor(witch.getZ());
                RandomSource random = world.getRandom();

                if (target != null && random.nextFloat() < 0.05F && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    WizardEntity wizard = MineriaEntities.WIZARD.get().create(world);

                    for (int count = 0; count < 50; ++count) {
                        int x1 = x + Mth.nextInt(random, 1, 7) * Mth.nextInt(random, -1, 1);
                        int y1 = y + Mth.nextInt(random, 1, 7) * Mth.nextInt(random, -1, 1);
                        int z1 = z + Mth.nextInt(random, 1, 7) * Mth.nextInt(random, -1, 1);
                        BlockPos pos = new BlockPos(x1, y1, z1);
                        SpawnPlacements.Type placementType = SpawnPlacements.getPlacementType(MineriaEntities.WIZARD.get());
                        if (NaturalSpawner.isSpawnPositionOk(placementType, world, pos, MineriaEntities.WIZARD.get())/* && EntitySpawnPlacementRegistry.checkSpawnRules(MineriaEntities.WIZARD.get(), world, SpawnReason.REINFORCEMENT, pos, random)*/) {
                            wizard.setPos(x1, y1, z1);
                            if (!world.hasNearbyAlivePlayer(x1, y1, z1, 7.0D) && world.isUnobstructed(wizard) && world.noCollision(wizard) && !world.containsAnyLiquid(wizard.getBoundingBox())) {
                                if (!(target instanceof Player && ((Player) target).getAbilities().instabuild))
                                    wizard.setTarget(target);
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
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        DamageSource source = event.getSource();

        if(!living.isInvulnerableTo(source)) {
            if(source.is(DamageTypeTags.IS_EXPLOSION) && living.hasEffect(MineriaEffects.EXPLOSION_RESISTANCE.get())) {
                event.setCanceled(true);
            }
            if(source.is(DamageTypeTags.IS_FALL) && living.hasEffect(MineriaEffects.FALL_DAMAGE_RESISTANCE.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onToolUsed(BlockEvent.BlockToolModificationEvent event) {
        if(ToolActions.HOE_TILL.equals(event.getToolAction()) && event.getState().is(Blocks.MUD)) {
            event.setFinalState(MineriaBlocks.MUDDY_FARMLAND.get().defaultBlockState());
        }
    }

    @SubscribeEvent
    public static void onEffectCheck(MobEffectEvent.Applicable event) {
        MobEffectInstance effect = event.getEffectInstance();

        if (!(effect instanceof ModdedMobEffectInstance) && effect.getEffect().equals(MobEffects.POISON)) {
            event.setResult(Event.Result.DENY);
            PoisonMobEffectInstance.applyPoisonEffect(event.getEntity(), Math.min(effect.getAmplifier(), 2), effect.getDuration(), 0, PoisonSource.UNKNOWN);
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Container craftingMatrix = event.getInventory();
        Player player = event.getEntity();

        if (craftingMatrix instanceof CraftingContainer && player instanceof ServerPlayer) {
            MineriaCriteriaTriggers.SHAPED_RECIPE_USED.trigger((ServerPlayer) player, (CraftingContainer) craftingMatrix);
        }
    }

    @SubscribeEvent
    public static void onItemForged(AnvilRepairEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            MineriaCriteriaTriggers.USED_ANVIL.trigger((ServerPlayer) event.getEntity(), event.getLeft(), event.getRight(), event.getOutput());
        }
    }

    @SubscribeEvent
    public static void onItemBrewed(PlayerBrewedPotionEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            MineriaCriteriaTriggers.BREWED_ITEM.trigger((ServerPlayer) event.getEntity(), event.getStack());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.isCanceled()) {
            LevelAccessor world = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getPlayer();

            if (!player.getAbilities().instabuild && !event.getState().is(MineriaBlocks.Tags.ALLOWED_BLOCKS_RITUAL_TABLE)) {
                for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -3, -4), pos.offset(4, 3, 4))) {
                    BlockEntity te = world.getBlockEntity(blockPos);
                    if (te instanceof RitualTableBlockEntity && ((RitualTableBlockEntity) te).isAreaProtected()) {
                        player.displayClientMessage(Component.translatable("msg.mineria.ritual_table.protected_area"), true);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (!player.getAbilities().instabuild && !stack.is(MineriaItems.Tags.ALLOWED_BLOCKS_RITUAL_TABLE)) {
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -3, -4), pos.offset(4, 3, 4))) {
                BlockEntity te = world.getBlockEntity(blockPos);
                if (te instanceof RitualTableBlockEntity && ((RitualTableBlockEntity) te).isAreaProtected() && !(event.getHitVec().getBlockPos().equals(blockPos) && ((RitualTableBlockEntity) te).getPlacedItem().isEmpty() || stack.isEmpty())) {
                    player.displayClientMessage(Component.translatable("msg.mineria.ritual_table.protected_area"), true);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if(event.getItem().is(Items.MILK_BUCKET)) {
            DrinkItem.unlockLaxativeDrinks(event.getEntity());
        }
    }
}
