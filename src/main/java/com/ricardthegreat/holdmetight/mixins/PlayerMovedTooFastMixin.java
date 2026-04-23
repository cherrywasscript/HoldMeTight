package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.ricardthegreat.holdmetight.HMTConfig;

import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerMovedTooFastMixin {
    @ModifyExpressionValue(method = "handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", order = 500, at = @At(value = "CONSTANT", args = "floatValue=100.0F"))
    private float playerTooFastMaxSpeed(float speed) {
        return HMTConfig.SERVER_CONFIG.maximumMovespeed.get().floatValue();
    }

    @ModifyExpressionValue(method = "handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", order = 500, at = @At(value = "CONSTANT", args = "floatValue=300.0F"))
    private float elytraTooFastMaxSpeed(float speed) {
        return HMTConfig.SERVER_CONFIG.maximumElytraspeed.get().floatValue();
    }

    @ModifyExpressionValue(method = "handleMoveVehicle(Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)V", order = 500, at = @At(value = "CONSTANT", args = "doubleValue=100.0D"))
    private double vehicleTooFastMaxSpeed(double speed) {
        return HMTConfig.SERVER_CONFIG.maximumMovespeed.get();
    }
}
