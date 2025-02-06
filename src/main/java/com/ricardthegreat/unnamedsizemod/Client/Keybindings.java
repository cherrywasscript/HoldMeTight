package com.ricardthegreat.unnamedsizemod.Client;

import com.mojang.blaze3d.platform.InputConstants;
import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings(){}

    private static final String CATEGORY = "key.categories." + UnnamedSizeMod.MODID;

    public final KeyMapping shoulderCarryKey = new KeyMapping(
        "key." + UnnamedSizeMod.MODID + ".shoulder_carry_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_P, -1),
         CATEGORY);
    
    public final KeyMapping customCarryKey = new KeyMapping(
        "key." + UnnamedSizeMod.MODID + ".custom_carry_key",
         KeyConflictContext.IN_GAME, 
         InputConstants.getKey(InputConstants.KEY_V, -1),
         CATEGORY);
}
