package com.ricardthegreat.holdmetight.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings(){}

    private static final String CATEGORY = "key.categories." + HoldMeTight.MODID;

    public final KeyMapping shoulderCarryKey = new KeyMapping(
        "key." + HoldMeTight.MODID + ".shoulder_carry_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_P, -1),
         CATEGORY);
    
    public final KeyMapping customCarryKey = new KeyMapping(
        "key." + HoldMeTight.MODID + ".custom_carry_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_U, -1),
         CATEGORY);

    public final KeyMapping carryWheelKey = new KeyMapping(
        "key." + HoldMeTight.MODID + ".carry_wheel_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_V, -1),
         CATEGORY);

    public final KeyMapping sizePrefsKey = new KeyMapping(
        "key." + HoldMeTight.MODID + ".size_prefs_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_RBRACKET, -1),
         CATEGORY);

    public final KeyMapping carryScreenKey = new KeyMapping(
        "key." + HoldMeTight.MODID + ".carry_screen_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_LBRACKET, -1),
         CATEGORY);
}
