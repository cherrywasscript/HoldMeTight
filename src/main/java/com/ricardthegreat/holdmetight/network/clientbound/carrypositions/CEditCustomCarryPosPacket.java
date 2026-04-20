package com.ricardthegreat.holdmetight.network.clientbound.carrypositions;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CEditCustomCarryPosPacket {
    private final CarryPosition customPos;
    private final int index;
    private final UUID uuid;

    public CEditCustomCarryPosPacket(CarryPosition customPos, int index, UUID uuid){
        this.customPos = customPos;
        this.index = index;
        this.uuid = uuid;
    }

    public CEditCustomCarryPosPacket(FriendlyByteBuf buffer){
        this.customPos = new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean());
        
        this.index = buffer.readInt();

        this.uuid = buffer.readUUID();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUtf(customPos.posName);
        buffer.writeInt(customPos.RotationOffset);
        buffer.writeDouble(customPos.leftRightMove);
        buffer.writeDouble(customPos.vertOffset);
        buffer.writeDouble(customPos.xymult);
        buffer.writeBoolean(customPos.headLink);

        buffer.writeInt(index);

        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleEditCarryPosPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public UUID getUuid() {
        return uuid;
    }

    public CarryPosition getCarryPos(){
        return customPos;
    }

    public int getIndex(){
        return index;
    }
}
