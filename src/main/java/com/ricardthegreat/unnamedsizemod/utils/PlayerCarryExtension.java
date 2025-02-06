package com.ricardthegreat.unnamedsizemod.utils;

import net.minecraft.nbt.CompoundTag;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public interface PlayerCarryExtension {

    boolean getIsCarried();

    void setCarried(boolean carried);

    boolean getIsCarrying();

    void setCarrying(boolean carrying);

    boolean getShoulderCarry();

    void setShoulderCarry(boolean isShoulderCarry);
 
    boolean getCustomCarry();
    
    void setCustomCarry(boolean isCustomCarryPosition);

    double getVertOffset();

    void setVertOffset(double vert);

    double getXYMult();

    void setXYMult(double mult);

    int getRotationOffset();

    void setRotationOffset(int rotation);

    double getLeftRightMove();

    void setLeftRightMove(double leftRightMove);

    boolean getShouldSync();

    void setShouldSync(boolean sync);

    void updateSyncables(int rotationOffset, double xymult, double vertOffset, double leftRightMove, boolean isCarried, boolean isCarrying, boolean isShoulderCarry, boolean isCustomCarryPosition);

    void readCarryNbt(CompoundTag tag);

    CompoundTag writeCarryNbt(CompoundTag tag);
}
