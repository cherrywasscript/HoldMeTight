package com.ricardthegreat.holdmetight.effects;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

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
        float size = EntitySizeUtils.getSize(livingEntity);
        double target = size*mult;

        EntitySizeUtils.setSize((Entity) livingEntity, (float) target, 0);
    }
    
    @Override
    public boolean isDurationEffectTick(int remainingTicks, int amplifier) {
        return true;
    }
    
}
