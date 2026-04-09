package com.ricardthegreat.holdmetight.mixins.sound;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class PlayerSoundMixin {

    @ModifyArg(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 7)
    public float playEatSound$pitch(float pitch) {
        if (HMTConfig.SERVER_CONFIG.changeSoundPitchWithScale.get()) {
            float scale = getScaleClamped(HMTConfig.SERVER_CONFIG.getPitchRange());
            pitch /= scale;
        }
        return pitch;
    }

    @ModifyArg(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), index = 7)
    public float playAttackSound$pitch0(float pitch) {
        return modifyAttackPitch(pitch);
    }

    private float modifyAttackPitch(float pitch){
        if (HMTConfig.SERVER_CONFIG.changeSoundPitchWithScale.get()) {
            float scale = getScaleClamped(HMTConfig.SERVER_CONFIG.getPitchRange());
            pitch /= scale;
        }
        return pitch;
    }


    @ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), ordinal = 1)
    public float playSound$pitch(float pitch) {
        System.out.println("playerpitch:" + pitch);
        if (HMTConfig.SERVER_CONFIG.changeSoundPitchWithScale.get()) {
            System.out.println("modifying");
            float scale = getScaleClamped(HMTConfig.SERVER_CONFIG.getPitchRange());
            pitch /= scale;
            System.out.println("playerpitchmod:" + pitch);
        }
        return pitch;
    }

    private float getScaleClamped(Pair<Double, Double> pair){
        float scale = EntitySizeUtils.getSize((Player) (Object) this);
        scale = Math.min(scale, pair.getRight().floatValue());
        scale = Math.max(scale, pair.getLeft().floatValue());
        return scale;
    }
}
