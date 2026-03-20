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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class SPlayerCarrySyncPacket {

    private final ArrayList<CompoundTag> carriedPlayers;
    private final ArrayList<CarryPosition> customPos;

    public SPlayerCarrySyncPacket(ArrayList<CompoundTag> carriedPlayers, ArrayList<CarryPosition> customPos){
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

        listSize = buffer.readInt();
        ArrayList<CarryPosition> tempPos = new ArrayList<CarryPosition>();
        for(int i = 0; i < listSize; i++){
            tempPos.add(new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean()));
        }
        this.customPos = tempPos;
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(carriedPlayers.size());

        for (CompoundTag compoundTag : carriedPlayers) {
            buffer.writeNbt(compoundTag);
        }

        buffer.writeInt(customPos.size());
        for (CarryPosition carryPos : customPos) {
            buffer.writeUtf(carryPos.posName);
            buffer.writeInt(carryPos.RotationOffset);
            buffer.writeDouble(carryPos.leftRightMove);
            buffer.writeDouble(carryPos.vertOffset);
            buffer.writeDouble(carryPos.xymult);
            buffer.writeBoolean(carryPos.headLink);
        }
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
