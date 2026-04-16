package com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.size;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.capabilities.size.PlayerSize;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerSizeMixinSyncPacket {
    //TODO remove this class

    //private final float maxScale;
    //private final float minScale;
    //private final float defaultScale;
    private final UUID uuid;

    public CPlayerSizeMixinSyncPacket(UUID uuid){
        this.uuid = uuid;
    }

    public CPlayerSizeMixinSyncPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleSizePacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerSize playerSize){
    }

    public UUID getUuid() {
        return uuid;
    }
}
