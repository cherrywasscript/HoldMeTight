package com.ricardthegreat.unnamedsizemod.utils;

import net.minecraft.world.entity.Entity;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class SizeUtils {

    private static ScaleType base = ScaleTypes.BASE;
    private static ScaleType hitbox_height = ScaleTypes.HITBOX_HEIGHT;
    private static ScaleType hitbox_width = ScaleTypes.HITBOX_WIDTH;

    private static Float maxScale = 8.0f;

    
    public static void setSizeInstant() {
        
    }

    public static void multSizeInstant() {
        
    }

    //i should probably grab the actual default from pekhui as if it changes from 20 this wont however a second is a good default i feel
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

        ScaleData heightData = pEnt.pehkui_getScaleData(hitbox_height);
        ScaleData widthData = pEnt.pehkui_getScaleData(hitbox_width);

        heightData.setScaleTickDelay(ticks);
        widthData.setScaleTickDelay(ticks);

        if (size > maxScale) {
            heightData.setTargetScale(maxScale/size);
            widthData.setTargetScale(maxScale/size);
        }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
            heightData.setTargetScale(1.0f);
            widthData.setTargetScale(1.0f);
        }

        //return data;
    }
}
