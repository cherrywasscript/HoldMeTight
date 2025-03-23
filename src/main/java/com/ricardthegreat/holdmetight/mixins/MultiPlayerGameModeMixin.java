package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.init.BlockInit;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SPlayerPutDownPacket;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
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
            
            if (player.level().getBlockState(blockHit.getBlockPos()).is(Blocks.AIR)) {
                HoldMeTight.LOGGER.info("MultiPlayerGameModeMixin.java line 35: block is air");
                System.out.println("block is air");
            }
            
            if (player.level().getBlockState(blockHit.getBlockPos()).is(BlockInit.TINY_JAR.get())) {
                return;
            }

            if (passenger != null && passenger instanceof Player && !playerExt.getShoulderCarry() && !playerExt.getCustomCarry()) {
                PacketHandler.sendToServer(new SPlayerPutDownPacket(passenger.getUUID(), bp));
                info.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
    
    
}
