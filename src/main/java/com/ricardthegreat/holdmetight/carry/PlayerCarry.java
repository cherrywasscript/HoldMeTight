package com.ricardthegreat.holdmetight.carry;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.network.CPlayerMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SPlayerMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.SPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.utils.constants.PlayerCarryConstants;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class PlayerCarry {
    /*
     * xymult,vertoffset, and left right move are named poorly so here is a bad explanation of them
     * xymult - moves the carried person towards and away from the carriers chest
     * leftrightmove - moves the carried person to the left or right relative to the way the carriers chest is facing
     * vertoffset - moves the carried person up and down
    */

    //for if they are being rendered in the size remote so that i can disable the nametag
    private boolean isMenuGraphic = false;

    //if they are being carried
    private boolean isCarried = false;

    //if they are carrying and if it is one of the custom poses
    private boolean isCarrying = false;
    private boolean isShoulderCarry = false;
    private boolean isCustomCarryPosition = false;
    private boolean headLink = false;

    //xy mult is moving towards and away from the body (smaller number closer to body)
    //the offsets used if it is hand carry
    private final int handRotationOffset = 110;
    private final double handxymult = 0.77;
    private final double handvertOffset = 0.65;
    private final double handleftRightMove = 0;

    //the offsets used if it is shoulder carry
    private final int shoulderRotationOffset = 90;
    private final double shoulderxymult = 0;
    private final double shouldervertOffset = 0.38;
    private final double shoulderleftRightMove = -0.3;

    //the offsets used for custom carry position
    private int rotationOffset = 0;
    private double xymult = 0;
    private double vertOffset = 0;
    private double leftRightMove = 0;


    //might replace this with each player having their own size utils class
    //could be awful? but idk im shit at efficiency
    private float minScale = 0;
    private float maxScale = 10000;
    private float defaultScale = 1;
    
    //if this is true it will sync the values that can change next tick and set to false
    private boolean shouldSync = false;

    //if this is true it will sync only the booleans because thats less data
    private boolean shouldSyncSimple = false;


    //checks every tick if the player should sync
    private void tick(Player player){
        if(shouldSync){
            shouldSync = false;
            sync();
        }
    }

    //sync the changeable values
    //currently custom carry positions as well as
    //if they are/aren't carrying
    private void sync(){
        if(((Player) (Object) this).level().isClientSide){
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToServer(new SPlayerMixinSyncPacket(rotationOffset, xymult, vertOffset, leftRightMove, 
                    isCarried, isCarrying, isShoulderCarry, isCustomCarryPosition)));
        }else{
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
            PacketHandler.sendToAllClients(new CPlayerMixinSyncPacket(rotationOffset, xymult, vertOffset, leftRightMove, 
                isCarried, isCarrying, isShoulderCarry, isCustomCarryPosition, ((Player) (Object) this).getUUID())));
        }
    }

    //the sync packets call this to update everything at once
    public void updateSyncables(int rotationOffset, double xymult, double vertOffset, double leftRightMove, boolean isCarried, boolean isCarrying, boolean isShoulderCarry, boolean isCustomCarryPosition){
        this.rotationOffset = rotationOffset;
        this.xymult = xymult;
        this.vertOffset = vertOffset;
        this.leftRightMove = leftRightMove;
        this.isCarried = isCarried;
        this.isCarrying = isCarrying;
        this.isCustomCarryPosition = isCustomCarryPosition;
        this.isShoulderCarry = isShoulderCarry;
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

    public boolean getShoulderCarry(){
        return isShoulderCarry;
    }

    public void setShoulderCarry(boolean isShoulderCarry){
        this.isShoulderCarry = isShoulderCarry;
        //disable custom carry if shoulder carry is enabled
        if(isShoulderCarry){
            isCustomCarryPosition = false;
        } 
    }

    public boolean getCustomCarry(){
        return isCustomCarryPosition;
    }

    public void setCustomCarry(boolean isCustomCarryPosition){
        this.isCustomCarryPosition = isCustomCarryPosition;
        //disable shoulder carry if custom carry is enabled
        if(isCustomCarryPosition){
            isShoulderCarry = false;
        } 
    }

    public boolean getHeadLink(){
        return headLink;
    }

    public void setHeadLink(boolean headLink){
        this.headLink = headLink;
    }


    //getters and setters for calculating the position of its rider
    public double getXYMult(){
        if(isCustomCarryPosition){
            return xymult;
        }else if(isShoulderCarry){
            return shoulderxymult;
        }else{
            return handxymult;
        }
    }

    //set custom carry xy position
    public void setXYMult(double xymult){
        this.xymult = xymult;
    }

    public int getRotationOffset() {
        if(isCustomCarryPosition){
            return rotationOffset;
        }else if(isShoulderCarry){
            return shoulderRotationOffset;
        }else{
            return handRotationOffset;
        }
    }

    //
    public int getCustomRotOffset(){
        return rotationOffset;
    }

    //set custom carry rotation position
    public void setRotationOffset(int rotationOffset){
        this.rotationOffset = rotationOffset;
    }

    public double getVertOffset() {
        if(isCustomCarryPosition){
            return vertOffset;
        }else if(isShoulderCarry){
            return shouldervertOffset;
        }else{
            return handvertOffset;
        }
    }

    //set custom carry vertical position
    public void setVertOffset(double vertOffset) {
        this.vertOffset = vertOffset;
    }


    public double getLeftRightMove() {
        if(isCustomCarryPosition){
            return leftRightMove;
        }else if(isShoulderCarry){
            return shoulderleftRightMove;
        }else{
            return handleftRightMove;
        }
    }

    public void setLeftRightMove(double leftRightMove) {
        this.leftRightMove = leftRightMove;
    }

    //setting if it should sync, it probably doesnt need a getter
    public boolean getShouldSync() {
        return shouldSync;
    }

    public void setShouldSync(boolean shouldSync) {
        this.shouldSync = shouldSync;
    }
 
    //getter and setter for if its a menu object
    public boolean getIsMenu() {
        return isMenuGraphic;
    }

    public void setMenu(boolean menu) {
        isMenuGraphic = menu;
    }

    public float getMinScale() {
        return minScale;
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public float getDefaultScale() {
        return defaultScale;
    }

    public void setDefaultScale(float defaultScale) {
        this.defaultScale = defaultScale;
    }


    

    public void copyFrom(PlayerCarry source){
        this.isCarried = source.isCarried;
        this.isCarrying = source.isCarrying;
        this.isShoulderCarry = source.isShoulderCarry;
        this.isCustomCarryPosition = source.isCustomCarryPosition;
        this.rotationOffset = source.rotationOffset;
        this.xymult = source.xymult;
        this.vertOffset = source.vertOffset;
        this.leftRightMove = source.leftRightMove;
    }

    public void saveNBTData(CompoundTag tag){
        tag.putBoolean(PlayerCarryConstants.CARRIED_NBT_TAG, isCarried);
        tag.putBoolean(PlayerCarryConstants.CARRYING_NBT_TAG, isCarrying);
        tag.putBoolean(PlayerCarryConstants.SHOULDER_CARRY_NBT_TAG, isShoulderCarry);
        tag.putBoolean(PlayerCarryConstants.CUSTOM_CARRY_NBT_TAG, isCustomCarryPosition);

        tag.putInt(PlayerCarryConstants.ROTATION_NBT_TAG, rotationOffset);
        tag.putDouble(PlayerCarryConstants.MULT_NBT_TAG, xymult);
        tag.putDouble(PlayerCarryConstants.VERT_NBT_TAG, vertOffset);
        tag.putDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG, leftRightMove);
    }

    public void loadNBTData(CompoundTag tag){
        isCarried = tag.getBoolean(PlayerCarryConstants.CARRIED_NBT_TAG);
        isCarrying = tag.getBoolean(PlayerCarryConstants.CARRYING_NBT_TAG);
        isShoulderCarry = tag.getBoolean(PlayerCarryConstants.SHOULDER_CARRY_NBT_TAG);
        isCustomCarryPosition = tag.getBoolean(PlayerCarryConstants.CUSTOM_CARRY_NBT_TAG);

        rotationOffset = tag.getInt(PlayerCarryConstants.ROTATION_NBT_TAG);
        xymult = tag.getDouble(PlayerCarryConstants.MULT_NBT_TAG);
        vertOffset = tag.getDouble(PlayerCarryConstants.VERT_NBT_TAG);
        leftRightMove = tag.getDouble(PlayerCarryConstants.LEFT_RIGHT_NBT_TAG);
    }
}
