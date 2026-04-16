package com.ricardthegreat.holdmetight.capabilities.preferences;

import java.util.ArrayList;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.preferences.CPlayerPreferencesSyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.carry.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.preferences.SPlayerPreferencesSyncPacket;
import com.ricardthegreat.holdmetight.utils.constants.PlayerCarryConstants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PlayerPreferences {

    private float maxScale = Float.MAX_VALUE;
    private float minScale = 0;
    private float defaultScale = 1;

    private boolean inventoryCanBeAccessed = true;
    private boolean trapCarriedPlayer = true;
    private boolean canBeTrappedWhileCarried = true;

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(Float maxScale) {
        if (maxScale < minScale) {
            maxScale = minScale;
        }
        if (maxScale < defaultScale) {
            defaultScale = maxScale;
        }
        this.maxScale = maxScale;
    }

    public float getMinScale() {
        return minScale;
    }

    public void setMinScale(Float minScale) {
        if (minScale > maxScale) {
            minScale = maxScale;
        }
        if (minScale > defaultScale) {
            defaultScale = minScale;
        }
        this.minScale = minScale;
    }

    public float getDefaultScale(){
        return defaultScale;
    }

    public void setDefaultScale(Float defaultScale){
        if (defaultScale > maxScale) {
            this.defaultScale = maxScale;
        }else if (defaultScale < minScale) {
            this.defaultScale = minScale;
        }else{
            this.defaultScale = defaultScale;
        }  
    }

    public void saveNBTData(CompoundTag tag){
        tag.putFloat("maxScale", maxScale);
        tag.putFloat("minScale", minScale);
        tag.putFloat("defaultScale", defaultScale);

        tag.putBoolean("inventoryCanBeAccessed", inventoryCanBeAccessed);
        tag.putBoolean("trapCarriedPlayer", trapCarriedPlayer);
        tag.putBoolean("canBeTrappedWhileCarried", canBeTrappedWhileCarried);
    }

    public void loadNBTData(CompoundTag tag){
        maxScale = tag.getFloat("maxScale");
        minScale = tag.getFloat("minScale");
        defaultScale = tag.getFloat("defaultScale");

        inventoryCanBeAccessed = tag.getBoolean("inventoryCanBeAccessed");
        trapCarriedPlayer = tag.getBoolean("trapCarriedPlayer");
        canBeTrappedWhileCarried = tag.getBoolean("canBeTrappedWhileCarried");
    }

    public void copyFrom(PlayerPreferences source){
        this.maxScale = source.maxScale;
        this.minScale = source.minScale;
        this.defaultScale = source.defaultScale;

        this.inventoryCanBeAccessed = source.inventoryCanBeAccessed;
        this.trapCarriedPlayer = source.trapCarriedPlayer;
        this.canBeTrappedWhileCarried = source.canBeTrappedWhileCarried;
    }

    public void updateAllSyncables(float maxScale, float minScale, float defaultScale, boolean inventoryCanBeAccessed, boolean trapCarriedPlayer, boolean canBeTrappedWhileCarried){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
        this.inventoryCanBeAccessed = inventoryCanBeAccessed;
        this.trapCarriedPlayer = trapCarriedPlayer;
        this.canBeTrappedWhileCarried = canBeTrappedWhileCarried;
    }

    public CPlayerPreferencesSyncPacket getClientSyncPacket(Player player){
        return new CPlayerPreferencesSyncPacket(maxScale, minScale, defaultScale, inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried, player.getUUID());
    }

    public SPlayerPreferencesSyncPacket getServerSyncPacket(){
        return new SPlayerPreferencesSyncPacket(maxScale, minScale, defaultScale, inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried);
    }
}
