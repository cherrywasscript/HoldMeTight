package com.ricardthegreat.unnamedsizemod.Client.handlers;

import java.util.function.Supplier;

import com.ricardthegreat.unnamedsizemod.network.CPlayerCarryPositionPacket;
import com.ricardthegreat.unnamedsizemod.network.CPlayerDismountPlayerPacket;
import com.ricardthegreat.unnamedsizemod.network.CPlayerMixinSyncPacket;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.player.Player;

public class ClientPacketHandler {

    public static void handleDismountPacket(CPlayerDismountPlayerPacket msg, Supplier<NetworkEvent.Context> context) {
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                player.stopRiding();
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
