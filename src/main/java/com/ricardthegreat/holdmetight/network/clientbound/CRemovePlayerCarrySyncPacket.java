package com.ricardthegreat.holdmetight.network.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CRemovePlayerCarrySyncPacket {
    private final UUID entity;
    private final UUID uuid;

    /**
     * @param entity the entity to be removed from carry list
     * @param uuid the uuid of the player the entity is being removed from
     */
    public CRemovePlayerCarrySyncPacket(UUID entity, UUID uuid){
        this.entity = entity;
        this.uuid = uuid;
    }

    public CRemovePlayerCarrySyncPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUUID(entity);
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleRemovePlayerPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerCarry playerCarry){
        playerCarry.removeCarriedEntity(entity);
    }

    public UUID getUuid() {
        return uuid;
    }
}
