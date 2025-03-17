package com.ricardthegreat.holdmetight.effects;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GrowEffect extends MobEffect{

    public GrowEffect(MobEffectCategory category, int colour) {
        super(category, colour);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        //this works out to make the entity approximately 2/3 height after 1min
        double mult = 1+((0.00034)*(amplifier+1));
        float size = SizeUtils.getSize(livingEntity);
        double target = size*mult;

        SizeUtils.setSizeInstant((Entity) livingEntity, (float) target);
    }
    
    @Override
    public boolean isDurationEffectTick(int remainingTicks, int amplifier) {
        return true;
    }
    
}
