package com.ricardthegreat.holdmetight.mixins.entitybehaviour;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.ricardthegreat.holdmetight.utils.mobBehaviour.CatAvoidPlayerExtension;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cat.CatAvoidEntityGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

@Mixin(AvoidEntityGoal.class)
public class MobAvoidPlayerMixin<T extends LivingEntity> {
	//mobs do not avoid players that are less than 1/2 their height
	@ModifyVariable(method = "<init>(Lnet/minecraft/world/entity/PathfinderMob;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V", at = @At("HEAD"), ordinal = 1)
	private static Predicate<LivingEntity> AvoidEntityGoal(Predicate<LivingEntity> predicate, @Local PathfinderMob mob) {
		Predicate<LivingEntity> tmp = (ent) -> {
			if (ent instanceof Player player) {
				boolean bool = EntitySizeUtils.getSize(mob) <= EntitySizeUtils.getSize(player)*2;
				System.out.println(EntitySizeUtils.getSize(mob) + "/" + EntitySizeUtils.getSize(player) + "/" + predicate.test(player) + "/" + bool);
				return predicate.test(ent) && bool;
			}else{
				return predicate.test(ent);
			}};
		return tmp;
   	}
}
