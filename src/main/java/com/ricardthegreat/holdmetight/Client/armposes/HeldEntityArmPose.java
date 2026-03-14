package com.ricardthegreat.holdmetight.client.armposes;

import net.minecraft.client.model.HumanoidModel;

public class HeldEntityArmPose{
    public static final HumanoidModel.ArmPose HELD_ENTITY_POSE = HumanoidModel.ArmPose.create("HELD_ENTITY", false, new HeldEntityArmPoser());
}
