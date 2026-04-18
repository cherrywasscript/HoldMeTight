package com.ricardthegreat.holdmetight.mixins.carry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.utils.carry.carryPreferencesChecker;

import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class PlayerDismountMixin {
    @Inject(at = @At("HEAD"), method = "wantsToStopRiding()Z", cancellable = true)
    protected void wantsToStopRiding(CallbackInfoReturnable<Boolean> info) {
        Player thisP = ((Player)(Object)this);
        if (!carryPreferencesChecker.canStopRiding(thisP, thisP.getVehicle())) {
            info.setReturnValue(false);
        }
    }
}
