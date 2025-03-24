package com.ricardthegreat.holdmetight.utils;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerSizeExtension {
    
    PlayerSizeUtils getSizeUtil();

    Float getMaxScale();
    void setMaxScale(Float maxScale);

    Float getMinScale();
    void setMinScale(Float minScale);

    Float getTargetScale();
    void setTargetScale(Float targetScale);

    Float getCurrentScale();
    void setCurrentScale(Float currentScale);

    int getRemainingTicks();
    void setRemainingTicks(int remainingTicks);

    boolean getPerpetualChange();
    void setPerpetualChange(boolean perpetualChange);

    float getPerpetualChangeValue();
    void setPerpetualChangeValue(float perpetualChangeValue);

}
