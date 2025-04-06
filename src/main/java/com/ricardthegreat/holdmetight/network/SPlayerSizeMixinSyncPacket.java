package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.Client.handlers.ClientPacketHandler;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;
import com.ricardthegreat.holdmetight.utils.PlayerSizeExtension;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerSizeMixinSyncPacket {

    private final float maxScale;
    private final float minScale;
    private final float defaultScale;
    private final float currentScale;
    private final float targetScale;
    private final int remainingTicks;
    private final UUID uuid;

    public SPlayerSizeMixinSyncPacket(float maxScale, float minScale, float defaultScale, float currentScale, float targetScale, int remainingTicks, UUID uuid){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
        this.currentScale = currentScale;
        this.targetScale = targetScale;
        this.remainingTicks = remainingTicks;
        this.uuid = uuid;
    }

    public SPlayerSizeMixinSyncPacket(FriendlyByteBuf buffer){
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
        ServerPlayer player = context.get().getSender();
        
        ServerPlayer target = player.server.getPlayerList().getPlayer(uuid);

        if(target != null){
            LazyOptional<PlayerSize> optional = player.getCapability(PlayerSizeProvider.PLAYER_SIZE);

            if (optional.isPresent()) {
                PlayerSize orElse = optional.orElse(null);
                orElse.updateSyncables(maxScale, minScale, defaultScale, currentScale, targetScale, remainingTicks);
            }
        }
    }
}
