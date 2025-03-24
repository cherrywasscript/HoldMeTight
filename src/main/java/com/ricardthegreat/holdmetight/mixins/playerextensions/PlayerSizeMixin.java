package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.utils.PlayerSizeExtension;
import com.ricardthegreat.holdmetight.utils.PlayerSizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

@Mixin(Player.class)
public abstract class PlayerSizeMixin implements PlayerSizeExtension {

    private PlayerSizeUtils sizeUtil = new PlayerSizeUtils();

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

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo info){
        if (remainingTicks > 0) {
            nextScaleStep();

            getScaleData().setScale(currentScale);
            clampMaxHitbox();
            fixStepHeight();
        }

        if (perpetualChange) {
            getScaleData().setScale(currentScale*perpetualChangeValue);
            clampMaxHitbox();
            fixStepHeight();
        }
    }

    //step the players size by 1 stage
    //
    private void nextScaleStep(){
        float scaleChange = (targetScale - currentScale)/remainingTicks;
        currentScale += scaleChange;
        remainingTicks--;
    }

    //returns the scale data for this player, i am unsure if this is something i can just grab once or not so currently
    //it is freshly grabbed every time
    private ScaleData getScaleData() {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) (Player) (Object) this;
        ScaleData data = pEnt.pehkui_getScaleData(ScaleTypes.BASE);
        return data;
    }

    //ensure that the players hitbox does not go above the maximum and 
    //ensure that it returns to the correct proportions once the player has gotten small enough
    private void clampMaxHitbox() {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) (Player) (Object) this;

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

    //step height scales weirdly this helps alliviate that a bit
    private void fixStepHeight(){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) (Player) (Object) this;

        ScaleData stepData = pEnt.pehkui_getScaleData(ScaleTypes.STEP_HEIGHT);

        //this should make the step height equal to 1 + (height-1)/2
        float stepHeight = (1+((currentScale-1)/2))/currentScale;

        stepData.setScale(stepHeight);
    }

    @Override
    public PlayerSizeUtils getSizeUtil() {
        return sizeUtil;
    }

    @Override
    public Float getMaxScale() {
        return maxScale;
    }

    @Override
    public void setMaxScale(Float maxScale) {
        this.maxScale = maxScale;
    }

    @Override
    public Float getMinScale() {
        return minScale;
    }

    @Override
    public void setMinScale(Float minScale) {
        this.minScale = minScale;
    }

    @Override
    public Float getTargetScale() {
        return targetScale;
    }

    @Override
    public void setTargetScale(Float targetScale) {
        this.targetScale = targetScale;
    }

    @Override
    public Float getCurrentScale() {
        return currentScale;
    }

    @Override
    public void setCurrentScale(Float currentScale) {
        this.currentScale = currentScale;
    }

    @Override
    public int getRemainingTicks() {
        return remainingTicks;
    }

    @Override
    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    @Override
    public boolean getPerpetualChange() {
        return perpetualChange;
    }

    @Override
    public void setPerpetualChange(boolean perpetualChange) {
        this.perpetualChange = perpetualChange;
    }

    @Override
    public float getPerpetualChangeValue() {
        return perpetualChangeValue;
    }

    @Override
    public void setPerpetualChangeValue(float perpetualChangeValue) {
        this.perpetualChangeValue = perpetualChangeValue;
    }
    
}
