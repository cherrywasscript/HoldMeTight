package com.ricardthegreat.holdmetight.mixins.sound;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public class EntitySoundMixin {

    @ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), ordinal = 0)
    public float playSound$volume(float volume) {
        /* 
        if (HMTConfig.SERVER_CONFIG.changeSoundVolumeWithScale.get()) {
            float scale = getScaleClamped(HMTConfig.SERVER_CONFIG.getVolumeRange());
            volume *= scale;
        }
        */
        return volume;
    }

    @ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), ordinal = 1)
    public float playSound$pitch(float pitch) {
        if (HMTConfig.SERVER_CONFIG.changeSoundPitchWithScale.get()) {
            float scale = getScaleClamped(HMTConfig.SERVER_CONFIG.getPitchRange());
            pitch /= scale;
        }
        return pitch;
    }

    private float getScaleClamped(Pair<Double, Double> pair){
        float scale = EntitySizeUtils.getSize((Entity) (Object) this);
        scale = Math.min(scale, pair.getRight().floatValue());
        scale = Math.max(scale, pair.getLeft().floatValue());
        return scale;
    }
}
