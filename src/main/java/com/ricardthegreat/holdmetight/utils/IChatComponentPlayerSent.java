package com.ricardthegreat.holdmetight.utils;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;

public interface IChatComponentPlayerSent {
    void addMessage(Component component, @Nullable MessageSignature signature, @Nullable GuiMessageTag tag, GameProfile profile);
}
