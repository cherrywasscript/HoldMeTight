package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity>{

    @Shadow
    ModelPart rightArm;

    @Shadow
    HumanoidModel.ArmPose rightArmPose;

    //sets arm position while carrying an entity so it looks natural
    @Inject(at = @At("RETURN"), method = "poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V")
    //@Overwrite
    private void poseRightArm(T ent, CallbackInfo info){
        if(ent instanceof Player){
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) ent);
            if(playerCarry.getIsCarrying() && playerCarry.getCarryPosition().posName == "hand"){
                //rightArm.xRot = rightArm.xRot-1.4f;
                //System.out.println(rightArm.xRot);
                rightArm.xRot = -1.4f;
            } 
        }
    }
}
