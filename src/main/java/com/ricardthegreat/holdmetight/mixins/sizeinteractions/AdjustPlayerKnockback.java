package com.ricardthegreat.holdmetight.mixins.sizeinteractions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class AdjustPlayerKnockback {
    
    // while a player is sprinting the knockback calculation is called from their class and not from the class of the entity they hit, idfk why but it is what it is
    @ModifyArg(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double modifyStrength$sprint(double strength){
        Player player = (Player) (Object) this;
        float scale = EntitySizeUtils.getSize(player);
        scale = (float) Math.pow(scale, 0.6);
        return strength;
    }
}
