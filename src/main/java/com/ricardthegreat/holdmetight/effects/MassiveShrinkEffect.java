package com.ricardthegreat.holdmetight.effects;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

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
        float size = EntitySizeUtils.getSize(livingEntity);
        double target = size*mult;

        EntitySizeUtils.setSize((Entity) livingEntity, (float) target, 0);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    
}
