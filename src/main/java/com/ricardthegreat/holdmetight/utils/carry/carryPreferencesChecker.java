package com.ricardthegreat.holdmetight.utils.carry;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class carryPreferencesChecker {
    public static boolean canStopRiding(Player rider, Entity vehicle){
        if (vehicle instanceof Player vehicPlayer) {
            PlayerPreferences thisPreferences = PlayerPreferencesProvider.getPlayerPreferencesCapability(rider);
            PlayerPreferences vehiclePreferences = PlayerPreferencesProvider.getPlayerPreferencesCapability(vehicPlayer);
            if (thisPreferences.getCanBeTrappedWhileCarried() && vehiclePreferences.getTrapCarriedPlayer()) {
                return false;
            }
        }
        return true;
    }

    public static boolean canPickup(Player vehicle, Entity rider){
        if (vehicle.getPassengers().contains(rider)) {
            HoldMeTight.LOGGER.debug("cannot carry something you are already carrying");
            return false;
        }

        if (rider instanceof Player player) {
            PlayerPreferences vehPref = PlayerPreferencesProvider.getPlayerPreferencesCapability(vehicle);
            PlayerPreferences ridPref = PlayerPreferencesProvider.getPlayerPreferencesCapability(player);
            if (!vehPref.getCanPickupOthers() || !ridPref.getCanBePickedup()) {
                return false;
            }
        }
        return canCarry(vehicle, rider);
    }

    public static boolean canCarry(Player vehicle, Entity rider){
        return EntitySizeUtils.getSize(rider) <= EntitySizeUtils.getSize(vehicle)*HMTConfig.SERVER_CONFIG.pickupRatioScale.get();
    }
}
