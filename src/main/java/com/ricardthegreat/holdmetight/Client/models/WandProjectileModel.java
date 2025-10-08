package com.ricardthegreat.holdmetight.client.models;

import javax.annotation.Nonnull;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WandProjectileModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart laser;

    public WandProjectileModel(ModelPart root) {
        this.laser = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        int i = 2;
        partdefinition.addOrReplaceChild("main",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 8.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(@Nonnull T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
    }

    @Override
    public ModelPart root() {
        return laser;
    }

}
