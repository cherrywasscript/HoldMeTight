package com.ricardthegreat.unnamedsizemod.network;

import java.util.UUID;
import java.util.function.Supplier;

import org.joml.Vector3f;

import com.ricardthegreat.unnamedsizemod.Client.handlers.ClientPacketHandler;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerPutDownPacket {
    private final UUID uuid;
    private final Vec3 pos;

    public SPlayerPutDownPacket(UUID uuid, Vec3 pos){
        this.uuid = uuid;
        this.pos = pos;
    }

    public SPlayerPutDownPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID(), new Vec3(buffer.readVector3f()));
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUUID(uuid);
        buffer.writeVector3f(pos.toVector3f());;
    }

    public UUID getUuid(){
        return this.uuid;
    }

    public Vec3 getVec3(){
        return this.pos;
    }

    public void handle(Supplier<NetworkEvent.Context> context){

        ServerPlayer player = context.get().getSender();
        ServerPlayer target = player.server.getPlayerList().getPlayer(uuid);

        if (target != null) {
            target.stopRiding();
            target.dismountTo(pos.x, pos.y, pos.z);
        }
    }
}
