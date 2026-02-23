package com.ricardthegreat.holdmetight.network.serverbound;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerCarrySyncPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerCarrySyncPacket {

    private final ArrayList<CompoundTag> carriedPlayers;
    private final CarryPosition customPos;

    public SPlayerCarrySyncPacket(ArrayList<CompoundTag> carriedPlayers, CarryPosition customPos){
        this.carriedPlayers = carriedPlayers;
        this.customPos = customPos;
    }
    
    public SPlayerCarrySyncPacket(FriendlyByteBuf buffer){
        int listSize = buffer.readInt();

        ArrayList<CompoundTag> temp = new ArrayList<CompoundTag>();
        for(int i = 0; i < listSize; i++){
            temp.add(buffer.readNbt());
        }

        this.carriedPlayers = temp;
        this.customPos = new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(carriedPlayers.size());

        for (CompoundTag compoundTag : carriedPlayers) {
            buffer.writeNbt(compoundTag);
        }

        buffer.writeUtf(customPos.posName);
        buffer.writeInt(customPos.RotationOffset);
        buffer.writeDouble(customPos.leftRightMove);
        buffer.writeDouble(customPos.vertOffset);
        buffer.writeDouble(customPos.xymult);
        buffer.writeBoolean(customPos.headLink);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        ServerPlayer player = context.get().getSender();

        PlayerCarry carry = PlayerCarryProvider.getPlayerCarryCapability(player);

        if(carry != null){
            carry.updateAllSyncables(carriedPlayers, customPos);

            PacketHandler.sendToAllClients(new CPlayerCarrySyncPacket(carriedPlayers, customPos, player.getUUID()));
        }

    }
}
