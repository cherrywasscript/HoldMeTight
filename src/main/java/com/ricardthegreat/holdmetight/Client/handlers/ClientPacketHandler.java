package com.ricardthegreat.holdmetight.Client.handlers;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.CPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.network.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.player.Player;

public class ClientPacketHandler {

    public static void handleDismountPacket(CPlayerDismountPlayerPacket msg, Supplier<NetworkEvent.Context> context) {
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                player.stopRiding();
                HoldMeTight.LOGGER.info("ClientPacketHandler 23: " + player.position());
            }
        }   
    }

    public static void handleCarryPacket(CPlayerCarrySyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerSizeCapability(player);
                msg.playerSyncablesUpdate(playerCarry);
                //playerCarry.setRotationOffset(msg.getRotation());
                //playerCarry.setXYMult(msg.getDistance());
                //playerCarry.setVertOffset(msg.getHeight());
            }
        }   
    }

    public static void handleSizePacket(CPlayerSizeMixinSyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                LazyOptional<PlayerSize> optional = player.getCapability(PlayerSizeProvider.PLAYER_SIZE);
                PlayerSize orElse = optional.orElse(new PlayerSize());
                msg.playerSyncablesUpdate(orElse);
            }
        }  
    }

    public static void handleCarryPositionPacket(boolean carried, boolean carrying, int[] carryPos, UUID uuid, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;

        if(level != null){
            Player player = level.getPlayerByUUID(uuid);
            if(player != null) {
                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerSizeCapability(player);

                playerCarry.updateSimpleSyncables(carried, carrying, carryPos);
            }
        }
    }

}
