package com.ricardthegreat.holdmetight.capabilities.size;

import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.size.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.size.SPlayerSizeMixinSyncPacket;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PlayerSize {

    //TODO remove this class if i find no use for it

    public void updateSyncables(float maxScale, float minScale, float defaultScale){

    }

    public CPlayerSizeMixinSyncPacket getSyncPacket(Player player){
        return new CPlayerSizeMixinSyncPacket(player.getUUID());
    }

    public void copy(PlayerSize source){
    }

    public void saveNBTData(CompoundTag tag){
    }

    public void loadNBTData(CompoundTag tag){
    }
}
