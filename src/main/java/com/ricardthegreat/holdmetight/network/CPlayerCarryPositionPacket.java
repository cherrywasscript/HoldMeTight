package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.Client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerCarryPositionPacket {
     
    private final boolean position;
    private final UUID uuid;
    private final byte target; //0 for carried, 1 for carrying, 2 for shoulder, 3 for custom, anything else is nothing

    public CPlayerCarryPositionPacket(boolean position, UUID uuid, byte target){
        this.position = position;
        this.uuid = uuid;
        this.target = target;
    }

    public CPlayerCarryPositionPacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean(), buffer.readUUID(), buffer.readByte());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(position);
        buffer.writeUUID(uuid);
        buffer.writeByte(target);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCarryPositionPacket(this, context))
        );
        context.get().setPacketHandled(true);
    }

    public boolean getPosition() {
        return position;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Byte getTarget() {
        return target;
    }
}
