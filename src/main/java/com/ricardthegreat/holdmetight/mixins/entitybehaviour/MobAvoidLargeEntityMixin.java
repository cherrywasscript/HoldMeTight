package com.ricardthegreat.holdmetight.mixins.entitybehaviour;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.utils.mobBehaviour.MobAvoidLargeEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.level.Level;

@Mixin(Mob.class)
public abstract class MobAvoidLargeEntityMixin{
    @Shadow GoalSelector goalSelector;

    //injecting into the end of the mob constructor to hopefully register it as a goal for all monsters
    @Inject(method = "<init>", at = @At("TAIL"), cancellable = true)
    protected void mob(EntityType<? extends Mob> ent, Level level, CallbackInfo info){
        if (level != null && !level.isClientSide) {
            Mob mobRep = (Mob) (Object) this;
            if (mobRep instanceof Monster monster) {
                this.goalSelector.addGoal(1, new MobAvoidLargeEntity<>(monster, LivingEntity.class, 6.0F, 1.0D, 1.2D));
            }
        }
    }
}
