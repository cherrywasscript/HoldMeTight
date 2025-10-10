package com.ricardthegreat.holdmetight.carry;

import java.util.ArrayList;
import java.util.Arrays;

import com.ricardthegreat.holdmetight.network.CPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.utils.constants.PlayerCarryConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PlayerCarry {
    /*
     * xymult,vertoffset, and left right move are named poorly so here is a bad explanation of them
     * xymult - moves the carried person towards and away from the carriers chest
     * leftrightmove - moves the carried person to the left or right relative to the way the carriers chest is facing
     * vertoffset - moves the carried person up and down
    */

    //if they are being carried
    private boolean isCarried = false;

    //these are just here for initialising stuff
    private final CarryPosition hand = new CarryPosition("hand", 110, 0.77, 0.65, 0, false);
    private final CarryPosition shoulder = new CarryPosition("shoulder",90, 0, 0.38, -0.3, false);
    private CarryPosition custom = new CarryPosition("custom",0, 0, 0, 0, false);

    private final ArrayList<CarryPosition> defaultCarryPositions = new ArrayList<>(Arrays.asList(hand,shoulder));
    private ArrayList<CarryPosition> customCarryPositions = new ArrayList<>(Arrays.asList(custom));

    private ArrayList<ArrayList<CarryPosition>> allCarryPositions = new ArrayList<ArrayList<CarryPosition>>(Arrays.asList(defaultCarryPositions,customCarryPositions));
    
    private boolean isCarrying = false;
    private int[] currentCarryPos = new int[]{0,0};


    //if this is true it will sync the values that can change next tick and set to false
    private boolean shouldSync = false;

    //if this is true it will sync only the booleans because thats less data
    private boolean shouldSyncSimple = false;
    private boolean shouldSyncCustom = false;
    private boolean shouldSyncAll = false;
    
    //checks every tick if the player should sync
    public void tick(Player player){    
        if(shouldSync){
            if (shouldSyncSimple) {
                shouldSyncSimple = false;
                syncSimple(player);
            }else if (shouldSyncCustom) {
                shouldSyncCustom = false;
                syncCustom(player);
            }else if (shouldSyncAll) {
                shouldSyncAll = false;
                sync(player);
            }else{
                shouldSync = false;
            }
        }
    }

    //forms of syncing i want
    // simple - literally just changing if the player is carrying, being carried and positon they are carrying in
    // updated carrypos - sends the new values for custom carrypos
    // everything - does both
    private void sync(Player player){
        //at some point when i have the ability for multiple custom positions i'll need to iterate through custom carry positions
        // or maybe just send the list straight so i can iterate on the packet side
        if(player.level().isClientSide){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PacketHandler.sendToServer(new SPlayerCarrySyncPacket(isCarried, isCarrying, currentCarryPos, customCarryPositions.get(0))));
        }else{
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
            PacketHandler.sendToAllClients(new CPlayerCarrySyncPacket(isCarried, isCarrying, currentCarryPos, customCarryPositions.get(0), player.getUUID())));
        }
    }

    private void syncSimple(Player player){
        if(player.level().isClientSide){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToServer(new SPlayerCarrySimplePacket(isCarried, isCarrying, currentCarryPos,  player.getUUID())));
        }else{
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                PacketHandler.sendToAllClients(new CPlayerCarrySimplePacket(isCarried, isCarrying, currentCarryPos,  player.getUUID())));
        }
    }

    //TODO add custom carrying syncing
    private void syncCustom(Player player){
        if(player.level().isClientSide){
            //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                //PacketHandler.sendToServer();
        }else{
            //DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                //PacketHandler.sendToAllClients();
        }
    }

    //the sync packets call this to update everything at once
    public void updateAllSyncables(boolean isCarried, boolean isCarrying, int[] currentCarryPos, CarryPosition customCarry){
        updateSimpleSyncables(isCarried, isCarrying, currentCarryPos);
        updatePositionSyncables(customCarry);
    }

    public void updateSimpleSyncables(boolean isCarried, boolean isCarrying, int[] currentCarryPos){
        this.isCarried = isCarried;
        this.isCarrying = isCarrying;
        this.currentCarryPos = currentCarryPos;
    }

    public void updatePositionSyncables(CarryPosition customCarry){
        this.custom = customCarry;

        int tempSize = customCarryPositions.size();
        for(int i = 0; i < tempSize; i++){
            customCarryPositions.set(i, custom);
        }
    }


