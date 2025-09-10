package com.ricardthegreat.holdmetight.utils.sizeutils;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

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
    public static void setSize(Player player, float size, int ticks){
        PlayerSize playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);

        //ensure the size is not greater than the maximum allowed by the config
        size = lockSizeCap(size);

        //check if it is perpetual, instant, or over time change
        if (ticks < 0) {
            
        }else if (ticks == 0) {
            float mult = size/playerSize.getCurrentScale();
            
            playerSize.setCurrentScale(size);
            playerSize.setTargetScale(playerSize.getTargetScale()*mult);
            if (playerSize.getRemainingTicks() == 0) {
                playerSize.setRemainingTicks(1);
            }
        }else{
            playerSize.setTargetScale(size);
            playerSize.setRemainingTicks(ticks);
        }

        playerSize.updateShouldSync();
    }

    /**
     * multiply a players size
     * @param player - the player whos size is changing
     * @param size - the multplier to be applied to the players size
     * @param ticks - the time it should take for the player to reach the given size in ticks (1/20 seconds)
     */
    public static void multSize(Player player, Float size, int ticks){
        PlayerSize playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);

        Float targetScale = playerSize.getTargetScale()*size;
        
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
     * add to the players height instantly (use SEntityAddTargetScalePacket to call this from client)
     * @param player - the player whos size is changing
     * @param size - the amount that should be added to their size
     */
    public static void addSize(Player player, Float size){
        PlayerSize playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);

        Float currentScale = playerSize.getCurrentScale();
        Float targetScale = playerSize.getTargetScale();

        //TODO make this better currently just locks the cap at the moment of applying should probably do this before
        playerSize.setCurrentScale(lockSizeCap(currentScale + size));
        playerSize.setTargetScale(lockSizeCap(targetScale + size));

        if (playerSize.getRemainingTicks() == 0) {
            playerSize.setRemainingTicks(1);
        }

        playerSize.updateShouldSync();
    }

    //get a players size
    public static float getSize(Player player) {
        PlayerSize playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);

        //TODO check this, currently returns pehkui scale data which might be more reliable than mine
        //return playerSize.getCurrentScale();
        return getScaleData(player).getScale();
    }

    public static int getRemainingTicks(Player player) {
        PlayerSize playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);
        return playerSize.getRemainingTicks();
        //return getScaleData(player).getScale();
    }

    public static float getTargetSize(Player player) {
        PlayerSize playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);
        return playerSize.getTargetScale();
        //return getScaleData(player).getScale();
    }

    private static ScaleData getScaleData(Player player) {
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;
        ScaleData data = pEnt.pehkui_getScaleData(base);
        return data;
    }

    private static Float lockSizeCap(float size){
        System.out.println(size);
        System.out.println(Config.maxEntityScale);
        if (size > Config.maxEntityScale) {
            return (float) Config.maxEntityScale;
        }
        
        return size;
    }
}
