/*
 * file from https://www.youtube.com/watch?v=M5fpFoD26hU
 */

package com.ricardthegreat.holdmetight.Client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class ArmorModel extends EntityModel<LivingEntity> {
    protected static final CubeDeformation NULL_DEFORM = new CubeDeformation(0);
    public final ModelPart armorHead;
    public final ModelPart armorChest;
    public final ModelPart armorRightArm;
    public final ModelPart armorLeftArm;
    public final ModelPart armorRightLeg;
    public final ModelPart armorLeftLeg;
    public final ModelPart armorRightBoot;
    public final ModelPart armorLeftBoot;



    public ArmorModel(ModelPart root) {
        armorHead = getSave("armorHead", root);
        armorChest = getSave("armorBody", root);
        armorRightArm = getSave("armorRightArm", root);
        armorLeftArm = getSave("armorLeftArm", root);
        armorRightLeg = getSave("armorRightLeg", root);
        armorLeftLeg = getSave("armorLeftLeg", root);
        armorRightBoot = getSave("armorRightBoot", root);
        armorLeftBoot = getSave("armorLeftBoot", root);
    }

    private static ModelPart getSave(String name, ModelPart part) {
        try {
            return part.getChild(name);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public void setupAnim(@NotNull LivingEntity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack stack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        armorHead.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorChest.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorRightArm.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorLeftArm.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorRightLeg.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorLeftLeg.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorRightBoot.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
        armorLeftBoot.render(stack, consumer, packedLight, packedOverlay, r, g, b, a);
    }

    public void makeInvisible(boolean invisible) {
        armorHead.visible = !invisible;
        armorChest.visible = !invisible;
        armorRightArm.visible = !invisible;
        armorLeftArm.visible = !invisible;
        armorRightLeg.visible = !invisible;
        armorLeftLeg.visible = !invisible;
        armorRightBoot.visible = !invisible;
        armorLeftBoot.visible = !invisible;
    }
}