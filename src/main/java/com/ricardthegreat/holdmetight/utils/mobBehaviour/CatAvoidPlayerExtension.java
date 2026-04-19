package com.ricardthegreat.holdmetight.utils.mobBehaviour;

import java.util.function.Predicate;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cat.CatAvoidEntityGoal;
import net.minecraft.world.entity.player.Player;

public class CatAvoidPlayerExtension<T extends LivingEntity> extends AvoidEntityGoal<T>{

    private final Cat cat;

    public CatAvoidPlayerExtension(Cat cat, Class<T> classType, float f, double d0, double d1) {
        super(cat, classType, f, d0, d1, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.cat = cat;
    }

    public boolean canUse() {
        return !this.cat.isTame() && super.canUse();
    }

    public boolean canContinueToUse() {
        return !this.cat.isTame() && super.canContinueToUse();
    }
}
