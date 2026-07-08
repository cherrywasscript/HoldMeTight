package com.ricardthegreat.holdmetight.utils.mobBehaviour;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class MobAvoidLargeEntity<T extends LivingEntity> extends AvoidEntityGoal<T>{

    public MobAvoidLargeEntity(PathfinderMob mob, Class target, float f, double d0, double d1) {
        super(mob, target, (ent) -> {
            if (EntitySizeUtils.getSize(mob) <= EntitySizeUtils.getSize(ent)/4) {
                return true;
            }
            return false;
        }, f, d0, d1, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
    }
}
