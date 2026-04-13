package com.ricardthegreat.holdmetight.network.serverbound.carrypositions;

import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CEditCustomCarryPosPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SEditCustomCarryPosPacket {
    private final CarryPosition customPos;
    private final int index;

    public SEditCustomCarryPosPacket(CarryPosition customPos, int index){
        this.customPos = customPos;
        this.index = index;
    }
    
    public SEditCustomCarryPosPacket(FriendlyByteBuf buffer){
        this.customPos = new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean());
        this.index = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUtf(customPos.posName);
        buffer.writeInt(customPos.RotationOffset);
        buffer.writeDouble(customPos.leftRightMove);
        buffer.writeDouble(customPos.vertOffset);
        buffer.writeDouble(customPos.xymult);
        buffer.writeBoolean(customPos.headLink);

        buffer.writeInt(index);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        ServerPlayer player = context.get().getSender();

        PlayerCarry carry = PlayerCarryProvider.getPlayerCarryCapability(player);

        if(carry != null){
            carry.editCustomCarryPos(customPos, index);

            PacketHandler.sendToAllClients(new CEditCustomCarryPosPacket(customPos, index, player.getUUID()));
        }
    }
}
