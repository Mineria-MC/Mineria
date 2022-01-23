package com.mineria.mod.common.blocks.ritual_table;

import com.mineria.mod.common.entity.AbstractDruidEntity;
import com.mineria.mod.common.entity.GreatDruidOfGaulsEntity;
import com.mineria.mod.common.entity.MineriaLightningBoltEntity;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaPotions;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RitualTableTileEntity extends TileEntity implements ITickableTileEntity
{
    private ItemStack placedItem = ItemStack.EMPTY;
    private final List<UUID> druidUUIDs = new ArrayList<>();
    private final List<AbstractDruidEntity> druids = new ArrayList<>();
    private RitualStage currentStage = RitualStage.NOT_STARTED;
    private int nextStageDelay;
    private boolean potionDrank;
    private boolean areaProtection = true;

    public RitualTableTileEntity()
    {
        super(MineriaTileEntities.RITUAL_TABLE.get());
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

    @Override
    public void tick()
    {
        if(level == null)
            return;

        if(druidUUIDs.isEmpty())
        {
            druids.removeIf(druid -> !druid.isAlive());

            if(nextStageDelay > 0)
                nextStageDelay--;

            if(this.currentStage == RitualStage.POTION_DRANK)
            {
                if(nextStageDelay == 0)
                {
                    Optional<PlayerEntity> nearestPlayer = this.findNearestPlayer(this.level);
                    if(!level.isClientSide)
                    {
                        ServerWorld world = (ServerWorld) level;
                        MineriaLightningBoltEntity.create(world, this.getBlockPos().above(), SpawnReason.EVENT, true, 2, entity -> true).ifPresent(world::addFreshEntityWithPassengers);
                        GreatDruidOfGaulsEntity gdog = MineriaEntities.GREAT_DRUID_OF_GAULS.get().spawn(world, null, null, null, this.getBlockPos().above(2), SpawnReason.EVENT, false, false);
                        if(gdog != null)
                            nearestPlayer.filter(player -> !player.abilities.instabuild).ifPresent(gdog::setTarget);
                    }
                    this.druids.forEach(druid -> {
                        druid.setRitualPosition(null);
                        druid.setRitualTablePosition(null);
                        nearestPlayer.filter(player -> !player.abilities.instabuild).ifPresent(player -> {
                            druid.setTarget(player);
                            druid.setPersistentAngerTarget(player.getUUID());
                        });
                    });
                    this.level.removeBlock(this.getBlockPos(), false);
                }
            } else
            {
                if(canPerformRitual() && currentStage != RitualStage.SPAWN_DRUID)
                {
                    switch(this.currentStage)
                    {
                        case NOT_STARTED:
                            this.currentStage = RitualStage.STARTED;
                            break;
                        case STARTED:
                            this.renderParticles(this.getBlockPos(), Direction.NORTH);
                            this.checkForItemAndConsume(MineriaItems.MISTLETOE, 16, RitualStage.MISTLETOE_CONSUMED);
                            break;
                        case MISTLETOE_CONSUMED:
                            this.renderParticles(this.getBlockPos(), Direction.NORTH, Direction.EAST);
                            this.checkForItemAndConsume(MineriaItems.VANADIUM_INGOT, 1, RitualStage.VANADIUM_CONSUMED);
                            break;
                        case VANADIUM_CONSUMED:
                            this.renderParticles(this.getBlockPos(), Direction.NORTH, Direction.EAST, Direction.SOUTH);
                            this.checkForItemAndConsume(Items.LAPIS_LAZULI, 64, RitualStage.LAPIS_CONSUMED);
                            if(currentStage == RitualStage.LAPIS_CONSUMED) this.nextStageDelay = 40;
                            break;
                        case LAPIS_CONSUMED:
                            this.renderParticles(this.getBlockPos(), Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
                            if(this.nextStageDelay == 0)
                            {
                                this.currentStage = RitualStage.DRUIDS_DRINK;
                                this.druids.forEach(druid -> druid.setItemInHand(Hand.MAIN_HAND, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LUCK)));
                                this.nextStageDelay = 60;
                            }
                            break;
                        case DRUIDS_DRINK:
                            if(this.nextStageDelay == 0)
                            {
                                this.druids.forEach(druid -> druid.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY));
                                BrainUtil.throwItem(this.druids.get(0), PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_POTION), MineriaPotions.YEW_POISONING.get()), findNearestPlayer(this.level).map(Entity::position).orElse(Vector3d.atCenterOf(this.getBlockPos().above())));
                                this.nextStageDelay = 60 * 20;
                                this.currentStage = RitualStage.POTION_DROPPED;
                            }
                            break;
                        case POTION_DROPPED:
                            if(this.nextStageDelay == 0)
                            {
                                this.currentStage = RitualStage.STARTED;
                            }
                            else if(potionDrank)
                            {
                                this.potionDrank = false;
                                this.currentStage = RitualStage.POTION_DRANK;
                                this.nextStageDelay = 40;
                            }
                            break;
                    }
                } else
                {
                    if (this.currentStage == RitualStage.NOT_STARTED)
                    {
                        if (this.placedItem.getItem().equals(MineriaItems.VANADIUM_INGOT) && this.placedItem.getCount() == 1)
                        {
                            currentStage = RitualStage.SPAWN_DRUID;
                            this.nextStageDelay = 100;
                        }
                    } else if(this.currentStage == RitualStage.SPAWN_DRUID)
                    {
                        this.renderParticles(this.getBlockPos(), Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
                        if (nextStageDelay == 0)
                        {
                            this.placedItem = ItemStack.EMPTY;
                            this.level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
                            if(!level.isClientSide)
                            {
                                MineriaUtils.getRandomElement(MineriaEntities.Tags.DRUIDS.getValues()).spawn((ServerWorld) this.level, null, null, null, this.getBlockPos().above(2), SpawnReason.EVENT, false, false);
                            }
                            currentStage = RitualStage.NOT_STARTED;
                        }
                    } else
                    {
                        this.currentStage = RitualStage.NOT_STARTED;
                        if(areDruidsPositioned() || druids.size() != 5)
                        {
                            this.druids.forEach(druid -> {
                                druid.setRitualTablePosition(null);
                                druid.setRitualPosition(null);
                            });
                        }
                    }
                }
            }
        } else if(gatherEntities(this.level))
            druidUUIDs.clear();
    }

    private boolean gatherEntities(World world)
    {
        if(world.isClientSide)
        {
            Iterable<Entity> entities = ((ClientWorld) world).entitiesForRendering();

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
                Entity entity = ((ServerWorld) world).getEntity(uuid);
                if(entity != null)
                {
                    this.druids.add(druidUUIDs.indexOf(uuid), (AbstractDruidEntity) entity);
                }
            }
        }

        return !druids.isEmpty();
    }

    public void tryStartRitual(World world, PlayerEntity player)
    {
        BlockPattern.PatternHelper patternHelper = RitualTableBlock.getOrCreateShape().find(world, this.getBlockPos().offset(-3, 0, -3));
        if(patternHelper != null)
        {
            if(druids.size() < 5)
                for(AbstractDruidEntity druid : world.getNearbyEntities(AbstractDruidEntity.class, EntityPredicate.DEFAULT, null, new AxisAlignedBB(this.getBlockPos().offset(-30, -20, -30), this.getBlockPos().offset(20, 20, 20))))
                    if(!druids.contains(druid) && druids.size() < 5)
                        druids.add(druid);

            if(druids.size() >= 5)
                druids.forEach(druid -> druid.callForRitual(patternHelper, druids.indexOf(druid)));
            else if(world.isClientSide)
                player.displayClientMessage(new TranslationTextComponent("msg.mineria.ritual_table.not_enough_druids", druids.size()), false);
        } else if(world.isClientSide)
            player.displayClientMessage(new TranslationTextComponent("msg.mineria.ritual_table.misplaced_blocks"), false);
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
                        Vector3i normal = direction.getNormal();
                        BlockPos startPos = pos.offset(normal.getX() * 2, 1, normal.getZ() * 2);
                        double x = startPos.getX() + 0.5 + level.random.nextGaussian() * -normal.getX() * 0.75;
                        double y = startPos.getY() + 0.5;
                        double z = startPos.getZ() + 0.5 + level.random.nextGaussian() * -normal.getZ() * 0.75;
                        double dx = level.random.nextGaussian() * 0.01;
                        double dy = level.random.nextGaussian();
                        double dz = level.random.nextGaussian() * 0.01;

                        this.level.addParticle(new RedstoneParticleData(0.27F, 0.69F, 0.75F, 1), x, y, z, dx, dy, dz);
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
            this.level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
            this.currentStage = nextStage;
        }
    }

    private Optional<PlayerEntity> findNearestPlayer(World world)
    {
        return world.getNearbyPlayers(EntityPredicate.DEFAULT, null, new AxisAlignedBB(this.getBlockPos().offset(-5, -5, -5), this.getBlockPos().offset(5, 5, 5))).stream().findFirst();
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
    public CompoundNBT save(CompoundNBT nbt)
    {
        super.save(nbt);
        nbt.put("PlacedItem", this.placedItem.save(new CompoundNBT()));
        nbt.put("Druids", serializeDruids());
        nbt.putString("CurrentStage", this.currentStage.name());
        nbt.putInt("NextStageDelay", this.nextStageDelay);
        nbt.putBoolean("PotionDrank", this.potionDrank);
        nbt.putBoolean("AreaProtection", this.areaProtection);
        return nbt;
    }

    public ListNBT serializeDruids()
    {
        ListNBT nbt = new ListNBT();
        druids.forEach(druid -> nbt.add(NBTUtil.createUUID(druid.getUUID())));
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.placedItem = ItemStack.of(nbt.getCompound("PlacedItem"));
        deserializeDruids(nbt.getList("Druids", 11));
        this.currentStage = RitualStage.valueOf(nbt.getString("CurrentStage"));
        this.nextStageDelay = nbt.getInt("NextStageDelay");
        this.potionDrank = nbt.getBoolean("PotionDrank");
        this.areaProtection = nbt.getBoolean("AreaProtection");
    }

    public void deserializeDruids(ListNBT nbt)
    {
        nbt.forEach(inbt -> druidUUIDs.add(NBTUtil.loadUUID(inbt)));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return new SUpdateTileEntityPacket(this.worldPosition, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        this.load(state, tag);
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
