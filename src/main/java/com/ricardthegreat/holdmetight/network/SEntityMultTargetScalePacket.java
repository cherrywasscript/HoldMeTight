package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class SEntityMultTargetScalePacket {

    private final float scale;
    private final UUID uuid;
    private final int numericId;
    private final int ticks;
    private final boolean player;

    public SEntityMultTargetScalePacket(float scale, UUID uuid, int numericId, int ticks, boolean player){
        this.scale = scale;
        this.uuid = uuid;
        this.numericId = numericId;
        this.ticks = ticks;
        this.player = player;
    }
    
    public SEntityMultTargetScalePacket(FriendlyByteBuf buffer){
        this(buffer.readFloat(), buffer.readUUID(), buffer.readInt(), buffer.readInt(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(scale);
        buffer.writeUUID(uuid);
        buffer.writeInt(numericId);
        buffer.writeInt(ticks);
        buffer.writeBoolean(player);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        
        ServerPlayer sender = context.get().getSender();

        if (player) {
            ServerPlayer target = sender.server.getPlayerList().getPlayer(uuid);

            if(target != null){
                PlayerSizeUtils.multSize(target, scale, ticks);
            }
        }else{
            Entity target = sender.level().getEntity(numericId);

            if (target != null && target.getUUID().compareTo(uuid) == 0) {
                EntitySizeUtils.multSize(target, scale, ticks);
            }
        }
    }
}
