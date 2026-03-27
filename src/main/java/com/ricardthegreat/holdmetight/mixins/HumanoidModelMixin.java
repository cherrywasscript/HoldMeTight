package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity>{

    @Shadow
    ModelPart rightArm;
    @Shadow
    ModelPart leftArm;

    @Shadow
    HumanoidModel.ArmPose rightArmPose;
    @Shadow
    HumanoidModel.ArmPose leftArmPose;

    //sets arm position while carrying an entity so it looks natural
    @Inject(at = @At("HEAD"), method = "poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V", cancellable = true)
    private void poseRightArm(T ent, CallbackInfo info){
        if(ent instanceof Player){
            if (ArmPose.values().length >= 11) {
                if (rightArmPose.name().equals("HELD_ENTITY")) {
                    rightArmPose.applyTransform((HumanoidModel) (Object) this, ent, net.minecraft.world.entity.HumanoidArm.RIGHT);
                    info.cancel();
                }
            }  
        }    
    }

    @Inject(at = @At("HEAD"), method = "poseLeftArm(Lnet/minecraft/world/entity/LivingEntity;)V", cancellable = true)
    private void poseLeftArm(T ent, CallbackInfo info){
        if(ent instanceof Player){
            if (ArmPose.values().length >= 11) {
                if (leftArmPose.name().equals("HELD_ENTITY")) {
                    leftArmPose.applyTransform((HumanoidModel) (Object) this, ent, net.minecraft.world.entity.HumanoidArm.LEFT);
                    info.cancel();
                }
            }   
        }      
    }
}
