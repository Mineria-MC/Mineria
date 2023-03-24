package io.github.mineria_mc.mineria.common.blocks.ritual_table;

import io.github.mineria_mc.mineria.common.entity.AbstractDruidEntity;
import io.github.mineria_mc.mineria.common.entity.GreatDruidOfGaulsEntity;
import io.github.mineria_mc.mineria.common.entity.MineriaLightningBoltEntity;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RitualTableBlockEntity extends BlockEntity {
    private final List<UUID> druidUUIDs = new ArrayList<>();
    private final List<AbstractDruidEntity> druids = new ArrayList<>();

    private RitualStage currentStage = RitualStage.IDLE;
    private ItemStack placedItem = ItemStack.EMPTY;
    private boolean areaProtection;
    private boolean requiresCleaningArea;

    private int nextStageDelay;

    public RitualTableBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaBlockEntities.RITUAL_TABLE.get(), pos, state);
    }

    // TODO: fix druids not positioned correctly
    public void serverTick(@Nonnull Level level, @Nonnull BlockPos pos) {
        if(requiresCleaningArea && level.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), 100)) {
            List<Block> forbiddenBlocks = List.of(Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.MUSHROOM_STEM);
            for (int x = -15; x < 15; x++) {
                for (int y = 0; y < 31; y++) {
                    for (int z = -15; z < 15; z++) {
                        BlockState state = level.getBlockState(pos.offset(x, y, z));
                        if(state.is(BlockTags.OVERWORLD_NATURAL_LOGS) || state.is(BlockTags.LEAVES) || forbiddenBlocks.contains(state.getBlock())) {
                            level.removeBlock(pos.offset(x, y, z), false);
                        }
                    }
                }
            }
            requiresCleaningArea = false;
        }
        if(!druidUUIDs.isEmpty()) {
            loadDruidEntities(level);
            return;
        }
        if (nextStageDelay > 0) {
            nextStageDelay--;
        }

        druids.removeIf(druid -> !druid.isAlive());

        RitualEventHandler eventHandler = new RitualEventHandler() {
            @Override
            public void setStage(RitualStage stage, int delay) {
                currentStage = stage;
                if(delay > 0) {
                    nextStageDelay = delay;
                }
                sendEvent(1, stage.ordinal());
                setChanged();
            }

            @Override
            public void removePlacedItem() {
                setPlacedItem(ItemStack.EMPTY);
                sendEvent(2, 0);
            }

            @Override
            public void sendEvent(int id, int param) {
                level.blockEvent(pos, getBlockState().getBlock(), id, param);
            }
        };
        boolean bossSummoning = canPerformBossSummoning(level, pos);
        boolean druidSummoning = canPerformDruidSummoning();

        if(currentStage == RitualStage.IDLE) {
            // Ritual Initiation
            if(bossSummoning) {
                eventHandler.setStage(RitualStage.STARTED);
                return;
            } else if (druidSummoning) {
                eventHandler.setStage(RitualStage.SPAWN_DRUID, 100);
                return;
            }
        } else if(!druidSummoning && !bossSummoning) {
            // Ritual interruption
            eventHandler.setStage(RitualStage.IDLE);
            if (areDruidsPositioned() || druids.size() != 5) {
                druids.forEach(druid -> {
                    druid.setRitualTablePosition(null);
                    druid.setRitualPosition(null);
                });
            }
            return;
        }

        if(nextStageDelay == 0) {
            handleCurrentStage(level, pos, eventHandler);
        }
    }

    private boolean canPerformBossSummoning(Level level, BlockPos pos) {
        return areDruidsPositioned() && druids.size() == 5 && RitualTableBlock.getOrCreateShape().find(level, pos.offset(-3, 0, -3)) != null;
    }

    private boolean canPerformDruidSummoning() {
        return placedItem.is(MineriaItems.VANADIUM_INGOT.get()) && placedItem.getCount() == 1;
    }

    private boolean areDruidsPositioned() {
        for (AbstractDruidEntity druid : druids) {
            if (!(druid.getNavigation().isDone() && druid.getRitualPosition().isPresent())) {
                return false;
            }
        }

        return true;
    }

    private void handleCurrentStage(Level level, BlockPos pos, RitualEventHandler eventHandler) {
        switch (currentStage) {
            case STARTED -> checkForItemAndConsume(MineriaItems.MISTLETOE.get(), 16, () -> eventHandler.setStage(RitualStage.MISTLETOE_CONSUMED), eventHandler);
            case MISTLETOE_CONSUMED -> checkForItemAndConsume(MineriaItems.VANADIUM_INGOT.get(), 1, () -> eventHandler.setStage(RitualStage.VANADIUM_CONSUMED), eventHandler);
            case VANADIUM_CONSUMED -> checkForItemAndConsume(Items.LAPIS_LAZULI, 64, () -> eventHandler.setStage(RitualStage.LAPIS_CONSUMED, 40), eventHandler);
            case LAPIS_CONSUMED -> {
                druids.forEach(druid -> druid.setItemInHand(InteractionHand.MAIN_HAND, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LUCK)));
                eventHandler.setStage(RitualStage.DRUIDS_DRINK, 60);
            }
            case DRUIDS_DRINK -> {
                druids.forEach(druid -> druid.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY));
                BehaviorUtils.throwItem(druids.get(0), PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_POTION.get()), MineriaPotions.YEW_POISONING.get()), findNearestPlayer(level).map(Entity::position).orElse(Vec3.atCenterOf(pos.above())));
                eventHandler.setStage(RitualStage.POTION_DROPPED, 1200);
            }
            case POTION_DROPPED -> {
                eventHandler.setStage(RitualStage.STARTED);
            }
            case POTION_DRANK -> {
                spawnBoss(level, pos);
            }
            case SPAWN_DRUID -> {
                eventHandler.removePlacedItem();
                eventHandler.sendEvent(2, 0);
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                Optional.ofNullable(ForgeRegistries.ENTITY_TYPES.tags())
                        .map(tags -> tags.getTag(MineriaEntities.Tags.DRUIDS))
                        .flatMap(tag -> tag.getRandomElement(level.random))
                        .ifPresent(type -> type.spawn((ServerLevel) level, pos.above(2), MobSpawnType.EVENT));
                eventHandler.setStage(RitualStage.IDLE);
            }
        }
    }

    private void checkForItemAndConsume(Item item, int count, Runnable stageUpdate, RitualEventHandler eventHandler) {
        if (this.placedItem.getItem().equals(item) && this.placedItem.getCount() == count) {
            eventHandler.removePlacedItem();
            if(level != null) {
                this.level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
            }
            stageUpdate.run();
        }
    }

    private void spawnBoss(Level level, BlockPos pos) {
        Optional<Player> nearestPlayer = findNearestPlayer(level);
        if (level instanceof ServerLevel world) {
            MineriaLightningBoltEntity.create(world, pos.above(), MobSpawnType.EVENT, true, 2, Player.class::isInstance).ifPresent(world::addFreshEntityWithPassengers);
            GreatDruidOfGaulsEntity boss = MineriaEntities.GREAT_DRUID_OF_GAULS.get().spawn(world, pos.above(2), MobSpawnType.EVENT);
            if (boss != null) {
                nearestPlayer.filter(player -> !player.getAbilities().instabuild).ifPresent(boss::setTarget);
            }
        }
        druids.forEach(druid -> {
            druid.setInvoking(false);
            druid.setRitualPosition(null);
            druid.setRitualTablePosition(null);
            nearestPlayer.filter(player -> !player.getAbilities().instabuild).ifPresent(player -> {
                druid.setTarget(player);
                druid.setPersistentAngerTarget(player.getUUID());
            });
        });
        level.removeBlock(pos, false);
    }

    private Optional<Player> findNearestPlayer(Level world) {
        return world.getNearbyPlayers(TargetingConditions.DEFAULT, null, new AABB(this.getBlockPos().offset(-5, -5, -5), this.getBlockPos().offset(5, 5, 5))).stream().findFirst();
    }


    public void clientTick(@Nonnull Level level, @Nonnull BlockPos pos) {
        switch (currentStage) {
            case STARTED -> renderParticles(level, pos, Direction.NORTH);
            case MISTLETOE_CONSUMED -> renderParticles(level, pos, Direction.NORTH, Direction.EAST);
            case VANADIUM_CONSUMED -> renderParticles(level, pos, Direction.NORTH, Direction.EAST, Direction.SOUTH);
            case LAPIS_CONSUMED -> renderParticles(level, pos, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        }
    }

    /**
     * Tries resolving the druid entities by their unique id.
     * @param level The current level.
     */
    private void loadDruidEntities(Level level) {
        if(level instanceof ServerLevel serverLevel) {
            // On server side we are provided with a method which accepts UUID
            for (UUID uuid : druidUUIDs) {
                Entity entity = serverLevel.getEntity(uuid);
                if(entity instanceof AbstractDruidEntity druid) {
                    druids.add(druid);
                }
            }
        } else if(level instanceof ClientLevel) {
            for (UUID uuid : druidUUIDs) {
                LevelEntityGetter<Entity> getter = getEntityGetter(level);
                if(getter == null) {
                    continue;
                }
                Entity entity = getter.get(uuid);
                if(entity instanceof AbstractDruidEntity druid) {
                    druids.add(druid);
                }
            }
        }
        if(!druids.isEmpty()) {
            druidUUIDs.clear();
        }
    }

    private static Method GET_ENTITIES;

    /**
     * Reflection for 'Level#getEntities'.
     * @param level The level instance.
     * @return a LevelEntityGetter.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private static LevelEntityGetter<Entity> getEntityGetter(Level level) {
        if(GET_ENTITIES == null) {
            GET_ENTITIES = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
        }
        try {
            return (LevelEntityGetter<Entity>) GET_ENTITIES.invoke(level);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
        return null;
    }

    private final ParticleOptions dustParticles = new DustParticleOptions(new Vector3f(0.27F, 0.69F, 0.75F), 1);

    /**
     * Renders a particle trail in the specified directions.
     *
     * @param level The current level.
     * @param pos The block entity position.
     * @param directions The directions in which the trail should be rendered (horizontal only).
     */
    private void renderParticles(Level level, BlockPos pos, Direction... directions) {
        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                for (int i = 0; i < 20; i++) {
                    Vec3i normal = direction.getNormal();
                    BlockPos startPos = pos.offset(normal.getX() * 2, 1, normal.getZ() * 2);
                    double x = startPos.getX() + 0.5 + level.random.nextGaussian() * -normal.getX() * 0.75;
                    double y = startPos.getY() + 0.5;
                    double z = startPos.getZ() + 0.5 + level.random.nextGaussian() * -normal.getZ() * 0.75;
                    double dx = level.random.nextGaussian() * 0.01;
                    double dy = level.random.nextGaussian();
                    double dz = level.random.nextGaussian() * 0.01;

                    level.addParticle(dustParticles, x, y, z, dx, dy, dz);
                }
            }
        }
    }

    /**
     * Triggers checks for starting the summoning ritual of the Great Druid of Gauls.
     * If the blocks are correctly placed and five druids are found the area, the ritual is started.
     *
     * @param level The current level.
     * @param pos The position of the block entity.
     * @param player The player using the ritual table.
     */
    public void tryStartBossRitual(Level level, BlockPos pos, Player player) {
        if(currentStage != RitualStage.IDLE) {
            return;
        }

        BlockPattern.BlockPatternMatch patternMatch = RitualTableBlock.getOrCreateShape().find(level, this.getBlockPos().offset(-3, 0, -3));
        if(patternMatch == null) {
            if(level.isClientSide) {
                player.displayClientMessage(Component.translatable("msg.mineria.ritual_table.misplaced_blocks"), false);
            }
            return;
        }

        @SuppressWarnings("DataFlowIssue")
        List<AbstractDruidEntity> foundDruids = level.getNearbyEntities(AbstractDruidEntity.class, TargetingConditions.DEFAULT, null, new AABB(pos.offset(-30, -20, -30), pos.offset(20, 20, 20)));
        foundDruids.sort(Comparator.comparingDouble(druid -> druid.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())));
        if(foundDruids.size() < 5) {
            if(level.isClientSide) {
                player.displayClientMessage(Component.translatable("msg.mineria.ritual_table.not_enough_druids", foundDruids.size()), false);
            }
            return;
        }

        if(!level.isClientSide) {
            druids.clear();
            druids.addAll(foundDruids);
            druids.forEach(druid -> druid.callForRitual(patternMatch, druids.indexOf(druid)));
        }
        this.areaProtection = true;
    }

    /**
     * Places an item on the ritual table and delays the next ritual stage.<br>
     * <strong>Must be called on both sides.</strong>
     * @param toPlace The item to place.
     */
    public void placeItem(Level level, ItemStack toPlace) {
        setPlacedItem(toPlace.copy());
        // nextStageDelay field is unused in client
        if(!level.isClientSide && currentStage.itemPlacedDelay() > 0) {
            nextStageDelay = currentStage.itemPlacedDelay();
        }
    }

    public void setPlacedItem(ItemStack toPlace) {
        this.placedItem = toPlace;
        setChanged();
    }

    public ItemStack getPlacedItem() {
        return placedItem;
    }

    /**
     * Notifies the ritual table that a nearby player drank a yew poisoning potion.
     * It is only effective in the {@link RitualStage#POTION_DROPPED} stage as drinking the potion
     * is a required step in the ritual.<br>
     * <strong>Note: This method is and should be called only on server side!</strong>
     *
     * @param level The current level. (ServerLevel in theory)
     * @param pos The position of the block entity.
     */
    public void setPotionDrank(Level level, BlockPos pos) {
        if (currentStage == RitualStage.POTION_DROPPED) {
            currentStage = RitualStage.POTION_DRANK;
            nextStageDelay = 40;
            level.blockEvent(pos, getBlockState().getBlock(), 1, currentStage.ordinal());
            druids.forEach(druid -> druid.setInvoking(true));
        }
    }

    /**
     * Method that receives block events.<br>
     * Used to retrieve messages sent from the Server to the Client.
     *
     * @param id The id of the event.
     * @param arg An optional argument for the event.
     * @return {@code true} if the event id is valid and something was changed.
     */
    @Override
    public boolean triggerEvent(int id, int arg) {
        switch (id) {
            case 1 -> {
                currentStage = RitualStage.byOrdinal(arg);
                setChanged();
                return true;
            }
            case 2 -> {
                setPlacedItem(ItemStack.EMPTY);
                return true;
            }
        }
        return super.triggerEvent(id, arg);
    }

    public boolean isAreaProtected() {
        return areaProtection;
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("PlacedItem", this.placedItem.save(new CompoundTag()));
        nbt.put("Druids", Util.make(new ListTag(), list -> {
            if(druids.isEmpty()) {
                // This is actually not necessary (as the druid list is unused in client side) but still
                druidUUIDs.forEach(uuid -> list.add(NbtUtils.createUUID(uuid)));
            } else {
                druids.forEach(druid -> list.add(NbtUtils.createUUID(druid.getUUID())));
            }
        }));
        nbt.putInt("CurrentStage", this.currentStage.ordinal());
        nbt.putInt("NextStageDelay", this.nextStageDelay);
        nbt.putBoolean("AreaProtection", this.areaProtection);
        nbt.putBoolean("RequiresCleaningArea", this.requiresCleaningArea);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.placedItem = ItemStack.of(nbt.getCompound("PlacedItem"));
        nbt.getList("Druids", 11).forEach(tag -> druidUUIDs.add(NbtUtils.loadUUID(tag)));
        this.currentStage = RitualStage.byOrdinal(nbt.getInt("CurrentStage"));
        this.nextStageDelay = nbt.getInt("NextStageDelay");
        this.areaProtection = nbt.getBoolean("AreaProtection");
        this.requiresCleaningArea = nbt.getBoolean("RequiresCleaningArea");
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private enum RitualStage {
        IDLE,
        SPAWN_DRUID,
        STARTED(30),
        MISTLETOE_CONSUMED(30),
        VANADIUM_CONSUMED(30),
        LAPIS_CONSUMED,
        DRUIDS_DRINK,
        POTION_DROPPED,
        POTION_DRANK;

        /**
         * Delay ticks after an item is placed on the ritual table.
         */
        private final int itemPlacedDelay;

        RitualStage() {
            this(0);
        }

        RitualStage(int itemPlacedDelay) {
            this.itemPlacedDelay = itemPlacedDelay;
        }

        public int itemPlacedDelay() {
            return itemPlacedDelay;
        }

        public static RitualStage byOrdinal(int index) {
            if(index < 0 || index >= RitualStage.values().length) {
                return IDLE;
            }
            return RitualStage.values()[index];
        }
    }

    /**
     * Interface with utility methods used in the Block Entity 'tick' method.
     */
    private interface RitualEventHandler {
        /**
         * Updates the current ritual stage and sends updates to client.
         * @param stage The new stage.
         * @param delay An optional delay before stage logic execution starts (0 or less means no delay).
         */
        void setStage(RitualStage stage, int delay);

        /**
         * Removes the placed item on the ritual table and sends changes to client.
         */
        void removePlacedItem();

        /**
         * Sends a block event to the client.
         * See 'RitualTableTileEntity#triggerEvent'
         * @param id The id of the event.
         * @param param An optional argument for the event.
         */
        void sendEvent(int id, int param);

        default void setStage(RitualStage stage) {
            setStage(stage, 0);
        }
    }
}
