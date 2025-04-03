package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.Client.handlers.ClientPacketHandler;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;
import com.ricardthegreat.holdmetight.utils.PlayerSizeExtension;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerSizeMixinSyncPacket {

    private final float maxScale;
    private final float minScale;
    private final float defaultScale;
    private final float currentScale;
    private final float targetScale;
    private final int remainingTicks;
    private final UUID uuid;

    public CPlayerSizeMixinSyncPacket(float maxScale, float minScale, float defaultScale, float currentScale, float targetScale, int remainingTicks, UUID uuid){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
        this.currentScale = currentScale;
        this.targetScale = targetScale;
        this.remainingTicks = remainingTicks;
        this.uuid = uuid;
    }

    public CPlayerSizeMixinSyncPacket(FriendlyByteBuf buffer){
        this(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(maxScale);
        buffer.writeFloat(minScale);
        buffer.writeFloat(defaultScale);
        buffer.writeFloat(currentScale);
        buffer.writeFloat(targetScale);
        buffer.writeInt(remainingTicks);
        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleSizePacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerSizeExtension playerSize){
        playerSize.updateSyncables(maxScale, minScale, defaultScale, currentScale, 
        targetScale, remainingTicks);
    }

    public UUID getUuid() {
        return uuid;
    }
}
