package com.ricardthegreat.holdmetight.network.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CRemovePlayerCarrySyncPacket {
    private final UUID player;
    private final UUID uuid;

    public CRemovePlayerCarrySyncPacket(UUID player, UUID uuid){
        this.player = player;
        this.uuid = uuid;
    }

    public CRemovePlayerCarrySyncPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUUID(player);
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleRemovePlayerPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerCarry playerCarry){
        playerCarry.removeCarriedEntity(player);
    }

    public UUID getUuid() {
        return uuid;
    }
}
