package com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerCarrySyncPacket {

    private final ArrayList<CompoundTag> carriedPlayers;
    private final ArrayList<CarryPosition> customPos;
    private final UUID uuid;

    public CPlayerCarrySyncPacket(ArrayList<CompoundTag> carriedPlayers, ArrayList<CarryPosition> customPos,UUID uuid){
        this.carriedPlayers = carriedPlayers;
        this.customPos = customPos;
        this.uuid = uuid;
    }

    public CPlayerCarrySyncPacket(FriendlyByteBuf buffer){
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
        
        this.uuid = buffer.readUUID();
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

        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCarryPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerCarry playerCarry){
        playerCarry.updateAllSyncables(carriedPlayers, customPos);
    }

    public UUID getUuid() {
        return uuid;
    }
}
