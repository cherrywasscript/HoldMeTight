package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SEntitySetTargetScalePacket {

    private final float scale;
    private final UUID uuid;
    private final int ticks;

    // for scaleType 0 - sets target,  1- mults target
    //probably gonna add more, not sure on the default yet maybe just setting to 1
    public SEntitySetTargetScalePacket(float scale, UUID uuid, int ticks){
        this.scale = scale;
        this.uuid = uuid;
        this.ticks = ticks;
    }
    
    public SEntitySetTargetScalePacket(FriendlyByteBuf buffer){
        this(buffer.readFloat(), buffer.readUUID(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(scale);
        buffer.writeUUID(uuid);
        buffer.writeInt(ticks);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        
        ServerPlayer player = context.get().getSender();
        
        ServerPlayer target = player.server.getPlayerList().getPlayer(uuid);

        if(target != null){
            PlayerSizeUtils.setSize(target, scale, ticks);
            /* 
            if (ticks > 0) {
                SizeUtils.setSizeOverTimeCustom(target, scale, ticks);
            }else{
                SizeUtils.setSizeInstant(target, scale);
            } 
                */
        }
    }
}
