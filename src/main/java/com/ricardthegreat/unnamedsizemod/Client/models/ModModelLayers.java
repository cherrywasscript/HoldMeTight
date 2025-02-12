package com.ricardthegreat.unnamedsizemod.Client.models;

import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation RAY_LAYER = new ModelLayerLocation(
        new ResourceLocation(UnnamedSizeMod.MODID, "ray_gun_projectile"), "main");
}
