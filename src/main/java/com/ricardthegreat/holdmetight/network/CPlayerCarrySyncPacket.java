package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.Client.handlers.ClientPacketHandler;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerCarrySyncPacket {

    private final boolean carried;
    private final boolean carrying;
    private final int[] carryPos;
    private final CarryPosition custom;
    private final UUID uuid;

    public CPlayerCarrySyncPacket(boolean carried, boolean carrying, int[] carryPos, CarryPosition custom, UUID uuid){
        this.carried = carried;
        this.carrying = carrying;
        this.carryPos = carryPos;
        this.custom = custom;
        this.uuid = uuid;
    }

    public CPlayerCarrySyncPacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean(), buffer.readBoolean(), new int[]{buffer.readInt(), buffer.readInt()}, new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean()), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(carried);
        buffer.writeBoolean(carrying);

        buffer.writeInt(carryPos[0]);
        buffer.writeInt(carryPos[1]);

        buffer.writeUtf(custom.posName);
        buffer.writeInt(custom.RotationOffset);
        buffer.writeDouble(custom.leftRightMove);
        buffer.writeDouble(custom.vertOffset);
        buffer.writeDouble(custom.xymult);
        buffer.writeBoolean(custom.headLink);

        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCarryPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerCarry playerCarry){
        playerCarry.updateAllSyncables(carried, carrying, carryPos, custom);
    }

    public UUID getUuid() {
        return uuid;
    }
}
