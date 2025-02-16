package com.ricardthegreat.holdmetight.Client.models;

import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation RAY_LAYER = new ModelLayerLocation(
        new ResourceLocation(HoldMeTight.MODID, "ray_gun_projectile"), "main");
}
