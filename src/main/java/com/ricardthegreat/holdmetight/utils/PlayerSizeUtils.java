package com.ricardthegreat.holdmetight.utils;

import org.stringtemplate.v4.compiler.CodeGenerator.primary_return;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.mixins.playerextensions.PlayerSizeMixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class PlayerSizeUtils {

    private static ScaleType base = ScaleTypes.BASE;
    private static ScaleType hitbox_height = ScaleTypes.HITBOX_HEIGHT;
    private static ScaleType hitbox_width = ScaleTypes.HITBOX_WIDTH;
    private static ScaleType step_height = ScaleTypes.STEP_HEIGHT;

    private static float maxHitboxScale = (float) Config.maxHitboxScale; 

    private float maxScale = 50000;
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
        PlayerSizeExtension playerExt = (PlayerSizeExtension) player;
        return playerExt.getSizeUtil();
    }

    /**
     * set the player to change size
     * @param player - the player whos size is changing
     * @param size - the value the players size should become
     * @param ticks - the time it should take for the player to reach the given size in ticks (1/20 seconds)
     */
    public static void setSize(Player player, Float size, int ticks){
        PlayerSizeExtension pMix = (PlayerSizeExtension) player;

        //check if it is perpetual, instant, or over time change
        if (ticks < 0) {
            
        }else if (ticks == 0) {
            float mult = size/pMix.getCurrentScale();
            pMix.setCurrentScale(size);
            pMix.setTargetScale(pMix.getTargetScale()*mult);
            if (pMix.getRemainingTicks() == 0) {
                pMix.setRemainingTicks(1);
            }
        }else{
            pMix.setTargetScale(size);
            pMix.setRemainingTicks(ticks);
        }
    }

    /**
     * multiply a players size
     * @param player - the player whos size is changing
     * @param size - the multplier to be applied to the players size
     * @param ticks - the time it should take for the player to reach the given size in ticks (1/20 seconds)
     */
    public static void multSize(Player player, Float size, int ticks){
        Float targetScale = getScaleData(player).getTargetScale()*size;
        setSize(player, targetScale, ticks);
    }

    /**
     * set the player to perpetually change size
     * @param player - the player whos size is changing
     * @param size - the amount the player should change by over the given time
     * @param ticks - the time in ticks (1/20 seconds) in which the player should change by the amount given in size
     */
    public static void perpetualSize(Player player, Float size, int ticks){

    }

    //get a players size
    public static float getSize(Player player) {
        return getScaleData(player).getScale();
    }

    private static ScaleData getScaleData(Player player) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;
        ScaleData data = pEnt.pehkui_getScaleData(base);
        return data;
    }

    private static void checkMaxHitbox(Entity entity, float size, int ticks) {

        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) entity;

        //im adding this here as a temp thing i need to move it to a proper place later
        //fixStepHeight(pEnt, size, ticks);


        ScaleData heightData = pEnt.pehkui_getScaleData(hitbox_height);
        ScaleData widthData = pEnt.pehkui_getScaleData(hitbox_width);

        if (ticks > 0) {
            heightData.setScaleTickDelay(ticks);
            widthData.setScaleTickDelay(ticks);

            if (size > maxHitboxScale) {
                heightData.setTargetScale(maxHitboxScale/size);
                widthData.setTargetScale(maxHitboxScale/size);
            }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
                heightData.setTargetScale(1.0f);
                widthData.setTargetScale(1.0f);
            }
        }else{
            if (size > maxHitboxScale) {
                heightData.setScale(maxHitboxScale/size);
                widthData.setScale(maxHitboxScale/size);
            }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
                heightData.setScale(1.0f);
                widthData.setScale(1.0f);
            }
        }

        

        //return data;
    }

    private static void fixStepHeight(PehkuiEntityExtensions pEnt, float size, int ticks){
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
