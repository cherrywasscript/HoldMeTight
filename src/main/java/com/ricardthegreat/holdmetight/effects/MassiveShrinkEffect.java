package com.ricardthegreat.holdmetight.effects;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MassiveShrinkEffect extends MobEffect{

    public MassiveShrinkEffect(MobEffectCategory category, int colour) {
        super(category, colour);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        double mult = 1-0.05;
        mult += 0.05*amplifier;
        float size = SizeUtils.getSize(livingEntity);
        double target = size*mult;

        SizeUtils.setSizeInstant((Entity) livingEntity, (float) target);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        System.out.println("1st:" + p_19455_ + "/2nd:" + p_19456_);
        return true;
    }

    
}
