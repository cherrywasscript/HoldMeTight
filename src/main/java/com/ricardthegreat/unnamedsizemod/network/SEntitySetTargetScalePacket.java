package com.ricardthegreat.unnamedsizemod.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.unnamedsizemod.utils.SizeUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SEntitySetTargetScalePacket {

    private final float scale;
    private final UUID uuid;

    // for scaleType 0 - sets target,  1- mults target
    //probably gonna add more, not sure on the default yet maybe just setting to 1
    public SEntitySetTargetScalePacket(float scale, UUID uuid){
        this.scale = scale;
        this.uuid = uuid;
    }
    
    public SEntitySetTargetScalePacket(FriendlyByteBuf buffer){
        this(buffer.readFloat(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(scale);
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        
        ServerPlayer player = context.get().getSender();
        
        ServerPlayer target = player.server.getPlayerList().getPlayer(uuid);

        if(target != null){
            SizeUtils.setTargetSize(target, scale); 
        }
    }
}
