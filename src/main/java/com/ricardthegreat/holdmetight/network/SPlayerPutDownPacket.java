package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
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
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
            PlayerCarry targetCarry = PlayerCarryProvider.getPlayerCarryCapability(target);

            playerCarry.setCarrying(false);
            playerCarry.setShouldSyncSimple(true);
            targetCarry.setCarried(false);
            targetCarry.setShouldSyncSimple(true);

            //log if it isnt 1 for error checking
            //wont always be an error as if theyre on the side of a block or a non full block but it should help
            if (pos.y%1 != 0) {
                HoldMeTight.LOGGER.info("SPlayerPutDownPacket.java line 46: " + pos);
            }

            System.out.println(pos);

            target.dismountTo(pos.x, pos.y, pos.z);
        }
    }
}
