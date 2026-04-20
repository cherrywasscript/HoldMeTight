package com.ricardthegreat.holdmetight.utils.rendering;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenPlayer extends AbstractClientPlayer{

    private final UUID linkedID;
    private PlayerInfo playerInfo;

    public ScreenPlayer(ClientLevel p_250460_, GameProfile p_249912_, UUID linkedID) {
        super(p_250460_, p_249912_);
        this.linkedID = linkedID;
    }
    
    @Override
    @Nullable
    protected PlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(this.linkedID);
        }

        return this.playerInfo;
    }
}
