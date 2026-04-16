package com.ricardthegreat.holdmetight.capabilities.size;

import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.size.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.size.SPlayerSizeMixinSyncPacket;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PlayerSize {

    public void updateSyncables(float maxScale, float minScale, float defaultScale){

    }

    public CPlayerSizeMixinSyncPacket getSyncPacket(Player player){
        return new CPlayerSizeMixinSyncPacket(player.getUUID());
    }

    public void updateShouldSync(){
        this.shouldSync = true;
    }

    public void copy(PlayerSize source){
        this.maxScale = source.maxScale;
        this.minScale = source.minScale;
        this.defaultScale = source.defaultScale;
    }

    public void saveNBTData(CompoundTag tag){
        tag.putFloat("maxScale", maxScale);
        tag.putFloat("minScale", minScale);
        tag.putFloat("defaultScale", defaultScale);
    }

    public void loadNBTData(CompoundTag tag){
        maxScale = tag.getFloat("maxScale");
        minScale = tag.getFloat("minScale");
        defaultScale = tag.getFloat("defaultScale");
    }
}
