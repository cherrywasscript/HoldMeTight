package com.ricardthegreat.holdmetight.pehkui;

import java.util.List;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.ricardthegreat.holdmetight.HoldMeTight;

public class PehkuiMixinCanceller implements MixinCanceller{

    private static List<String> CANCEL = List.of(
        "virtuoel.pehkui.mixin.compat1204minus.ScreenHandlerMixin", 
        "virtuoel.pehkui.mixin.reach.compat1204minus.ScreenHandlerMixin",
        "virtuoel.pehkui.mixin.reach.compat1204minus.compat1194plus.InventoryMixin");

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        for (String string : CANCEL) {
            if (mixinClassName.equals(string)) {
                HoldMeTight.LOGGER.debug("cancelling mixin:" + mixinClassName);
                return true;
            }
        }
        return false;
    }
    
}
