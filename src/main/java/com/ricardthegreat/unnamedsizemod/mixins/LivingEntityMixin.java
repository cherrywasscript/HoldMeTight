package com.ricardthegreat.unnamedsizemod.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyReturnValue(method = "onClimbable()Z", at = @At("HEAD"))
    public void onClimbable(CallbackInfoReturnable<Boolean> info) {

        LivingEntity e = (LivingEntity) (Object) this;

        System.out.println(e.blockPosition());

        
   }
    
}
