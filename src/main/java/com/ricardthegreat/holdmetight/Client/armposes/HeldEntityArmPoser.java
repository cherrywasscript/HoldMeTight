package com.ricardthegreat.holdmetight.client.armposes;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.IArmPoseTransformer;

public class HeldEntityArmPoser implements IArmPoseTransformer{
    @Override
    public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        switch (arm) {
            case RIGHT:
                model.rightArm.xRot = -1.4f;
                break;

            case LEFT:
                model.leftArm.xRot = -1.4f;
                break;
        
            default:
                break;
        }
    }
}
