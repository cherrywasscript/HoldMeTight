package com.ricardthegreat.holdmetight.utils.sizeutils;

import com.ricardthegreat.holdmetight.Config;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

/*
 * need to completely redo this class at some point as it currently is crap
 */
public class EntitySizeUtils {

    private static final ScaleType base = ScaleTypes.BASE;
    private static final ScaleType hitbox_height = ScaleTypes.HITBOX_HEIGHT;
    private static final ScaleType hitbox_width = ScaleTypes.HITBOX_WIDTH;
    private static final ScaleType step_height = ScaleTypes.STEP_HEIGHT;

    private static float maxScale = (float) Config.maxHitboxScale;

    public static void setSize(Entity entity, Float size, int ticks) {
        if (entity instanceof Player) {
            PlayerSizeUtils.setSize((Player) entity, size, ticks);
        }else {
            if (ticks < 0) {
            ticks = 0;
            }

            checkMaxHitbox(entity, size, ticks);
            ScaleData data = getScaleData(entity);
            data.setScaleTickDelay(ticks);
            data.setTargetScale(size);
        }
    }

    public static void multSize(Entity entity, Float size, int ticks) {
        if (entity instanceof Player) {
            PlayerSizeUtils.multSize((Player) entity, size, ticks);
        }else{
            Float targetScale = getScaleData(entity).getTargetScale()*size;
            setSize(entity, targetScale, ticks);
        }
    } 

    public static float getSize(Entity entity) {
        if (entity instanceof Player) {
            return PlayerSizeUtils.getSize((Player) entity);
        }
        return getScaleData(entity).getScale();
    }

    private static ScaleData getScaleData(Entity entity) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;
        ScaleData data = pEnt.pehkui_getScaleData(base);
        return data;
    }

    private static void checkMaxHitbox(Entity entity, float size, int ticks) {

        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;

        //TODO implement stepheight fix for other entities
        //fixStepHeight(pEnt, size, ticks);


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
