package com.ricardthegreat.holdmetight.capabilities.preferences;

import java.util.ArrayList;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.preferences.CPlayerPreferencesSyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.size.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.carry.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.preferences.SPlayerPreferencesSyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.size.SPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.utils.constants.PlayerCarryConstants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PlayerPreferences {

    //Size related preferences
    private float maxScale = Float.MAX_VALUE;
    private float minScale = 0;
    private float defaultScale = 1;
    private boolean othersCanChangeYourSize = true;
    private boolean youCanChangeYourSize = true;

    //Carry related preferences
    private boolean inventoryCanBeAccessed = true;
    private boolean trapCarriedPlayer = true;
    private boolean canBeTrappedWhileCarried = true;
    private boolean canBePickedup = true;
    private boolean canPickupOthers = true;

    private boolean shouldSync = false;

    public void tick(Player player){
        if (shouldSync) {
            sync(player);
        }
    }

    private void sync(Player player){
        shouldSync = false;
        HoldMeTight.LOGGER.debug("player pref sync");
        if (player.level().isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToServer(getServerSyncPacket()));
        }else {
            if (!player.getServer().isDedicatedServer()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToAllClients(getClientSyncPacket(player)));
            }else{
                DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                PacketHandler.sendToAllClients(getClientSyncPacket(player)));
            }
        }
    }

    public void updateShouldSync(){
        this.shouldSync = true;
    }

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

    public boolean getOthersCanChange(){
        return othersCanChangeYourSize;
    }

    public void setOthersCanChange(boolean othersCanChangeYourSize){
        this.othersCanChangeYourSize = othersCanChangeYourSize;
    }

    public boolean getSelfCanChange(){
        return youCanChangeYourSize;
    }

    public void setSelfCanChange(boolean youCanChangeYourSize){
        this.youCanChangeYourSize = youCanChangeYourSize;
    }

    public boolean getInventoryCanBeAccessed() {
        return inventoryCanBeAccessed;
    }

    public void setInventoryCanBeAccessed(boolean inventoryCanBeAccessed) {
        this.inventoryCanBeAccessed = inventoryCanBeAccessed;
    }

    public boolean getTrapCarriedPlayer() {
        return trapCarriedPlayer;
    }

    public void setTrapCarriedPlayer(boolean trapCarriedPlayer) {
        this.trapCarriedPlayer = trapCarriedPlayer;
    }

    public boolean getCanBeTrappedWhileCarried() {
        return canBeTrappedWhileCarried;
    }

    public void setCanBeTrappedWhileCarried(boolean canBeTrappedWhileCarried) {
        this.canBeTrappedWhileCarried = canBeTrappedWhileCarried;
    }

    public boolean getCanBePickedup() {
        return canBePickedup;
    }

    public void setCanBePickedup(boolean canBePickedup) {
        this.canBePickedup = canBePickedup;
    }

    public boolean getCanPickupOthers() {
        return canPickupOthers;
    }

    public void setCanPickupOthers(boolean canPickupOthers) {
        this.canPickupOthers = canPickupOthers;
    }

    public void saveNBTData(CompoundTag tag){
        tag.putFloat("maxScale", maxScale);
        tag.putFloat("minScale", minScale);
        tag.putFloat("defaultScale", defaultScale);
        tag.putBoolean("othersCanChangeYourSize", othersCanChangeYourSize);
        tag.putBoolean("youCanChangeYourSize", youCanChangeYourSize);

        tag.putBoolean("inventoryCanBeAccessed", inventoryCanBeAccessed);
        tag.putBoolean("trapCarriedPlayer", trapCarriedPlayer);
        tag.putBoolean("canBeTrappedWhileCarried", canBeTrappedWhileCarried);
        tag.putBoolean("canBePickedup", canBePickedup);
        tag.putBoolean("canPickupOthers", canPickupOthers);
    }

    public void loadNBTData(CompoundTag tag){
        maxScale = tag.getFloat("maxScale");
        minScale = tag.getFloat("minScale");
        defaultScale = tag.getFloat("defaultScale");
        othersCanChangeYourSize = tag.getBoolean("othersCanChangeYourSize");
        youCanChangeYourSize = tag.getBoolean("youCanChangeYourSize");

        inventoryCanBeAccessed = tag.getBoolean("inventoryCanBeAccessed");
        trapCarriedPlayer = tag.getBoolean("trapCarriedPlayer");
        canBeTrappedWhileCarried = tag.getBoolean("canBeTrappedWhileCarried");
        canBePickedup = tag.getBoolean("canBePickedup");
        canPickupOthers = tag.getBoolean("canPickupOthers");
    }

    public void copyFrom(PlayerPreferences source){
        this.maxScale = source.maxScale;
        this.minScale = source.minScale;
        this.defaultScale = source.defaultScale;
        this.othersCanChangeYourSize = source.othersCanChangeYourSize;
        this.youCanChangeYourSize = source.youCanChangeYourSize;

        this.inventoryCanBeAccessed = source.inventoryCanBeAccessed;
        this.trapCarriedPlayer = source.trapCarriedPlayer;
        this.canBeTrappedWhileCarried = source.canBeTrappedWhileCarried;
        this.canBePickedup = source.canBePickedup;
        this.canPickupOthers = source.canPickupOthers;
    }

    public void updateAllSyncables(
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

    //TODO consider changing these packets to just send nbts?
    //im not certain what is the best bc im sure that sending an nbt would be more data overall but also it would make the code far cleaner
    //than sending every single variable individually
    public CPlayerPreferencesSyncPacket getClientSyncPacket(Player player){
        return new CPlayerPreferencesSyncPacket(
            maxScale, minScale, defaultScale, othersCanChangeYourSize, youCanChangeYourSize, 
            inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried, canBePickedup, canPickupOthers, 
            player.getUUID());
    }

    public SPlayerPreferencesSyncPacket getServerSyncPacket(){
        return new SPlayerPreferencesSyncPacket(
            maxScale, minScale, defaultScale, othersCanChangeYourSize, youCanChangeYourSize, 
            inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried, canBePickedup, canPickupOthers);
    }
}
