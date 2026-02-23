package com.ricardthegreat.holdmetight.network.clientbound;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerCarrySyncPacket {

    private final ArrayList<CompoundTag> carriedPlayers;
    private final CarryPosition customPos;
    private final UUID uuid;

    public CPlayerCarrySyncPacket(ArrayList<CompoundTag> carriedPlayers, CarryPosition customPos,UUID uuid){
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
        this.customPos = new CarryPosition(buffer.readUtf(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean());
        this.uuid = buffer.readUUID();
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
