package com.ricardthegreat.holdmetight.size;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.network.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.SPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.fml.DistExecutor;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class PlayerSize {

    //the max and min size this player can be as well as their default size
    //default size is what it will be set to when "reset" is used on size devices
    //max size is default 50k because i think the game will break far before then so its effectively infinite
    private float maxScale = 50000;
    private float minScale = 0;
    private float defaultScale = 1;

    //current and target scale, not sure if having these here is correct but still
    private float currentScale = 1;
    private float targetScale = 1;

    private boolean perpetualChange = false;
    private float perpetualChangeValue = 1;

    //remaining number of size change ticks (-1 means infinite)
    private int remainingTicks = 0;


    //maximum size a hitbox can be
    private float maxHitboxScale = (float) Config.maxHitboxScale; 


    //private ScaleData data = ((PehkuiEntityExtensions) (Player) (Object) this).pehkui_getScaleData(ScaleTypes.BASE);

    boolean shouldSync = false;


    public void tick(Player player){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;
        ScaleData baseData = pEnt.pehkui_getScaleData(ScaleTypes.BASE);
        
        if (!player.level().isClientSide) {
            if (remainingTicks > 0) {
                nextScaleStep();
    
                if (baseData.getScaleTickDelay() != 1) {
                    baseData.setScaleTickDelay(1);
                }
    
                baseData.setTargetScale(currentScale);
    
                setPeripheralScales(player);

                shouldSync = true;
            }else if(remainingTicks == 0){
                 if (baseData.getScale() != currentScale) {
                    currentScale = baseData.getScale();
                    targetScale = currentScale;
                    
                    setPeripheralScales(player);

                    shouldSync = true;
                }
            }
    
            if (perpetualChange) {
                baseData.setScale(currentScale*perpetualChangeValue);

                setPeripheralScales(player);

                shouldSync = true;
            }
        }

        

        if (shouldSync) {
            sync(player);
        }
    }
    
    private void sync(Player player){
        
        if (player.level().isClientSide) {
            //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
            //    PacketHandler.sendToServer(new SPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, currentScale, 
            //    targetScale, remainingTicks, player.getUUID())));
        }else {
            if (!player.getServer().isDedicatedServer()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToAllClients(new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, currentScale, 
                targetScale, remainingTicks, player.getUUID())));
            }else{
                DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                PacketHandler.sendToAllClients(new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, currentScale, 
                targetScale, remainingTicks, player.getUUID())));
            }
        }
            
        shouldSync = false;
    }

    public void updateSyncables(float maxScale, float minScale, float defaultScale, float currentScale, float targetScale, int remainingTicks){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
        this.currentScale = currentScale;
        this.targetScale = targetScale;
        this.remainingTicks = remainingTicks;

        /*
         * perpetualChange
         * perpetualChangeValue
         */
    }

    public CPlayerSizeMixinSyncPacket getSyncPacket(Player player){
        return new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, currentScale, targetScale, remainingTicks, player.getUUID());
    }

    //step the players size by 1 stage
    //
    private void nextScaleStep(){
        float scaleChange = (targetScale - currentScale)/remainingTicks;
        currentScale += scaleChange;
        remainingTicks--;
    }

    private void setPeripheralScales(Player player){
        clampMaxHitbox(player);
        fixStepHeight(player);
        if (Config.miningSpeedScaleLink) {
            setMiningSpeed(player);
        }
        if (Config.damageTakenScaleLink) {
            setDefence(player);
        }
    }

    //ensure that the players hitbox does not go above the maximum and 
    //ensure that it returns to the correct proportions once the player has gotten small enough
    private void clampMaxHitbox(Player player) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData heightData = pEnt.pehkui_getScaleData(ScaleTypes.HITBOX_HEIGHT);
        ScaleData widthData = pEnt.pehkui_getScaleData(ScaleTypes.HITBOX_WIDTH);

        if (currentScale > maxHitboxScale) {
            heightData.setScale(maxHitboxScale/currentScale);
            widthData.setScale(maxHitboxScale/currentScale);
        }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
            heightData.setScale(1.0f);
            widthData.setScale(1.0f);
        }
    }

    private void setDefence(Player player){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData defenceData = pEnt.pehkui_getScaleData(ScaleTypes.DEFENSE);

        defenceData.setScale((float) Math.sqrt(currentScale));
    }

    private void setMiningSpeed(Player player){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData miningData = pEnt.pehkui_getScaleData(ScaleTypes.MINING_SPEED);

        miningData.setScale(currentScale);
    }

    //step height scales weirdly this helps alliviate that a bit
    private void fixStepHeight(Player player){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData stepData = pEnt.pehkui_getScaleData(ScaleTypes.STEP_HEIGHT);

        //for some reason step height increases by step height scale^2 so i need to counteract that
        float stepHeight = (float) (1/Math.sqrt(currentScale));

        stepData.setScale(stepHeight);
    }

    public Float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(Float maxScale) {
        this.maxScale = maxScale;
    }

    public Float getMinScale() {
        return minScale;
    }

    public void setMinScale(Float minScale) {
        this.minScale = minScale;
    }

    public Float getTargetScale() {
        return targetScale;
    }

    public void setTargetScale(Float targetScale) {
        if (targetScale > maxScale) {
            this.targetScale = maxScale;
        }else if (targetScale < minScale) {
            this.targetScale = minScale;
        }else {
            this.targetScale = targetScale;
        } 
    }

    public Float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(Float currentScale) {
        this.currentScale = currentScale;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    public boolean getPerpetualChange() {
        return perpetualChange;
    }

    public void setPerpetualChange(boolean perpetualChange) {
        this.perpetualChange = perpetualChange;
    }

    public float getPerpetualChangeValue() {
        return perpetualChangeValue;
    }

    public void setPerpetualChangeValue(float perpetualChangeValue) {
        this.perpetualChangeValue = perpetualChangeValue;
    }

    public void updateShouldSync(){
        this.shouldSync = true;
    }

    public void copyAll(PlayerSize source){
        this.maxScale = source.maxScale;
        this.minScale = source.minScale;
        this.defaultScale = source.defaultScale;
        this.currentScale = source.currentScale;
        this.targetScale = source.targetScale;
        this.perpetualChange = source.perpetualChange;
        this.perpetualChangeValue = source.perpetualChangeValue;
        this.remainingTicks = source.remainingTicks;
    }

    public void copyBasic(PlayerSize source){
        this.maxScale = source.maxScale;
        this.minScale = source.minScale;
        this.defaultScale = source.defaultScale;
    }

    public void saveNBTData(CompoundTag tag){
        tag.putFloat("maxScale", maxScale);
        tag.putFloat("minScale", minScale);
        tag.putFloat("defaultScale", defaultScale);
        tag.putFloat("currentScale", currentScale);
        tag.putFloat("targetScale", targetScale);
        tag.putBoolean("perpetualChange", perpetualChange);
        tag.putFloat("perpetualChangeValue", perpetualChangeValue);
        tag.putInt("remainingTicks", remainingTicks);
    }

    public void loadNBTData(CompoundTag tag){
        maxScale = tag.getFloat("maxScale");
        minScale = tag.getFloat("minScale");
        defaultScale = tag.getFloat("defaultScale");
        currentScale = tag.getFloat("currentScale");
        targetScale = tag.getFloat("targetScale");
        perpetualChange = tag.getBoolean("perpetualChange");
        perpetualChangeValue = tag.getFloat("perpetualChangeValue");
        remainingTicks = tag.getInt("remainingTicks");
    }
}
