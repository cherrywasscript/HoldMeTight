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

    
    public static void setSize() {
        
    }

    public static void setTargetSize(Entity entity, Float size) {
        checkMaxHitbox(entity, size);
        getScaleData(entity).setTargetScale(size);
    }   

    public static void multTargetSize(Entity entity, Float size){
        ScaleData data = getScaleData(entity);

        Float targetScale = data.getTargetScale()*size;

        data.setTargetScale(targetScale);
    }

    public static float getSize(Entity entity) {

        return getScaleData(entity).getScale();

    }

    private static ScaleData getScaleData(Entity entity) {

        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;

        ScaleData data = pEnt.pehkui_getScaleData(base);

        return data;
    }

    private static void checkMaxHitbox(Entity entity, float size) {

        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;

        ScaleData heightData = pEnt.pehkui_getScaleData(hitbox_height);
        ScaleData widthData = pEnt.pehkui_getScaleData(hitbox_width);

        if (size > maxScale) {
            heightData.setTargetScale(maxScale/size);
            widthData.setTargetScale(maxScale/size);
        }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
            heightData.setTargetScale(1.0f);
            widthData.setTargetScale(1.0f);
        }

        System.out.println("Target: " + heightData.getTargetScale());
        System.out.println("actual" + heightData.getScale());

        

        //return data;
    }


    private static Float limitToMax(Float scale) {
        if(scale > maxScale){
            return maxScale;
        }else {
            return scale;
        }
    }
}