//    public CPlayerSizeMixinSyncPacket getSyncPacket(Player player){
//        return new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, currentScale, targetScale, remainingTicks, player.getUUID());
//    }


    //getter and setter for if it is carried
    public boolean getIsCarried() {
        return isCarried;
    }

    public void setCarried(boolean isCarried) {
        this.isCarried = isCarried;
    }

    //getters and setters for if it is carrying someone and how
    public boolean getIsCarrying() {
        return isCarrying;
    }

    public void setCarrying(boolean isCarrying) {
        this.isCarrying = isCarrying;
    }

    /**
     * 
     * @param default if its a custom or default position
     * @param pos which position it is in the list - invalid numbers default to the first
     */
    public void setCarryPosition(boolean custom, int pos){
        if (custom) {
            if (pos < 0 || pos > customCarryPositions.size()-1) {
                pos = 0;
            }
            currentCarryPos[0] = 1;
            currentCarryPos[1] = pos;
        }else{
            if (pos < 0 || pos > defaultCarryPositions.size()-1) {
                pos = 0;
            }
            currentCarryPos[0] = 0;
            currentCarryPos[1] = pos;
        }
    }

    //TODO remove or change
    public CarryPosition getCarryPosition(){
        return allCarryPositions.get(currentCarryPos[0]).get(currentCarryPos[1]);
    }

    public ArrayList<ArrayList<CarryPosition>> getAllCarryPositions(){
        return allCarryPositions;
    }

    public void addCustomCarryPos(CarryPosition custom){
        boolean added = false;
        customCarryPositions.set(0, custom);
        
        //TODO use this when i have more than 1 custom carry
        /* 
        for(int i = 0; i < customCarryPositions.size(); i++){
            if (customCarryPositions.get(i).posName == custom.posName) {
                customCarryPositions.set(i, custom);
                added = true;
            }
        }
        if (!added) {
            customCarryPositions.add(custom);
        }
        */
    }

    public void removeCustomCarryPos(String name){
        for(int i = 0; i < customCarryPositions.size(); i++){
            if (customCarryPositions.get(i).posName == custom.posName) {
                customCarryPositions.remove(i);
            }
        }
    }

    //setting if it should sync, it probably doesnt need a getter
    public boolean getShouldSyncSimple() {
        return shouldSync;
    }

    public void setShouldSyncSimple(boolean shouldSyncSimple) {
        this.shouldSyncSimple = shouldSyncSimple;
        this.shouldSync = shouldSyncSimple;
    }

    //setting if it should sync
    public boolean getShouldSyncCustom() {
        return shouldSyncCustom;
    }

    public void setShouldSyncCustom(boolean shouldSyncCustom) {
        this.shouldSyncCustom = shouldSyncCustom;
        this.shouldSync = shouldSyncCustom;
    }

    //setting if it should sync
    public boolean getShouldSyncAll() {
        return shouldSyncAll;
    }

    public void setShouldSyncAll(boolean shouldSyncAll) {
        this.shouldSyncAll = shouldSyncAll;
        this.shouldSync = shouldSyncAll;
    }
    



    public void copyFrom(PlayerCarry source){
        this.isCarried = source.isCarried;
        this.isCarrying = source.isCarrying;

        this.custom = source.custom;
        this.customCarryPositions = source.customCarryPositions;
        this.allCarryPositions = source.allCarryPositions;
    }


    //TODO update to iterate through all custom positions
    public void saveNBTData(CompoundTag tag){
        tag.putBoolean(PlayerCarryConstants.CARRIED_NBT_TAG, isCarried);
        tag.putBoolean(PlayerCarryConstants.CARRYING_NBT_TAG, isCarrying);

        CarryPosition carry = customCarryPositions.get(0);

        tag.putString(PlayerCarryConstants.POS_NAME_NBT_TAG, carry.posName);
        tag.putInt(PlayerCarryConstants.ROTATION_NBT_TAG, carry.RotationOffset);
        tag.putDouble(PlayerCarryConstants.MULT_NBT_TAG, carry.xymult);
        tag.putDouble(PlayerCarryConstants.VERT_NBT_TAG, carry.vertOffset);
        tag.putDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG, carry.leftRightMove);
        tag.putBoolean(PlayerCarryConstants.HEAD_LINK_NBT_TAG, carry.headLink);
    }

    public void loadNBTData(CompoundTag tag){
        isCarried = tag.getBoolean(PlayerCarryConstants.CARRIED_NBT_TAG);
        isCarrying = tag.getBoolean(PlayerCarryConstants.CARRYING_NBT_TAG);

        custom = new CarryPosition(
            tag.getString(PlayerCarryConstants.POS_NAME_NBT_TAG), tag.getInt(PlayerCarryConstants.ROTATION_NBT_TAG), tag.getDouble(PlayerCarryConstants.MULT_NBT_TAG), 
            tag.getDouble(PlayerCarryConstants.VERT_NBT_TAG), tag.getDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG), tag.getBoolean(PlayerCarryConstants.HEAD_LINK_NBT_TAG));

        customCarryPositions.set(0, custom);
    }

    public CPlayerCarrySyncPacket getSyncPacket(Player player){
        return new CPlayerCarrySyncPacket(isCarried, isCarrying, currentCarryPos, customCarryPositions.get(0), player.getUUID());
    }
}
