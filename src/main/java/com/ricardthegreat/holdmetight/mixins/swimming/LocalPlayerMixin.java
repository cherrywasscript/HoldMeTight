package com.ricardthegreat.holdmetight.mixins.swimming;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.extensions.IForgeEntity;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel p_250460_, GameProfile p_249912_) {super(p_250460_, p_249912_);}

    @Shadow public Minecraft minecraft;
    
    @Inject(at = @At("RETURN"), method = "hasEnoughImpulseToStartSprinting()Z", cancellable = true)
    private void hasEnoughImpulseToStartSprinting(CallbackInfoReturnable<Boolean> info) {
    }


    @Shadow
    abstract boolean hasEnoughImpulseToStartSprinting();
    @Shadow
    abstract boolean canStartSprinting();
}
