package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class SEntityAddTargetScalePacket {

    private final float scale;
    private final UUID uuid;
    private final int numericId;
    private final boolean player;

    // for scaleType 0 - sets target,  1- mults target
    //probably gonna add more, not sure on the default yet maybe just setting to 1
    public SEntityAddTargetScalePacket(float scale, UUID uuid, int numericId, boolean player){
        this.scale = scale;
        this.uuid = uuid;
        this.numericId = numericId;
        this.player = player;
    }
    
    public SEntityAddTargetScalePacket(FriendlyByteBuf buffer){
        this(buffer.readFloat(), buffer.readUUID(), buffer.readInt(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(scale);
        buffer.writeUUID(uuid);
        buffer.writeInt(numericId);
        buffer.writeBoolean(player);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        
        ServerPlayer sender = context.get().getSender();

        if (player) {
            ServerPlayer target = sender.server.getPlayerList().getPlayer(uuid);

            if(target != null){
                PlayerSizeUtils.addSize(target, scale);
            }
        }else{
            Entity target = sender.level().getEntity(numericId);

            if (target != null && target.getUUID().compareTo(uuid) == 0) {
                EntitySizeUtils.addSize(target, scale);
            }
        }
    }
}
