package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class SEntityPutDownPacket {
    private final UUID uuid;
    private final Vec3 pos;

    public SEntityPutDownPacket(UUID uuid, Vec3 pos){
        this.uuid = uuid;
        this.pos = pos;
    }

    public SEntityPutDownPacket(FriendlyByteBuf buffer){
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
        Entity target = null;

        for(Entity entity : player.getPassengers()){
            //need some check where if none of them are correct then do a failsafe
            if (entity.getUUID().compareTo(uuid) == 0) {
                target = entity;
            }
        }

        if (target != null) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            playerCarry.setCarrying(false);
            playerCarry.setShouldSyncSimple(true);

            target.stopRiding();

            target.dismountTo(pos.x, pos.y, pos.z);
        }
    }
}
