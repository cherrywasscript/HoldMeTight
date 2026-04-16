package com.ricardthegreat.holdmetight.capabilities.preferences;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerPreferencesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{
    
    public static Capability<PlayerPreferences> PLAYER_PREFERENCES = CapabilityManager.get(new CapabilityToken<PlayerPreferences>() {});

    private PlayerPreferences preferences = null;
    private final LazyOptional<PlayerPreferences> optional = LazyOptional.of(this::createPlayerPreferences);

    private PlayerPreferences createPlayerPreferences() {
        if (this.preferences == null) {
            this.preferences = new PlayerPreferences();
        }

        return this.preferences;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerPreferences().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
       createPlayerPreferences().loadNBTData(tag);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_PREFERENCES) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    public static PlayerPreferences getPlayerPreferencesCapability(Player player){
        LazyOptional<PlayerPreferences> optional = player.getCapability(PLAYER_PREFERENCES);
        return optional.orElse(new PlayerPreferences());
    }
}
