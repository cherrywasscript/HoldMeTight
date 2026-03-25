package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.ricardthegreat.holdmetight.HMTConfig;

import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class PlayerMovedTooFastMixin {
    @ModifyConstant(method = "handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", constant = @Constant(floatValue = 100.0F))
    private float playerTooFastMaxSpeed(float speed) {
        return HMTConfig.SERVER_CONFIG.maximumMovespeed.get().floatValue();
    }

    @ModifyConstant(method = "handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", constant = @Constant(floatValue = 300.0F))
    private float elytraTooFastMaxSpeed(float speed) {
        return HMTConfig.SERVER_CONFIG.maximumElytraspeed.get().floatValue();
    }

    @ModifyConstant(method = "handleMoveVehicle(Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)V", constant = @Constant(doubleValue  = 100.0F))
    private double vehicleTooFastMaxSpeed(double speed) {
        return HMTConfig.SERVER_CONFIG.maximumMovespeed.get();
    }
}
