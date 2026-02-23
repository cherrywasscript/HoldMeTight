package com.ricardthegreat.holdmetight.carry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.serverbound.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.utils.constants.PlayerCarryConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
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

    //these are just here for initialising stuff
    private final CarryPosition hand = new CarryPosition("hand", 110, 0.77, 0.65, 0, false);
    private final CarryPosition shoulder = new CarryPosition("shoulder",90, 0, 0.38, -0.3, false);
    private CarryPosition custom = new CarryPosition("custom",0, 0, 0, 0, false);

    private final ArrayList<CarryPosition> defaultCarryPositions = new ArrayList<>(Arrays.asList(hand,shoulder));
    private ArrayList<CarryPosition> customCarryPositions = new ArrayList<>(Arrays.asList(custom));

    private ArrayList<ArrayList<CarryPosition>> allCarryPositions = new ArrayList<ArrayList<CarryPosition>>(Arrays.asList(defaultCarryPositions,customCarryPositions));
    

    private ArrayList<CompoundTag> carriedPlayers = new ArrayList<>();


    //if this is true it will sync the values that can change next tick and set to false
    private boolean shouldSync = false;

    //if this is true it will sync only the booleans because thats less data
    private boolean shouldSyncAll = false;
    
    //checks every tick if the player should sync
    public void tick(Player player){    
        if(shouldSync){
            if (shouldSyncAll) {
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
        //should only be updated server side so should only be called when on server
        if(!player.level().isClientSide){
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
            PacketHandler.sendToAllClients(new CPlayerCarrySyncPacket(carriedPlayers, customCarryPositions.get(0), player.getUUID())));
        }
    }

    private void addPlayerSync(Player player){

    }

    private void removePlayerSync(Player player){

    }

    //the sync packets call this to update everything at once
    public void updateAllSyncables(ArrayList<CompoundTag> carriedPlayers, CarryPosition customCarry){
        this.carriedPlayers = carriedPlayers;

        //TODO choose if i want to allow multiple custom carry spots or just one, leaning towards just one
        this.custom = customCarry;

        int tempSize = customCarryPositions.size();
        for(int i = 0; i < tempSize; i++){
            customCarryPositions.set(i, custom);
        }
    }

    /**
     * add a tag to the list of players this person is holding, needs to call a sync after to ensure everything works
     * @param tag tag with the players id and the slot it is held in
     */
    public void addOrUpdateCarriedPlayer(CompoundTag tag){
        for (int i = 0; i < carriedPlayers.size(); i++){
            if (carriedPlayers.get(i).getUUID(PlayerStandinItem.PLAYER_UUID).equals(tag.getUUID(PlayerStandinItem.PLAYER_UUID))) {
                carriedPlayers.remove(i);
                carriedPlayers.add(tag);
            }
        }
    }

    /**
     * remove a tag from the list of players this person is holding, needs to call a sync after to ensure everything works
     * @param tag tag with the players id
     */
    public void removeCarriedPlayer(UUID id){
        for (int i = 0; i < carriedPlayers.size(); i++){
            if (carriedPlayers.get(i).getUUID(PlayerStandinItem.PLAYER_UUID).equals(id)) {
                carriedPlayers.remove(i);
            }
        }
    }

    //TODO remove or change
    public CarryPosition getCarryPosition(Entity entity, boolean selected){
        if (selected) {
            return hand;
        }else{
            for (CompoundTag tag : carriedPlayers) {
                if (tag.getUUID(PlayerStandinItem.PLAYER_UUID).equals(entity.getUUID())) {
                    int invPos = tag.getInt(PlayerStandinItem.INV_ID);
                    if (!Inventory.isHotbarSlot(invPos)) {
                        //TODO make 9 carry positions to simulate having them on belt
                        return custom;
                    }else if (Inventory.SLOT_OFFHAND == invPos) {
                        //TODO make a carry pos that is the same as hand but mirrored for offhand
                        return shoulder;
                    }else{
                        return shoulder;
                    }
                }
            }  
        }

        //maybe want some form of error here not sure
        return custom;
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

    //setting if it should sync
    public boolean getShouldSyncAll() {
        return shouldSyncAll;
    }

    public void setShouldSyncAll(boolean shouldSyncAll) {
        this.shouldSyncAll = shouldSyncAll;
        this.shouldSync = shouldSyncAll;
    }
    



    public void copyFrom(PlayerCarry source){
        this.custom = source.custom;
        this.customCarryPositions = source.customCarryPositions;
        this.allCarryPositions = source.allCarryPositions;
        this.carriedPlayers = source.carriedPlayers;
    }


    //TODO update to iterate through all custom positions
    public void saveNBTData(CompoundTag tag){
        CarryPosition carry = customCarryPositions.get(0);

        tag.putString(PlayerCarryConstants.POS_NAME_NBT_TAG, carry.posName);
        tag.putInt(PlayerCarryConstants.ROTATION_NBT_TAG, carry.RotationOffset);
        tag.putDouble(PlayerCarryConstants.MULT_NBT_TAG, carry.xymult);
        tag.putDouble(PlayerCarryConstants.VERT_NBT_TAG, carry.vertOffset);
        tag.putDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG, carry.leftRightMove);
        tag.putBoolean(PlayerCarryConstants.HEAD_LINK_NBT_TAG, carry.headLink);

        tag.putInt(PlayerCarryConstants.CARRIED_PLAYERS_LIST_SIZE, carriedPlayers.size());

        for (int i = 0; i < carriedPlayers.size(); i++) {
            tag.putUUID(PlayerStandinItem.PLAYER_UUID + i, carriedPlayers.get(i).getUUID(PlayerStandinItem.PLAYER_UUID));
            tag.putInt(PlayerStandinItem.INV_ID + i, carriedPlayers.get(i).getInt(PlayerStandinItem.INV_ID));
        }
    }

    public void loadNBTData(CompoundTag tag){
        custom = new CarryPosition(
            tag.getString(PlayerCarryConstants.POS_NAME_NBT_TAG), tag.getInt(PlayerCarryConstants.ROTATION_NBT_TAG), tag.getDouble(PlayerCarryConstants.MULT_NBT_TAG), 
            tag.getDouble(PlayerCarryConstants.VERT_NBT_TAG), tag.getDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG), tag.getBoolean(PlayerCarryConstants.HEAD_LINK_NBT_TAG));

        customCarryPositions.set(0, custom);

        for (int i = 0; i < tag.getInt(PlayerCarryConstants.CARRIED_PLAYERS_LIST_SIZE); i++) {
            CompoundTag tmp = new CompoundTag();
            tmp.putUUID(PlayerStandinItem.PLAYER_UUID, tag.getUUID(PlayerStandinItem.PLAYER_UUID+i));
            tmp.putInt(PlayerStandinItem.INV_ID, tag.getInt(PlayerStandinItem.INV_ID+i));

            carriedPlayers.add(tmp);
        }
    }

    public CPlayerCarrySyncPacket getSyncPacket(Player player){
        return new CPlayerCarrySyncPacket(carriedPlayers, customCarryPositions.get(0), player.getUUID());
    }
}
