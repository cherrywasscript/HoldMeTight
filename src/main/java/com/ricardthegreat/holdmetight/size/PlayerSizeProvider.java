package com.ricardthegreat.holdmetight.size;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerSizeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{

    public static Capability<PlayerSize> PLAYER_SIZE = CapabilityManager.get(new CapabilityToken<PlayerSize>() {});

    private PlayerSize size = null;
    private final LazyOptional<PlayerSize> optional = LazyOptional.of(this::createPlayerSize);

    private PlayerSize createPlayerSize() {
        if (this.size == null) {
            this.size = new PlayerSize();
        }

        return this.size;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerSize().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
       createPlayerSize().loadNBTData(tag);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_SIZE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    //TODO make it so the only file that references this is the playersizeutils file
    public static PlayerSize getPlayerSizeCapability(Player player){
        LazyOptional<PlayerSize> optional = player.getCapability(PLAYER_SIZE);
        return optional.orElse(new PlayerSize());
    }
}
