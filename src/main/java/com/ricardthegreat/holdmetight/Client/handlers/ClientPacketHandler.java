package com.ricardthegreat.holdmetight.Client.handlers;

import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.network.CPlayerCarryPositionPacket;
import com.ricardthegreat.holdmetight.network.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.network.CPlayerMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;
import com.ricardthegreat.holdmetight.utils.PlayerSizeExtension;

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

    public static void handleCarryPacket(CPlayerMixinSyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerCarryExtension playerCarry = (PlayerCarryExtension) player;
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

    public static void handleCarryPositionPacket(CPlayerCarryPositionPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;

        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerCarryExtension playerCarry = (PlayerCarryExtension) player;
                boolean position = msg.getPosition();
                switch (msg.getTarget()) {
                    case 0:
                        playerCarry.setCarried(position);
                        break;
        
                    case 1:
                        playerCarry.setCarrying(position);
                        break;
        
                    case 2:
                        playerCarry.setShoulderCarry(position);
                        break;
        
                    case 3:
                        playerCarry.setCustomCarry(position);
                        break;
        
                    default:
                        break;
                } 
            }
        }
    }

}
