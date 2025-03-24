package com.ricardthegreat.holdmetight.utils;

import com.ricardthegreat.holdmetight.Config;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class PlayerSizeUtils {

    private ScaleType base = ScaleTypes.BASE;
    private ScaleType hitbox_height = ScaleTypes.HITBOX_HEIGHT;
    private ScaleType hitbox_width = ScaleTypes.HITBOX_WIDTH;
    private ScaleType step_height = ScaleTypes.STEP_HEIGHT;


    private float maxScale = (float) Config.maxHitboxScale;
    private float minScale = 0;


    private float currentScale;
    private float targetScale;

    public PlayerSizeUtils() {
    }

    public PlayerSizeUtils(float maxScale, float minScale) {
        this.maxScale = maxScale;
        this.minScale = minScale;
    }

    public static PlayerSizeUtils getPlayerSizeUtil(Player player){

    }

    public void setSize(Entity entity, Float size, int ticks){
        checkMaxHitbox(entity, size, ticks);
        ScaleData data = getScaleData(entity);
        data.setScale(size);
    }

    public void multSize(Entity entity, Float size, int ticks){
        Float targetScale = getScaleData(entity).getTargetScale()*size;
        setSize(entity, targetScale, ticks);
    }


    public float getSize(Entity entity) {
        return getScaleData(entity).getScale();
    }

    private ScaleData getScaleData(Entity entity) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;
        ScaleData data = pEnt.pehkui_getScaleData(base);
        return data;
    }

    private void checkMaxHitbox(Entity entity, float size, int ticks) {

        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;

        //im adding this here as a temp thing i need to move it to a proper place later
        fixStepHeight(pEnt, size, ticks);


        ScaleData heightData = pEnt.pehkui_getScaleData(hitbox_height);
        ScaleData widthData = pEnt.pehkui_getScaleData(hitbox_width);

        if (ticks > 0) {
            heightData.setScaleTickDelay(ticks);
            widthData.setScaleTickDelay(ticks);

            if (size > maxScale) {
                heightData.setTargetScale(maxScale/size);
                widthData.setTargetScale(maxScale/size);
            }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
                heightData.setTargetScale(1.0f);
                widthData.setTargetScale(1.0f);
            }
        }else{
            if (size > maxScale) {
                heightData.setScale(maxScale/size);
                widthData.setScale(maxScale/size);
            }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
                heightData.setScale(1.0f);
                widthData.setScale(1.0f);
            }
        }

        

        //return data;
    }

    private void fixStepHeight(PehkuiEntityExtensions pEnt, float size, int ticks){
        ScaleData stepData = pEnt.pehkui_getScaleData(ScaleTypes.STEP_HEIGHT);

        //this should make the step height equal to 1 + (height-1)/2
        float stepHeight = (1+((size-1)/2))/size;

        if (ticks > 0) {
            stepData.setScaleTickDelay(ticks);
            stepData.setTargetScale(stepHeight);
        }else{
            stepData.setScale(stepHeight);
        }
    }
}
