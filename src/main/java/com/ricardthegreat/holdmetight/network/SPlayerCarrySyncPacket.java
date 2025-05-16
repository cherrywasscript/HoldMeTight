package com.ricardthegreat.holdmetight.network;

import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerCarrySyncPacket {

    private final boolean carried;
    private final boolean carrying;
    private final int[] carryPos;
    private final CarryPosition custom;

    public SPlayerCarrySyncPacket(boolean carried, boolean carrying, int[] carryPos, CarryPosition custom){
        this.carried = carried;
        this.carrying = carrying;
        this.carryPos = carryPos;
        this.custom = custom;
    }
    
    public SPlayerCarrySyncPacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean(), buffer.readBoolean(), new int[]{buffer.readInt(), buffer.readInt()}, new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean()));
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
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        ServerPlayer player = context.get().getSender();

        PlayerCarry carry = PlayerCarryProvider.getPlayerSizeCapability(player);

        if(carry != null){
            carry.updateAllSyncables(carried, carrying, carryPos, custom);

            PacketHandler.sendToAllClients(new CPlayerCarrySyncPacket(carried, carrying, carryPos, custom, player.getUUID()));
        }

    }
    
}
