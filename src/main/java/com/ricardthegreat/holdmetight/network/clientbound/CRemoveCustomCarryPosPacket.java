package com.ricardthegreat.holdmetight.network.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CRemoveCustomCarryPosPacket {
    private final String pos;
    private final UUID uuid;

    public CRemoveCustomCarryPosPacket(String pos,UUID uuid){
        this.pos = pos;
        this.uuid = uuid;
    }

    public CRemoveCustomCarryPosPacket(FriendlyByteBuf buffer){
        this.pos = buffer.readUtf();
        
        this.uuid = buffer.readUUID();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUtf(pos);

        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleRemoveCarryPosPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCarryPos(){
        return pos;
    }
}
