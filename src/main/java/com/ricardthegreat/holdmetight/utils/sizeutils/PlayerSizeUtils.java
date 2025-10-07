package com.ricardthegreat.holdmetight.utils.sizeutils;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class PlayerSizeUtils {

    private static ScaleType base = ScaleTypes.BASE;

    /**
     * set the player to change size
     * @param player - the player whos size is changing
     * @param size - the value the players size should become
     * @param ticks - the time it should take for the player to reach the given size in ticks (1/20 seconds)
     */
    public static void setSize(Player player, float size, int ticks){
        //ensure the size is not greater than the maximum allowed by the config
        size = clampToPreferences(player, size);
        size = lockSizeCap(size);
        System.out.println(size);
        ScaleData data = getScaleData(player);

        if (ticks < 0) {
            
        }else if (ticks == 0) {
            float mult = size/data.getScale();
            float prevTargScale = data.getTargetScale();
            
            data.setScale(size);
            data.setTargetScale(prevTargScale*mult);
        }else{
            data.setScaleTickDelay(ticks);
            data.setTargetScale(size);
        }
    }

    /**
     * multiply a players size 
     * @param player - the player whos size is changing
     * @param size - the multplier to be applied to the players size
     * @param ticks - the time it should take for the player to reach the given size in ticks (1/20 seconds)
     */
    public static void multSize(Player player, Float size, int ticks){
        ScaleData data = getScaleData(player);

        Float targetScale = data.getTargetScale()*size;
        
        setSize(player, targetScale, ticks);
    }

    /**
     * set the player to perpetually change size TODO implement
     * @param player - the player whos size is changing
     * @param size - the amount the player should change by over the given time
     * @param ticks - the time in ticks (1/20 seconds) in which the player should change by the amount given in size
     */
    public static void perpetualSize(Player player, Float size, int ticks){

    }

    /**
     * add to the players height instantly (use SEntityAddTargetScalePacket to call this from client)
     * @param player - the player whos size is changing
     * @param size - the amount that should be added to their size
     */
    public static void addSize(Player player, Float size){
        ScaleData data = getScaleData(player);

        Float currentScale = data.getScale();
        Float targetScale = data.getTargetScale();

        currentScale = lockSizeCap(currentScale + size);
        targetScale = lockSizeCap(targetScale + size);

        data.setScale(currentScale);
        data.setTargetScale(targetScale);
    }

    //get a players size
    public static float getSize(Player player) {
        return getScaleData(player).getScale();
    }

    public static int getRemainingTicks(Player player) {
        //taken from calculateRemainingScaleTicks() in the ScaleData class from pehkui
        final float lastTarget = getScaleData(player).getTargetScale();
		final float initial = getScaleData(player).getInitialScale();
		
		final int remaining = lastTarget == initial ? 0 : Math.round(getScaleData(player).getScaleTickDelay() * ((lastTarget - getScaleData(player).getBaseScale()) / (lastTarget - initial)));

        return remaining;
    }

    public static float getTargetSize(Player player) {
        return getScaleData(player).getTargetScale();
    }

    private static ScaleData getScaleData(Player player) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;
        ScaleData data = pEnt.pehkui_getScaleData(base);
        return data;
    }

    private static Float lockSizeCap(float size){
        return (float) Math.min(size, Config.maxEntityScale);
    }




    public static void setPeripheralScales(Player player){
        float currentScale = getScaleData(player).getScale();
        clampMaxHitbox(player, currentScale);
        fixStepHeight(player, currentScale);
        setFallDamage(player, currentScale);

        if (Config.miningSpeedScaleLink) {
            setMiningSpeed(player, currentScale);
        }

        if (Config.damageTakenScaleLink) {
            setDefence(player, currentScale);
        }
    }

    //ensure that the players hitbox does not go above the maximum and 
    //ensure that it returns to the correct proportions once the player has gotten small enough
    private static void clampMaxHitbox(Player player, float currentScale) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData heightData = pEnt.pehkui_getScaleData(ScaleTypes.HITBOX_HEIGHT);
        ScaleData widthData = pEnt.pehkui_getScaleData(ScaleTypes.HITBOX_WIDTH);

        float maxHitboxScale = (float) Config.maxHitboxScale;

        if (currentScale > maxHitboxScale) {
            heightData.setScale(maxHitboxScale/currentScale);
            widthData.setScale(maxHitboxScale/currentScale);
        }else if (heightData.getTargetScale() < 1.0f || widthData.getTargetScale() < 1.0f){
            heightData.setScale(1.0f);
            widthData.setScale(1.0f);
        }
    }

    private static void setFallDamage(Player player, float currentScale){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData fallData = pEnt.pehkui_getScaleData(ScaleTypes.FALLING);

        if (currentScale < 1) {
            fallData.setScale(currentScale);
        }else if (fallData.getScale() < 1) {
            fallData.setScale(1f);
        }
    }

    private static void setDefence(Player player, float currentScale){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData defenceData = pEnt.pehkui_getScaleData(ScaleTypes.DEFENSE);

        defenceData.setScale((float) Math.sqrt(currentScale));
    }

    private static void setMiningSpeed(Player player, float currentScale){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData miningData = pEnt.pehkui_getScaleData(ScaleTypes.MINING_SPEED);

        miningData.setScale(currentScale);
    }

    //step height scales weirdly this helps alliviate that a bit
    private static void fixStepHeight(Player player, float currentScale){
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;

        ScaleData stepData = pEnt.pehkui_getScaleData(ScaleTypes.STEP_HEIGHT);

        //for some reason step height increases by step height scale^2 so i need to counteract that
        float stepHeight = (float) (1/Math.sqrt(currentScale));

        stepData.setScale(stepHeight);
    }

    private static float clampToPreferences(Player player, float size){
        LazyOptional<PlayerSize> optional = player.getCapability(PlayerSizeProvider.PLAYER_SIZE);
        PlayerSize orElse = optional.orElse(null);
        if (orElse != null) {
            size = Math.max(size, orElse.getMinScale());
            size = Math.min(size, orElse.getMaxScale());
        }

        return size;
    }
}
