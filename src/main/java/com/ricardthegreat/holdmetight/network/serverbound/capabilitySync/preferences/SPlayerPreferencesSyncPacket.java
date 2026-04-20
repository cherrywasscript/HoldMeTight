package com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.preferences;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.preferences.CPlayerPreferencesSyncPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerPreferencesSyncPacket {

    private final float maxScale;
    private final float minScale;
    private final float defaultScale;
    private final boolean othersCanChangeYourSize;
    private final boolean youCanChangeYourSize;

    private final boolean inventoryCanBeAccessed;
    private final boolean trapCarriedPlayer;
    private final boolean canBeTrappedWhileCarried;
    private final boolean canBePickedup;
    private final boolean canPickupOthers;

    public SPlayerPreferencesSyncPacket(
        float maxScale, float minScale, float defaultScale, boolean othersCanChangeYourSize, boolean youCanChangeYourSize, 
        boolean inventoryCanBeAccessed, boolean trapCarriedPlayer, boolean canBeTrappedWhileCarried, boolean canBePickedup, boolean canPickupOthers
        ){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
        this.othersCanChangeYourSize = othersCanChangeYourSize;
        this.youCanChangeYourSize = youCanChangeYourSize;

        this.inventoryCanBeAccessed = inventoryCanBeAccessed;
        this.trapCarriedPlayer = trapCarriedPlayer;
        this.canBeTrappedWhileCarried = canBeTrappedWhileCarried;
        this.canBePickedup = canBePickedup;
        this.canPickupOthers = canPickupOthers;
    }
    
    public SPlayerPreferencesSyncPacket(FriendlyByteBuf buffer){
        this.maxScale = buffer.readFloat();
        this.minScale = buffer.readFloat();
        this.defaultScale = buffer.readFloat();
        this.othersCanChangeYourSize = buffer.readBoolean();
        this.youCanChangeYourSize = buffer.readBoolean();

        this.inventoryCanBeAccessed = buffer.readBoolean();
        this.trapCarriedPlayer = buffer.readBoolean();
        this.canBeTrappedWhileCarried = buffer.readBoolean();
        this.canBePickedup = buffer.readBoolean();
        this.canPickupOthers = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(maxScale);
        buffer.writeFloat(minScale);
        buffer.writeFloat(defaultScale);
        buffer.writeBoolean(othersCanChangeYourSize);
        buffer.writeBoolean(youCanChangeYourSize);

        buffer.writeBoolean(inventoryCanBeAccessed);
        buffer.writeBoolean(trapCarriedPlayer);
        buffer.writeBoolean(canBeTrappedWhileCarried);
        buffer.writeBoolean(canBePickedup);
        buffer.writeBoolean(canPickupOthers);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        ServerPlayer player = context.get().getSender();

        PlayerPreferences preferences = PlayerPreferencesProvider.getPlayerPreferencesCapability(player);
        if(preferences != null){
            preferences.updateAllSyncables(
                maxScale, minScale, defaultScale, othersCanChangeYourSize, youCanChangeYourSize, 
                inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried, canBePickedup, canPickupOthers);

            PacketHandler.sendToAllClients(
                new CPlayerPreferencesSyncPacket(
                    maxScale, minScale, defaultScale, othersCanChangeYourSize, youCanChangeYourSize, 
                    inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried, canBePickedup, canPickupOthers, 
                    player.getUUID()));
        }
    }
}
