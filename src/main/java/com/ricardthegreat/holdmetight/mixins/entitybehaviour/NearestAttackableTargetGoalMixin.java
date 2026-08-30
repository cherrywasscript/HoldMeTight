package com.ricardthegreat.holdmetight.mixins.entitybehaviour;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal{
    public NearestAttackableTargetGoalMixin(Mob p_26140_, boolean p_26141_) {
        super(p_26140_, p_26141_);
    }

    @Shadow protected TargetingConditions targetConditions;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At("TAIL"), cancellable = true)
    public void NearestAttackableTargetGoal(Mob mob, Class<T> cls, int i, boolean bool0, boolean bool1, @Nullable Predicate<LivingEntity> predicate, CallbackInfo info) {
        if ((cls == Player.class || cls == ServerPlayer.class)) {
            Predicate<LivingEntity> test = predicate;
            predicate = (ent) -> {
                    if (EntitySizeUtils.getSize(mob) <= EntitySizeUtils.getSize(ent)/4) {
                        return false;
                    }
                    if (test == null) {
                        return true;
                    }
                    return test.test(ent);
            };

            this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(predicate);
        }
    }
}
