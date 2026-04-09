package com.ricardthegreat.holdmetight.mixins.sizeinteractions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Mob;

@Mixin(Mob.class)
public class AdjustMobKnockback {
    
    @ModifyArg(method = "doHurtTarget(Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double modifyStrength(double strength){
        Mob entity = (Mob) (Object) this;
        float scale = EntitySizeUtils.getSize(entity);
        scale = (float) Math.pow(scale, 0.6);
        return strength*scale;
    }
}
