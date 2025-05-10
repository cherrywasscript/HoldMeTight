package com.ricardthegreat.holdmetight.carry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ricardthegreat.holdmetight.size.PlayerSize;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCarryProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{

    public static Capability<PlayerCarry> PLAYER_CARRY = CapabilityManager.get(new CapabilityToken<PlayerCarry>() {});

    private PlayerCarry size = null;
    private final LazyOptional<PlayerCarry> optional = LazyOptional.of(this::createPlayerCarry);

    private PlayerCarry createPlayerCarry() {
        if (this.size == null) {
            this.size = new PlayerCarry();
        }

        return this.size;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerCarry().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
       createPlayerCarry().loadNBTData(tag);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_CARRY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    public static PlayerCarry getPlayerSizeCapability(Player player){
        LazyOptional<PlayerCarry> optional = player.getCapability(PLAYER_CARRY);
        return optional.orElse(new PlayerCarry());
    }
}
