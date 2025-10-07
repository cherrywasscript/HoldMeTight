package com.ricardthegreat.holdmetight.size;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.network.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SPlayerSizeMixinSyncPacket;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class PlayerSize {

    //the max and min size this player can be as well as their default size
    //default size is what it will be set to when "reset" is used on size devices
    //max size is default 50k because i think the game will break far before then so its effectively infinite
    private float maxScale = 50000;
    private float minScale = 0;
    private float defaultScale = 1;

    //private ScaleData data = ((PehkuiEntityExtensions) (Player) (Object) this).pehkui_getScaleData(ScaleTypes.BASE);
    private boolean shouldSync = false;

    public void tick(Player player){
        if (shouldSync) {
            sync(player);
        }
    }
    
    private void sync(Player player){
        shouldSync = false;
        if (player.level().isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToServer(new SPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, player.getUUID())));
        }else {
            if (!player.getServer().isDedicatedServer()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> 
                PacketHandler.sendToAllClients(new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, player.getUUID())));
            }else{
                DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                PacketHandler.sendToAllClients(new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, player.getUUID())));
            }
        }
    }

    public void updateSyncables(float maxScale, float minScale, float defaultScale){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
    }

    public CPlayerSizeMixinSyncPacket getSyncPacket(Player player){
        return new CPlayerSizeMixinSyncPacket(maxScale, minScale, defaultScale, player.getUUID());
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(Float maxScale) {
        if (maxScale < minScale) {
            maxScale = minScale;
        }
        if (maxScale < defaultScale) {
            defaultScale = maxScale;
        }
        this.maxScale = maxScale;
    }

    public float getMinScale() {
        return minScale;
    }

    public void setMinScale(Float minScale) {
        if (minScale > maxScale) {
            minScale = maxScale;
        }
        if (minScale > defaultScale) {
            defaultScale = minScale;
        }
        this.minScale = minScale;
    }

    public float getDefaultScale(){
        return defaultScale;
    }

    public void setDefaultScale(Float defaultScale){
        if (defaultScale > maxScale) {
            this.defaultScale = maxScale;
        }else if (defaultScale < minScale) {
            this.defaultScale = minScale;
        }else{
            this.defaultScale = defaultScale;
        }  
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
