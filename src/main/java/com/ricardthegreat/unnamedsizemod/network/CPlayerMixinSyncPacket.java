package com.ricardthegreat.unnamedsizemod.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.unnamedsizemod.Client.handlers.ClientPacketHandler;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerMixinSyncPacket {

    private final int rotation;
    private final double distance;
    private final double height;
    private final double leftRight;
    private final boolean carried;
    private final boolean carrying;
    private final boolean shoulder;
    private final boolean custom;
    private final UUID uuid;

    public CPlayerMixinSyncPacket(int rotation, double distance, double height, double leftRight, boolean carried, boolean carrying, boolean shoulder, boolean custom, UUID uuid){
        this.rotation = rotation;
        this.distance = distance;
        this.height = height;
        this.leftRight = leftRight;
        this.carried = carried;
        this.carrying = carrying;
        this.shoulder = shoulder;
        this.custom = custom;
        this.uuid = uuid;
    }

    public CPlayerMixinSyncPacket(FriendlyByteBuf buffer){
        this(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(rotation);
        buffer.writeDouble(distance);
        buffer.writeDouble(height);
        buffer.writeDouble(leftRight);
        buffer.writeBoolean(carried);
        buffer.writeBoolean(carrying);
        buffer.writeBoolean(shoulder);
        buffer.writeBoolean(custom);
        buffer.writeUUID(uuid);

    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCarryPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerCarryExtension playerCarry){
        playerCarry.updateSyncables(rotation, distance, height, leftRight, 
        carried, carrying, shoulder, custom);
    }

    public UUID getUuid() {
        return uuid;
    }
}
