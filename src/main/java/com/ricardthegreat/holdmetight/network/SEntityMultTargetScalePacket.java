package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SEntityMultTargetScalePacket {

    private final float scale;
    private final UUID uuid;
    private final int changeType;

    // for scaleType 0 - sets target,  1- mults target
    //probably gonna add more, not sure on the default yet maybe just setting to 1
    public SEntityMultTargetScalePacket(float scale, UUID uuid, int changeType){
        this.scale = scale;
        this.uuid = uuid;
        this.changeType = changeType;
    }
    
    public SEntityMultTargetScalePacket(FriendlyByteBuf buffer){
        this(buffer.readFloat(), buffer.readUUID(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(scale);
        buffer.writeUUID(uuid);
        buffer.writeInt(changeType);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        
        ServerPlayer player = context.get().getSender();
        
        ServerPlayer target = player.server.getPlayerList().getPlayer(uuid);

        if(target != null){
            SizeUtils.multSizeOverTimeDefault(target, scale);
        }
    }
}
