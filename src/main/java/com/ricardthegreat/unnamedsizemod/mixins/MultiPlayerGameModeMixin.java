package com.ricardthegreat.unnamedsizemod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.unnamedsizemod.network.PacketHandler;
import com.ricardthegreat.unnamedsizemod.network.SPlayerPutDownPacket;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {

    @Inject(at = @At("HEAD"), method = "performUseItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void performUseItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> info){
        if(player.getItemInHand(hand).isEmpty() && hand != InteractionHand.OFF_HAND){
            Vec3 bp = blockHit.getLocation();
            Entity passenger = player.getFirstPassenger();
            PlayerCarryExtension playerExt = (PlayerCarryExtension) player;
            if (passenger != null && passenger instanceof Player && !playerExt.getShoulderCarry() && !playerExt.getCustomCarry()) {
                PacketHandler.sendToServer(new SPlayerPutDownPacket(passenger.getUUID(), bp));
                info.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
    
    
}
