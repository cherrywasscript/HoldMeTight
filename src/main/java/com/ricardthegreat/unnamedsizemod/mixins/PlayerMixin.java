package com.ricardthegreat.unnamedsizemod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.unnamedsizemod.network.CPlayerMixinSyncPacket;
import com.ricardthegreat.unnamedsizemod.network.PacketHandler;
import com.ricardthegreat.unnamedsizemod.network.SPlayerMixinSyncPacket;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryUtils;
import com.ricardthegreat.unnamedsizemod.utils.PlayerRenderExtension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerCarryExtension, PlayerRenderExtension{

    //for if they are being rendered in the size remote so that i can disable the nametag
    private boolean isMenuGraphic = false;

    //if they are being carried
    private boolean isCarried = false;

    //if they are carrying and if it is one of the custom poses
    private boolean isCarrying = false;
    private boolean isShoulderCarry = false;
    private boolean isCustomCarryPosition = false;

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

    
    //if this is true it will sync the values that can change next tick and set to false
    private boolean shouldSync = false;

    //if this is true it will sync only the booleans because thats less data
    private boolean shouldSyncSimple = false;


    

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo info){
        if(shouldSync){
            shouldSync = false;
            if(((Player) (Object) this).level().isClientSide){
                System.out.println("sync client file for :" + ((Player) (Object) this).getName().toString());
            }else{
                System.out.println("sync server file for :" + ((Player) (Object) this).getName().toString());
            }
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
    @Override
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


    //getter and setter for if it is carried
    @Override
    public boolean getIsCarried() {
        return isCarried;
    }

    @Override
    public void setCarried(boolean isCarried) {
        this.isCarried = isCarried;
    }



    //getters and setters for if it is carrying someone and how
    @Override
    public boolean getIsCarrying() {
        return isCarrying;
    }

    @Override
    public void setCarrying(boolean isCarrying) {
        this.isCarrying = isCarrying;
    }

    @Override
    public boolean getShoulderCarry(){
        return isShoulderCarry;
    }

    @Override
    public void setShoulderCarry(boolean isShoulderCarry){
        this.isShoulderCarry = isShoulderCarry;
        //disable custom carry if shoulder carry is enabled
        if(isShoulderCarry){
            isCustomCarryPosition = false;
        } 
    }

    @Override
    public boolean getCustomCarry(){
        return isCustomCarryPosition;
    }

    @Override
    public void setCustomCarry(boolean isCustomCarryPosition){
        this.isCustomCarryPosition = isCustomCarryPosition;
        //disable shoulder carry if custom carry is enabled
        if(isCustomCarryPosition){
            isShoulderCarry = false;
        } 
    }


    //getter and setter for if its a menu object
    @Override
    public boolean getIsMenu() {
        return isMenuGraphic;
    }

    @Override
    public void setMenu(boolean menu) {
        isMenuGraphic = menu;
    }


    //getters and setters for calculating the position of its rider
    @Override
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
    @Override
    public void setXYMult(double xymult){
        this.xymult = xymult;
    }

    @Override
    public int getRotationOffset() {
        if(isCustomCarryPosition){
            return rotationOffset;
        }else if(isShoulderCarry){
            return shoulderRotationOffset;
        }else{
            return handRotationOffset;
        }
    }

    //set custom carry rotation position
    @Override
    public void setRotationOffset(int rotationOffset){
        this.rotationOffset = rotationOffset;
    }

    @Override
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
    @Override
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
    @Override
    public boolean getShouldSync() {
        return shouldSync;
    }

    @Override
    public void setShouldSync(boolean shouldSync) {
        this.shouldSync = shouldSync;
    }

    public void readCarryNbt(CompoundTag tag){

        isCarried = tag.getBoolean(PlayerCarryUtils.CARRIED_NBT_TAG);
        isCarrying = tag.getBoolean(PlayerCarryUtils.CARRYING_NBT_TAG);
        isShoulderCarry = tag.getBoolean(PlayerCarryUtils.SHOULDER_CARRY_NBT_TAG);
        isCustomCarryPosition = tag.getBoolean(PlayerCarryUtils.CUSTOM_CARRY_NBT_TAG);

        rotationOffset = tag.getInt(PlayerCarryUtils.ROTATION_NBT_TAG);
        xymult = tag.getDouble(PlayerCarryUtils.MULT_NBT_TAG);
        vertOffset = tag.getDouble(PlayerCarryUtils.VERT_NBT_TAG);
        leftRightMove = tag.getDouble(PlayerCarryUtils.LEFT_RIGHT_NBT_TAG);
        
    }

    public CompoundTag writeCarryNbt(CompoundTag tag){
        
        tag.putBoolean(PlayerCarryUtils.CARRIED_NBT_TAG, isCarried);
        tag.putBoolean(PlayerCarryUtils.CARRYING_NBT_TAG, isCarrying);
        tag.putBoolean(PlayerCarryUtils.SHOULDER_CARRY_NBT_TAG, isShoulderCarry);
        tag.putBoolean(PlayerCarryUtils.CUSTOM_CARRY_NBT_TAG, isCustomCarryPosition);

        tag.putInt(PlayerCarryUtils.ROTATION_NBT_TAG, rotationOffset);
        tag.putDouble(PlayerCarryUtils.MULT_NBT_TAG, xymult);
        tag.putDouble(PlayerCarryUtils.VERT_NBT_TAG, vertOffset);
        tag.putDouble(PlayerCarryUtils.LEFT_RIGHT_NBT_TAG, leftRightMove);

        return tag;
    }
    
}
