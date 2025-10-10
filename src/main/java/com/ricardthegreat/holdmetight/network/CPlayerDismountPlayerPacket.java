package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerDismountPlayerPacket {

    private final UUID uuid;

    public CPlayerDismountPlayerPacket(UUID uuid){

        this.uuid = uuid;

    }

    public CPlayerDismountPlayerPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){

        buffer.writeUUID(uuid);

    }

    public UUID getUuid(){
        return this.uuid;
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        

        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleDismountPacket(this, context))
        );
        
        context.get().setPacketHandled(true);

    }
    
}
