package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @Inject(at = @At("HEAD"), method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo info) {
        if (EntitySizeUtils.getSize(entity) >= 4) {
            System.out.println(entity.getName());
            info.cancel();
        }
    }
    
    @Inject(at = @At("HEAD"), method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    private static void canEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (EntitySizeUtils.getSize(entity) <= 1f/16f) {
            info.setReturnValue(true);
        }
    }
}
