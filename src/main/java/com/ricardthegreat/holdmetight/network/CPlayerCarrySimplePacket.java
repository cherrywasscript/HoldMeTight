package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerCarrySimplePacket {
     
    private final boolean carried;
    private final boolean carrying;
    private final int[] carryPos;
    private final UUID uuid;

    public CPlayerCarrySimplePacket(boolean carried, boolean carrying, int[] carryPos, UUID uuid){
        this.carried = carried;
        this.carrying = carrying;
        this.carryPos = carryPos;
        this.uuid = uuid;
    }

    public CPlayerCarrySimplePacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean(), buffer.readBoolean(), new int[]{buffer.readInt(),buffer.readInt()}, buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(carried);
        buffer.writeBoolean(carrying);
        buffer.writeInt(carryPos[0]);
        buffer.writeInt(carryPos[1]);
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCarryPositionPacket(carried, carrying, carryPos, uuid, context))
        );
        context.get().setPacketHandled(true);
    }
}
