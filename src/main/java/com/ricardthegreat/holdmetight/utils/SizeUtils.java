package com.ricardthegreat.holdmetight.utils;

import com.ricardthegreat.holdmetight.Config;

import net.minecraft.world.entity.Entity;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class SizeUtils {

    private static final ScaleType base = ScaleTypes.BASE;
    private static final ScaleType hitbox_height = ScaleTypes.HITBOX_HEIGHT;
    private static final ScaleType hitbox_width = ScaleTypes.HITBOX_WIDTH;
    private static final ScaleType step_height = ScaleTypes.STEP_HEIGHT;

    private static float maxScale = (float) Config.maxHitboxScale;

    
    
    public static void setSizeInstant(Entity entity, Float size) {
        checkMaxHitbox(entity, size, 0);
        ScaleData data = getScaleData(entity);
        data.setScale(size);
    }

    public static void multSizeInstant(Entity entity, Float size) {
        Float targetScale = getScaleData(entity).getTargetScale()*size;
        setSizeInstant(entity, targetScale);
    }

    //i should probably grab the actual default from pekhui as if it changes from 20 this wont however a second is a good default i feel
    //also i should spend some time to encorporate these like 6 similar methods into 1 or 2 methods it should be possible and will make for nicer code
    public static void setSizeOverTimeDefault(Entity entity, Float size) {
        checkMaxHitbox(entity, size, 20);
        ScaleData data = getScaleData(entity);
        data.setScaleTickDelay(20);
        data.setTargetScale(size);
    }   

    public static void multSizeOverTimeDefault(Entity entity, Float size){
        Float targetScale = getScaleData(entity).getTargetScale()*size;
        setSizeOverTimeDefault(entity, targetScale);
    }

    public static void setSizeOverTimeCustom(Entity entity, Float size, int ticks) {
        checkMaxHitbox(entity, size, ticks);
        ScaleData data = getScaleData(entity);
        data.setScaleTickDelay(ticks);
        data.setTargetScale(size);
    }   

    public static void multSizeOverTimeCustom(Entity entity, Float size, int ticks){
        Float targetScale = getScaleData(entity).getTargetScale()*size;
        setSizeOverTimeCustom(entity, targetScale, ticks);
    }


    public static float getSize(Entity entity) {
        return getScaleData(entity).getScale();
    }

    private static ScaleData getScaleData(Entity entity) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;
        ScaleData data = pEnt.pehkui_getScaleData(base);
        return data;
    }

    private static void checkMaxHitbox(Entity entity, float size, int ticks) {

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

    private static void fixStepHeight(PehkuiEntityExtensions pEnt, float size, int ticks){
        ScaleData stepData = pEnt.pehkui_getScaleData(step_height);

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
