package com.ricardthegreat.holdmetight.client.handlers;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSize;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.network.clientbound.CThrowEntityPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CThrowPlayerPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CAddPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.preferences.CPlayerPreferencesSyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.size.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.carrypositions.CAddCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.carrypositions.CEditCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.carrypositions.CRemoveCustomCarryPosPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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

    public static void handleThrowPlayerPacket(CThrowPlayerPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player thrown = level.getPlayerByUUID(msg.getThrownId());
            if(thrown != null) {
                thrown.stopRiding();
                thrown.setDeltaMovement(new Vec3(msg.getMovement())); 
                thrown.hurtMarked = true;
            }
        }
    }

    public static void handleThrowEntityPacket(CThrowEntityPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Entity thrown = level.getEntity(msg.getThrownId());
            if(thrown != null) {
                thrown.stopRiding();
                thrown.setDeltaMovement(new Vec3(msg.getMovement())); 
                thrown.hurtMarked = true;
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

    //TODO remove
    public static void handleSizePacket(CPlayerSizeMixinSyncPacket msg, Supplier<NetworkEvent.Context> context){
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

    public static void handlePreferencesPacket(CPlayerPreferencesSyncPacket msg, Supplier<NetworkEvent.Context> context){
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null){
            Player player = level.getPlayerByUUID(msg.getUuid());
            if(player != null) {
                PlayerPreferences playerPreferences = PlayerPreferencesProvider.getPlayerPreferencesCapability(player);
                msg.playerSyncablesUpdate(playerPreferences);
            }
        }   
    }

}
