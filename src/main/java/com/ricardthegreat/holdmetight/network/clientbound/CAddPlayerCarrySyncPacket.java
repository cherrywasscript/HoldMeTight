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

public class CAddPlayerCarrySyncPacket {
    private final CompoundTag player;
    private final UUID uuid;

    public CAddPlayerCarrySyncPacket(CompoundTag player, UUID uuid){
        this.player = player;
        this.uuid = uuid;
    }

    public CAddPlayerCarrySyncPacket(FriendlyByteBuf buffer){
        this(buffer.readNbt(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeNbt(player);
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleAddPlayerPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerCarry playerCarry){
        playerCarry.addOrUpdateCarriedPlayer(player);
    }

    public UUID getUuid() {
        return uuid;
    }
}
