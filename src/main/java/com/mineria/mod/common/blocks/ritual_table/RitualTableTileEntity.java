package com.mineria.mod.common.blocks.ritual_table;

import com.mineria.mod.common.entity.AbstractDruidEntity;
import com.mineria.mod.common.entity.GreatDruidOfGaulsEntity;
import com.mineria.mod.common.entity.MineriaLightningBoltEntity;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaPotions;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.MineriaUtils;
import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RitualTableTileEntity extends BlockEntity
{
    private ItemStack placedItem = ItemStack.EMPTY;
    private final List<UUID> druidUUIDs = new ArrayList<>();
    private final List<AbstractDruidEntity> druids = new ArrayList<>();
    private RitualStage currentStage = RitualStage.NOT_STARTED;
    private int nextStageDelay;
    private boolean potionDrank;
    private boolean areaProtection = true;

    public RitualTableTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.RITUAL_TABLE.get(), pos, state);
    }

    public void placeItem(ItemStack placedItem)
    {
        setPlacedItem(placedItem.copy());
        if(this.currentStage.ordinal() > 1 && this.currentStage.ordinal() < 5)
            nextStageDelay = 30;
    }

    public void setPlacedItem(ItemStack placedItem)
    {
        this.placedItem = placedItem;
    }

    public ItemStack getPlacedItem()
    {
        return placedItem;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RitualTableTileEntity tile)
    {
        if(level == null)
            return;

        if(tile.druidUUIDs.isEmpty())
        {
            tile.druids.removeIf(druid -> !druid.isAlive());

            if(tile.nextStageDelay > 0)
                tile.nextStageDelay--;

            if(tile.currentStage == RitualStage.POTION_DRANK)
            {
                if(tile.nextStageDelay == 0)
                {
                    Optional<Player> nearestPlayer = tile.findNearestPlayer(level);
                    if(!level.isClientSide)
                    {
                        ServerLevel world = (ServerLevel) level;
                        MineriaLightningBoltEntity.create(world, pos.above(), MobSpawnType.EVENT, true, 2, entity -> true).ifPresent(world::addFreshEntityWithPassengers);
                        GreatDruidOfGaulsEntity gdog = MineriaEntities.GREAT_DRUID_OF_GAULS.get().spawn(world, null, null, null, pos.above(2), MobSpawnType.EVENT, false, false);
                        if(gdog != null)
                            nearestPlayer.filter(player -> !player.getAbilities().instabuild).ifPresent(gdog::setTarget);
                    }
                    tile.druids.forEach(druid -> {
                        druid.setRitualPosition(null);
                        druid.setRitualTablePosition(null);
                        nearestPlayer.filter(player -> !player.getAbilities().instabuild).ifPresent(player -> {
                            druid.setTarget(player);
                            druid.setPersistentAngerTarget(player.getUUID());
                        });
                    });
                    level.removeBlock(pos, false);
                }
            } else
            {
                if(tile.canPerformRitual() && tile.currentStage != RitualStage.SPAWN_DRUID)
                {
                    switch(tile.currentStage)
                    {
                        case NOT_STARTED:
                            tile.currentStage = RitualStage.STARTED;
                            break;
                        case STARTED:
                            tile.renderParticles(pos, Direction.NORTH);
                            tile.checkForItemAndConsume(MineriaItems.MISTLETOE, 16, RitualStage.MISTLETOE_CONSUMED);
                            break;
                        case MISTLETOE_CONSUMED:
                            tile.renderParticles(pos, Direction.NORTH, Direction.EAST);
                            tile.checkForItemAndConsume(MineriaItems.VANADIUM_INGOT, 1, RitualStage.VANADIUM_CONSUMED);
                            break;
                        case VANADIUM_CONSUMED:
                            tile.renderParticles(pos, Direction.NORTH, Direction.EAST, Direction.SOUTH);
                            tile.checkForItemAndConsume(Items.LAPIS_LAZULI, 64, RitualStage.LAPIS_CONSUMED);
                            if(tile.currentStage == RitualStage.LAPIS_CONSUMED) tile.nextStageDelay = 40;
                            break;
                        case LAPIS_CONSUMED:
                            tile.renderParticles(pos, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
                            if(tile.nextStageDelay == 0)
                            {
                                tile.currentStage = RitualStage.DRUIDS_DRINK;
                                tile.druids.forEach(druid -> druid.setItemInHand(InteractionHand.MAIN_HAND, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LUCK)));
                                tile.nextStageDelay = 60;
                            }
                            break;
                        case DRUIDS_DRINK:
                            if(tile.nextStageDelay == 0)
                            {
                                tile.druids.forEach(druid -> druid.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY));
                                BehaviorUtils.throwItem(tile.druids.get(0), PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_POTION), MineriaPotions.YEW_POISONING.get()), tile.findNearestPlayer(level).map(Entity::position).orElse(Vec3.atCenterOf(pos.above())));
                                tile.nextStageDelay = 60 * 20;
                                tile.currentStage = RitualStage.POTION_DROPPED;
                            }
                            break;
                        case POTION_DROPPED:
                            if(tile.nextStageDelay == 0)
                            {
                                tile.currentStage = RitualStage.STARTED;
                            }
                            else if(tile.potionDrank)
                            {
                                tile.potionDrank = false;
                                tile.currentStage = RitualStage.POTION_DRANK;
                                tile.nextStageDelay = 40;
                            }
                            break;
                    }
                } else
                {
                    if (tile.currentStage == RitualStage.NOT_STARTED)
                    {
                        if (tile.placedItem.getItem().equals(MineriaItems.VANADIUM_INGOT) && tile.placedItem.getCount() == 1)
                        {
                            tile.currentStage = RitualStage.SPAWN_DRUID;
                            tile.nextStageDelay = 100;
                        }
                    } else if(tile.currentStage == RitualStage.SPAWN_DRUID)
                    {
                        tile.renderParticles(pos, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
                        if (tile.nextStageDelay == 0)
                        {
                            tile.placedItem = ItemStack.EMPTY;
                            tile.level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                            if(!level.isClientSide)
                            {
                                MineriaUtils.getRandomElement(MineriaEntities.Tags.DRUIDS.getValues()).spawn((ServerLevel) level, null, null, null, tile.getBlockPos().above(2), MobSpawnType.EVENT, false, false);
                            }
                            tile.currentStage = RitualStage.NOT_STARTED;
                        }
                    } else
                    {
                        tile.currentStage = RitualStage.NOT_STARTED;
                        if(tile.areDruidsPositioned() || tile.druids.size() != 5)
                        {
                            tile.druids.forEach(druid -> {
                                druid.setRitualTablePosition(null);
                                druid.setRitualPosition(null);
                            });
                        }
                    }
                }
            }
        } else if(tile.gatherEntities(level))
            tile.druidUUIDs.clear();
    }

    private boolean gatherEntities(Level world)
    {
        if(world.isClientSide)
        {
            Iterable<Entity> entities = ((ClientLevel) world).entitiesForRendering();

            for(UUID uuid : this.druidUUIDs)
            {
                for(Entity entity : entities)
                {
                    if(entity.getUUID().equals(uuid))
                    {
                        // Maybe fixed IDK ? (Crash when reload chunks)
                        this.druids.add(druidUUIDs.indexOf(uuid), (AbstractDruidEntity) entity);
                    }
                }
            }
        } else
        {
            for(UUID uuid : this.druidUUIDs)
            {
                Entity entity = ((ServerLevel) world).getEntity(uuid);
                if(entity != null)
                {
                    this.druids.add(druidUUIDs.indexOf(uuid), (AbstractDruidEntity) entity);
                }
            }
        }

        return !druids.isEmpty();
    }

    public void tryStartRitual(Level world, Player player)
    {
        BlockPattern.BlockPatternMatch patternHelper = RitualTableBlock.getOrCreateShape().find(world, this.getBlockPos().offset(-3, 0, -3));
        if(patternHelper != null)
        {
            if(druids.size() < 5)
                for(AbstractDruidEntity druid : world.getNearbyEntities(AbstractDruidEntity.class, TargetingConditions.DEFAULT, null, new AABB(this.getBlockPos().offset(-30, -20, -30), this.getBlockPos().offset(20, 20, 20))))
                    if(!druids.contains(druid) && druids.size() < 5)
                        druids.add(druid);

            if(druids.size() >= 5)
                druids.forEach(druid -> druid.callForRitual(patternHelper, druids.indexOf(druid)));
            else if(world.isClientSide)
                player.displayClientMessage(new TranslatableComponent("msg.mineria.ritual_table.not_enough_druids", druids.size()), false);
        } else if(world.isClientSide)
            player.displayClientMessage(new TranslatableComponent("msg.mineria.ritual_table.misplaced_blocks"), false);
    }

    private boolean canPerformRitual()
    {
        return areDruidsPositioned() && druids.size() == 5 && RitualTableBlock.getOrCreateShape().find(level, this.getBlockPos().offset(-3, 0, -3)) != null;
    }

    private boolean areDruidsPositioned()
    {
        for(AbstractDruidEntity druid : druids)
        {
            if(!(druid.getNavigation().isDone() && druid.getRitualPosition().isPresent()))
            {
                return false;
            }
        }

        return true;
    }

    private void renderParticles(BlockPos pos, Direction... directions)
    {
        if(this.level != null && this.level.isClientSide)
        {
            for(Direction direction : directions)
            {
                if(direction.getAxis().isHorizontal())
                {
                    for(int i = 0; i < 20; i++)
                    {
                        Vec3i normal = direction.getNormal();
                        BlockPos startPos = pos.offset(normal.getX() * 2, 1, normal.getZ() * 2);
                        double x = startPos.getX() + 0.5 + level.random.nextGaussian() * -normal.getX() * 0.75;
                        double y = startPos.getY() + 0.5;
                        double z = startPos.getZ() + 0.5 + level.random.nextGaussian() * -normal.getZ() * 0.75;
                        double dx = level.random.nextGaussian() * 0.01;
                        double dy = level.random.nextGaussian();
                        double dz = level.random.nextGaussian() * 0.01;

                        this.level.addParticle(new DustParticleOptions(new Vector3f(0.27F, 0.69F, 0.75F), 1), x, y, z, dx, dy, dz);
                    }
                }
            }
        }
    }

    private void checkForItemAndConsume(Item item, int count, RitualStage nextStage)
    {
        if(this.placedItem.getItem().equals(item) && this.placedItem.getCount() == count && nextStageDelay == 0)
        {
            this.placedItem = ItemStack.EMPTY;
            this.level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
            this.currentStage = nextStage;
        }
    }

    private Optional<Player> findNearestPlayer(Level world)
    {
        return world.getNearbyPlayers(TargetingConditions.DEFAULT, null, new AABB(this.getBlockPos().offset(-5, -5, -5), this.getBlockPos().offset(5, 5, 5))).stream().findFirst();
    }

    public void setPotionDrank()
    {
        if(currentStage == RitualStage.POTION_DROPPED) this.potionDrank = true;
    }

    private List<BlockPos> getTurnPositions(AbstractDruidEntity druid)
    {
        List<BlockPos> result = new ArrayList<>();

        for(int i = 1; i < 6; i++)
        {
            int index = druids.indexOf(druid) + 1;
            if(index >= 5) index -= 5;
            Optional<BlockPos> ritualPos = druids.get(index).getRitualPosition();
            if(ritualPos.isPresent())
                result.add(ritualPos.get());
            else
                return new ArrayList<>();
        }

        return result;
    }

    /*private void renderParticles()
    {
        for(int i = 0; i < 5; i++)
        {
            BlockPos pos = getBlockPos();
            double posX = pos.getX() + 0.5 + level.random.nextGaussian() * 0;
            double posY = pos.getY() + 2 + level.random.nextGaussian() * 0;
            double posZ = pos.getZ() + 0.5 + level.random.nextGaussian() * 0.75;
            double dx = level.random.nextGaussian() * 0.01;
            double dy = level.random.nextGaussian();
            double dz = level.random.nextGaussian() * 0.01;

            for(int j = 0; j < 4; j++)
                this.level.addParticle(new RedstoneParticleData(0.27F, 0.69F, 0.75F, 1), posX, posY, posZ, dx, i * 2, dz);
        }
    }*/

    public boolean isAreaProtected()
    {
        return areaProtection;
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.put("PlacedItem", this.placedItem.save(new CompoundTag()));
        nbt.put("Druids", serializeDruids());
        nbt.putString("CurrentStage", this.currentStage.name());
        nbt.putInt("NextStageDelay", this.nextStageDelay);
        nbt.putBoolean("PotionDrank", this.potionDrank);
        nbt.putBoolean("AreaProtection", this.areaProtection);
        return nbt;
    }

    public ListTag serializeDruids()
    {
        ListTag nbt = new ListTag();
        druids.forEach(druid -> nbt.add(NbtUtils.createUUID(druid.getUUID())));
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.placedItem = ItemStack.of(nbt.getCompound("PlacedItem"));
        deserializeDruids(nbt.getList("Druids", 11));
        this.currentStage = RitualStage.valueOf(nbt.getString("CurrentStage"));
        this.nextStageDelay = nbt.getInt("NextStageDelay");
        this.potionDrank = nbt.getBoolean("PotionDrank");
        this.areaProtection = nbt.getBoolean("AreaProtection");
    }

    public void deserializeDruids(ListTag nbt)
    {
        nbt.forEach(inbt -> druidUUIDs.add(NbtUtils.loadUUID(inbt)));
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        this.load(tag);
    }

    private enum RitualStage
    {
        NOT_STARTED,
        SPAWN_DRUID,
        STARTED,
        MISTLETOE_CONSUMED,
        VANADIUM_CONSUMED,
        LAPIS_CONSUMED,
        DRUIDS_DRINK,
        POTION_DROPPED,
        POTION_DRANK
    }
}
