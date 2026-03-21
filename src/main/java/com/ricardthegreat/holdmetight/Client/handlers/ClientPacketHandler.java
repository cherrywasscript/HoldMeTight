package com.ricardthegreat.holdmetight.client.handlers;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.clientbound.CAddCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CAddPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CEditCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemoveCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

    public static void handleCarryPacket(CPlayerCarrySyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                msg.playerSyncablesUpdate(playerCarry);
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

    @Deprecated
    public static void handleCarryPositionPacket(boolean carried, boolean carrying, int[] carryPos, UUID uuid, Supplier<NetworkEvent.Context> context){

    }

    public static void handleAddEntityPacket(CAddPlayerCarrySyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                msg.playerSyncablesUpdate(playerCarry);
            }
        }   
    }

    public static void handleRemovePlayerPacket(CRemovePlayerCarrySyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                msg.playerSyncablesUpdate(playerCarry);
            }
        }   
    }

    public static void handleAddCarryPosPacket(CAddCustomCarryPosPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                String name = msg.getCarryPos().posName;

                //ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).resolve().get();
                //curiosInventory.addTransientSlotModifier("custom", UUID.nameUUIDFromBytes(name.getBytes()), name, 1, AttributeModifier.Operation.ADDITION);

                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                playerCarry.addCustomCarryPos(msg.getCarryPos());
            }
        }
    }

    public static void handleRemoveCarryPosPacket(CRemoveCustomCarryPosPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                String name = msg.getCarryPos();

                //ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).resolve().get();
                //curiosInventory.addTransientSlotModifier("custom", UUID.nameUUIDFromBytes(name.getBytes()), name, -1, AttributeModifier.Operation.ADDITION);

                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                playerCarry.removeCustomCarryPos(name);
            }
        }
    }

    public static void handleEditCarryPosPacket(CEditCustomCarryPosPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                CarryPosition pos = msg.getCarryPos();
                int index = msg.getIndex();

                PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                playerCarry.editCustomCarryPos(pos, index);
            }
        }
    }

}
