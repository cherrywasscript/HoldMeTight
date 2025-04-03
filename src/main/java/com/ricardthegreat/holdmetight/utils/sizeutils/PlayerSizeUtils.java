package com.ricardthegreat.holdmetight.utils.sizeutils;

import com.ricardthegreat.holdmetight.utils.PlayerSizeExtension;

import net.minecraft.world.entity.player.Player;
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
        PlayerSizeExtension pMix = (PlayerSizeExtension) player;
        Float targetScale = pMix.getTargetScale()*size;
        
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

    /**
     * add to the players height instantly
     * @param player - the player whos size is changing
     * @param size - the amount that should be added to their size
     */
    public static void addSize(Player player, Float size){
        PlayerSizeExtension pMix = (PlayerSizeExtension) player;
        Float currentScale = pMix.getCurrentScale()*size;
        Float targetScale = pMix.getTargetScale()*size;

        pMix.setCurrentScale(currentScale + size);
        pMix.setTargetScale(targetScale + size);

        if (pMix.getRemainingTicks() == 0) {
            pMix.setRemainingTicks(1);
        }
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
}
