package com.ricardthegreat.holdmetight.carry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jline.utils.Log;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.utils.constants.CarryPosConstants;
import com.ricardthegreat.holdmetight.utils.constants.PlayerCarryConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    private final CarryPosition mainHand = new CarryPosition("mainHand", 110, 0.77, 0.65, 0, false);
    private final CarryPosition offHand = new CarryPosition("offHand", 70, 0.77, 0.65, 0, false);
    private final CarryPosition leftshoulder = new CarryPosition("leftshoulder",90, 0, 0.38, -0.3, false);
    private final CarryPosition rightshoulder = new CarryPosition("rightshoulder",270, 0, 0.38, -0.3, false);

    // this feels clunky and wrong, im not entirely sure on a "right" way to do these though, maybe have them exist in a seperate static class? or possibly in a superclass to this, either way this works currently
    private final CarryPosition hotbarSlot0 = new CarryPosition("hotbarSlot7", 0, 0.15, 1, -0.15, false);
    private final CarryPosition hotbarSlot1 = new CarryPosition("hotbarSlot0", 0, 0.3, 1, 0, false);
    private final CarryPosition hotbarSlot2 = new CarryPosition("hotbarSlot1", 0, 0.2, 1, 0.15, false);
    private final CarryPosition hotbarSlot3 = new CarryPosition("hotbarSlot2", 0, 0.1, 1, 0.15, false);
    private final CarryPosition hotbarSlot4 = new CarryPosition("hotbarSlot3", 0, 0, 1, 0.15, false);
    private final CarryPosition hotbarSlot5 = new CarryPosition("hotbarSlot4", 0, -0.1, 1, 0.15, false);
    private final CarryPosition hotbarSlot6 = new CarryPosition("hotbarSlot5", 0, -0.2, 1, 0.15, false);
    private final CarryPosition hotbarSlot7 = new CarryPosition("hotbarSlot6", 0, -0.3, 1, 0, false);
    private final CarryPosition hotbarSlot8 = new CarryPosition("hotbarSlot8", 0, -0.15, 1, -0.15, false);

    private final CarryPosition torso = new CarryPosition("torso", 0, 0, 0.75, 0, false);

    private final ArrayList<CarryPosition> hotbarCarryPositions = new ArrayList<>(Arrays.asList(hotbarSlot0, hotbarSlot1, hotbarSlot2, hotbarSlot3, hotbarSlot4, hotbarSlot5, hotbarSlot6, hotbarSlot7, hotbarSlot8));

    private CarryPosition custom = new CarryPosition("default",0, 0, 0, 0, false);

    private ArrayList<CarryPosition> customCarryPositions = new ArrayList<>(Arrays.asList(custom));


    

    private ArrayList<CompoundTag> carriedEntities = new ArrayList<>();
    private boolean checkedCarriedEntities = true;

    private boolean turnWhileCarried = true;

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
        }else{
            if (!player.level().isClientSide && !checkedCarriedEntities) {
                HoldMeTight.LOGGER.debug("checking carried entities");
                for (CompoundTag tag : carriedEntities) {
                    UUID id = tag.getUUID(EntityStandinItem.ENTITY_UUID);
                    boolean shouldRemove = true;
                    for (Entity entity : player.getPassengers()) {
                        if (entity.getUUID().equals(id)) {
                            shouldRemove = false;
                        }
                    }

                    if (shouldRemove) {
                        HoldMeTight.LOGGER.debug("found entity " + id + " that is not carried removing");
                        removeCarriedEntity(id);

                        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                            PacketHandler.sendToAllClients(new CRemovePlayerCarrySyncPacket(id, player.getUUID())));
                    }
                }
                checkedCarriedEntities = true;
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
            PacketHandler.sendToAllClients(new CPlayerCarrySyncPacket(carriedEntities, customCarryPositions, player.getUUID())));
        }
    }

    private void addPlayerSync(Player player){

    }

    private void removePlayerSync(Player player){

    }

    //the sync packets call this to update everything at once
    public void updateAllSyncables(ArrayList<CompoundTag> carriedEntities, ArrayList<CarryPosition> customCarryPositions){
        this.carriedEntities = carriedEntities;
        this.customCarryPositions = customCarryPositions;
        /* 
        this.custom = customCarry;

        int tempSize = customCarryPositions.size();
        for(int i = 0; i < tempSize; i++){
            customCarryPositions.set(i, custom);
        }
            */
    }

    /**
     * add a tag to the list of players this person is holding, needs to call a sync after to ensure everything works
     * @param tag tag with the players id and the slot it is held in
     */
    public void addOrUpdateCarriedEntity(CompoundTag tag){
        for (int i = 0; i < carriedEntities.size(); i++){
            if (carriedEntities.get(i).getUUID(EntityStandinItem.ENTITY_UUID).equals(tag.getUUID(EntityStandinItem.ENTITY_UUID))) {
                carriedEntities.remove(i);
            }
        }
        
        carriedEntities.add(tag);

        checkedCarriedEntities = false;
    }

    /**
     * remove a tag from the list of players this person is holding, needs to call a sync after to ensure everything works
     * @param tag tag with the players id
     */
    public void removeCarriedEntity(UUID id){
        for (int i = 0; i < carriedEntities.size(); i++){
            if (carriedEntities.get(i).getUUID(EntityStandinItem.ENTITY_UUID).equals(id)) {
                carriedEntities.remove(i);
            }
        }

        checkedCarriedEntities = false;
    }

    //TODO remove or change
    public CarryPosition getCarryPosition(Entity entity, String hand){
        //System.out.println("getcarrypos: " + hand);
        switch (hand) {
            case CarryPosConstants.MAIN_HAND:
                return mainHand;

            case CarryPosConstants.OFF_HAND:
                return offHand;

            case CarryPosConstants.LEFT_SHOULDER:
                return leftshoulder;
            
            case CarryPosConstants.RIGHT_SHOULDER:
                return rightshoulder;
            
            case CarryPosConstants.CUSTOM:
                return customCarryPositions.get(0);
            
            default:

                for(int i = 0; i < customCarryPositions.size(); i++){
                    if (hand.equals(CarryPosConstants.CUSTOM+i)) {
                        return customCarryPositions.get(i);
                    }
                }

                for (CompoundTag tag : carriedEntities) {
                    if (tag.getUUID(EntityStandinItem.ENTITY_UUID).equals(entity.getUUID())) {
                        int invPos = tag.getInt(EntityStandinItem.INV_ID);
                        if (!Inventory.isHotbarSlot(invPos)) {
                            return torso;
                        }else{
                            if (invPos < 0 || hotbarCarryPositions.size() <= invPos) {
                                HoldMeTight.LOGGER.debug("invPos: " + invPos + " is a number that is either greater than 8 or less than 0 but still registerd as a hotbar slot, setting to custom as a failsafe");
                                return custom;
                            }
                            return hotbarCarryPositions.get(invPos);
                        }
                    }
                }  
                break;
        }

        //maybe want some form of error here not sure
        return custom;
    }

    public ArrayList<CarryPosition> getCustomCarryPositions(){
        return customCarryPositions;
    }

    public void addCustomCarryPos(CarryPosition custom){
        boolean added = false;
        for(int i = 0; i < customCarryPositions.size(); i++){
            if (customCarryPositions.get(i).posName.equals(custom.posName)) {
                customCarryPositions.set(i, custom);
                added = true;
            }
        }
        if (!added) {
            customCarryPositions.add(custom);
        }
    }

    public void editCustomCarryPos(CarryPosition edit, int pos){
        customCarryPositions.set(pos, edit);
    }

    public boolean removeCustomCarryPos(String name){
        boolean success = false;
        if (name.equals("default")) {
            return success;
        }
        for(int i = 0; i < customCarryPositions.size(); i++){
            if (customCarryPositions.get(i).posName.equals(name)) {
                customCarryPositions.remove(i);
                success = true;
            }
        }
        return success;
    }

    //setting if it should sync
    public boolean getShouldSyncAll() {
        return shouldSyncAll;
    }

    public void setShouldSyncAll(boolean shouldSyncAll) {
        this.shouldSyncAll = shouldSyncAll;
        this.shouldSync = shouldSyncAll;
    }
    
    public boolean getTurnWhileCarried(){
        return turnWhileCarried;
    }

    public void setTurnWhileCarried(boolean turnWhileCarried){
        this.turnWhileCarried = turnWhileCarried;
    }


    public void copyFrom(PlayerCarry source){
        this.custom = source.custom;
        this.customCarryPositions = source.customCarryPositions;
        this.carriedEntities = source.carriedEntities;

        this.checkedCarriedEntities = source.checkedCarriedEntities;
    }


    //TODO update to iterate through all custom positions
    public void saveNBTData(CompoundTag tag){
        tag.putInt(PlayerCarryConstants.CARRY_POS_LIST_SIZE, customCarryPositions.size());

        for (int i = 0; i < customCarryPositions.size(); i++) {
            tag.putString(PlayerCarryConstants.POS_NAME_NBT_TAG + i, customCarryPositions.get(i).posName);
            tag.putInt(PlayerCarryConstants.ROTATION_NBT_TAG + i, customCarryPositions.get(i).RotationOffset);
            tag.putDouble(PlayerCarryConstants.MULT_NBT_TAG + i, customCarryPositions.get(i).xymult);
            tag.putDouble(PlayerCarryConstants.VERT_NBT_TAG + i, customCarryPositions.get(i).vertOffset);
            tag.putDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG + i, customCarryPositions.get(i).leftRightMove);
            tag.putBoolean(PlayerCarryConstants.HEAD_LINK_NBT_TAG + i, customCarryPositions.get(i).headLink);
        }

        

        tag.putInt(PlayerCarryConstants.CARRIED_PLAYERS_LIST_SIZE, carriedEntities.size());

        for (int i = 0; i < carriedEntities.size(); i++) {
            tag.putUUID(EntityStandinItem.ENTITY_UUID + i, carriedEntities.get(i).getUUID(EntityStandinItem.ENTITY_UUID));
            tag.putInt(EntityStandinItem.INV_ID + i, carriedEntities.get(i).getInt(EntityStandinItem.INV_ID));
        }
    }

    public void loadNBTData(CompoundTag tag){
        try {
            ArrayList<CarryPosition> temp = new ArrayList<>();
            for (int i = 0; i < tag.getInt(PlayerCarryConstants.CARRY_POS_LIST_SIZE); i++) {
                CarryPosition tmp = new CarryPosition(
                tag.getString(PlayerCarryConstants.POS_NAME_NBT_TAG+i), tag.getInt(PlayerCarryConstants.ROTATION_NBT_TAG+i), tag.getDouble(PlayerCarryConstants.MULT_NBT_TAG+i), 
                tag.getDouble(PlayerCarryConstants.VERT_NBT_TAG+i), tag.getDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG+i), tag.getBoolean(PlayerCarryConstants.HEAD_LINK_NBT_TAG+i));

                temp.add(tmp);
            }
            customCarryPositions = temp;


            for (int i = 0; i < tag.getInt(PlayerCarryConstants.CARRIED_PLAYERS_LIST_SIZE); i++) {
                CompoundTag tmp = new CompoundTag();
                tmp.putUUID(EntityStandinItem.ENTITY_UUID, tag.getUUID(EntityStandinItem.ENTITY_UUID+i));
                tmp.putInt(EntityStandinItem.INV_ID, tag.getInt(EntityStandinItem.INV_ID+i));

                carriedEntities.add(tmp);
            }

            checkedCarriedEntities = false;
        } catch (Exception e) {
            HoldMeTight.LOGGER.error(e.getMessage());
        }
    }

    public CPlayerCarrySyncPacket getClientSyncPacket(Player player){
        return new CPlayerCarrySyncPacket(carriedEntities, customCarryPositions, player.getUUID());
    }

    public SPlayerCarrySyncPacket getServerSyncPacket(){
        return new SPlayerCarrySyncPacket(carriedEntities, customCarryPositions);
    }

    public void fullReset(){
        this.copyFrom(new PlayerCarry());
    }
}
