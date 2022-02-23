package com.mineria.mod.mixin;

import com.mineria.mod.common.entity.*;
import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin
{
    @Shadow private ClientLevel level;

    // TODO
    @Inject(method = "handleAddEntity", at = @At("TAIL"))
    public void handleAddEntity(ClientboundAddEntityPacket pkt, CallbackInfo ci)
    {
        double x = pkt.getX();
        double y = pkt.getY();
        double z = pkt.getZ();
        EntityType<?> type = pkt.getType();
        Entity entity = null;
        if(type == MineriaEntities.KUNAI.get())
            entity = new KunaiEntity(x, y, z, this.level);
        else if(type == MineriaEntities.MINERIA_POTION.get())
            entity = new MineriaPotionEntity(this.level, x, y, z);
        else if(type == MineriaEntities.MINERIA_AREA_EFFECT_CLOUD.get())
            entity = new MineriaAreaEffectCloudEntity(this.level, x, y, z);
        else if(type == MineriaEntities.ELEMENTAL_ORB.get())
        {
            entity = new ElementalOrbEntity(this.level, x, y, z);
            Entity owner = this.level.getEntity(pkt.getData());
            if(owner != null)
                ((ElementalOrbEntity) entity).setOwner(owner);
        }
        else if(type == MineriaEntities.DART.get())
        {
            entity = new BlowgunRefillEntity(this.level, x, y, z);
            Entity owner = this.level.getEntity(pkt.getData());
            if (owner != null)
                ((BlowgunRefillEntity) entity).setOwner(owner);
        }
        else if(type == MineriaEntities.JAR.get())
            entity = new JarEntity(x, y, z, this.level);
        else if(type == MineriaEntities.MINERIA_LIGHTNING_BOLT.get())
            entity = new MineriaLightningBoltEntity(MineriaEntities.MINERIA_LIGHTNING_BOLT.get(), this.level);
        else if(type == MineriaEntities.TEMPORARY_ITEM_FRAME.get())
            entity = new TemporaryItemFrameEntity(this.level, new BlockPos(x, y, z), Direction.from3DDataValue(pkt.getData()));

        if(entity != null)
        {
            int id = pkt.getId();
            entity.setPacketCoordinates(x, y, z);
            entity.moveTo(x, y, z);
            entity.setXRot((float) (pkt.getxRot() * 360) / 256.0F);
            entity.setYRot((float) (pkt.getyRot() * 360) / 256.0F);
            entity.setId(id);
            entity.setUUID(pkt.getUUID());
            this.level.putNonPlayerEntity(id, entity);
        }
    }
}