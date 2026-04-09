package com.ricardthegreat.holdmetight.mixins.sound;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;
import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

@Mixin(LocalPlayer.class)
public class LocalPlayerSoundMixin {

    private final List<SoundEvent> events = List.of(
        SoundEvents.GOAT_SCREAMING_MILK, 
        SoundEvents.GOAT_MILK,
        SoundEvents.COW_MILK, 
        SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE, 
        SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE,
        SoundEvents.BUNDLE_REMOVE_ONE,
        SoundEvents.BUNDLE_INSERT,
        SoundEvents.BUNDLE_DROP_CONTENTS);
    
    @ModifyVariable(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), ordinal = 1)
    public float playSound$pitch(float pitch, @Local SoundEvent event) {
        if (HMTConfig.SERVER_CONFIG.changeSoundPitchWithScale.get()) {
            boolean doChange = true;
            for (SoundEvent soundEvent : events) {
                if (event.getLocation().equals(soundEvent.getLocation())) {
                    doChange = false;
                }
            }
            if (doChange) {
                float scale = getScaleClamped(HMTConfig.SERVER_CONFIG.getPitchRange());
                pitch /= scale;
            }
        }
        return pitch;
    }

    private float getScaleClamped(Pair<Double, Double> pair){
        float scale = EntitySizeUtils.getSize((LocalPlayer) (Object) this);
        scale = Math.min(scale, pair.getRight().floatValue());
        scale = Math.max(scale, pair.getLeft().floatValue());
        return scale;
    }
}
