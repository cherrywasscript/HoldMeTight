package com.ricardthegreat.holdmetight.utils;

import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

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

    void updateShouldSync();

    void updateSyncables(float maxScale, float minScale, float defaultScale, float currentScale, float targetScale, int remainingTicks);
}
