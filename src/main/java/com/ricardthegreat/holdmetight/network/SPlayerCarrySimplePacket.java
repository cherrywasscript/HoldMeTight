package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerCarrySimplePacket {

    private final boolean carried;
    private final boolean carrying;
    private final int[] carryPos;
    private final UUID uuid;

    public SPlayerCarrySimplePacket(boolean carried, boolean carrying, int[] carryPos, UUID uuid){
        this.carried = carried;
        this.carrying = carrying;
        this.carryPos = carryPos;
        this.uuid = uuid;
    }
    
    public SPlayerCarrySimplePacket(FriendlyByteBuf buffer){
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
        ServerPlayer player = context.get().getSender();

        PlayerCarry carry = PlayerCarryProvider.getPlayerCarryCapability(player);

        if(carry != null){
            carry.updateSimpleSyncables(carried, carrying, carryPos);

            PacketHandler.sendToAllClients(new CPlayerCarrySimplePacket(carried, carrying, carryPos, player.getUUID()));
        }

    }
    
}
