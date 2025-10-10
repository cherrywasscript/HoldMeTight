package com.ricardthegreat.holdmetight.client.renderers;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.client.models.ModModelLayers;
import com.ricardthegreat.holdmetight.client.models.RayGunProjectileModel;
import com.ricardthegreat.holdmetight.entities.projectile.RayGunProjectile;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RayGunProjectileRenderer extends EntityRenderer<RayGunProjectile> {

    private static final ResourceLocation RAY_PROJECTILE_LOCATION = new ResourceLocation(HoldMeTight.MODID, "textures/entity/ray_gun_projectile.png");
    private final RayGunProjectileModel<RayGunProjectile> model;

    public RayGunProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RayGunProjectileModel<>(context.bakeLayer(ModModelLayers.RAY_LAYER));
    }

    public void render(@Nonnull RayGunProjectile p_115373_, float p_115374_, float p_115375_, @Nonnull PoseStack p_115376_,
            @Nonnull MultiBufferSource p_115377_, int p_115378_) {
        p_115376_.pushPose();
        p_115376_.translate(0.0F, 0.15F, 0.0F);
        p_115376_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_115375_, p_115373_.yRotO, p_115373_.getYRot()) - 90.0F));
        p_115376_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_115375_, p_115373_.xRotO, p_115373_.getXRot())));
        this.model.setupAnim(p_115373_, p_115375_, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = p_115377_.getBuffer(this.model.renderType(RAY_PROJECTILE_LOCATION));
        this.model.renderToBuffer(p_115376_, vertexconsumer, p_115378_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                1.0F);
        p_115376_.popPose();
        super.render(p_115373_, p_115374_, p_115375_, p_115376_, p_115377_, p_115378_);
    }

    @Override
    public ResourceLocation getTextureLocation(@Nonnull RayGunProjectile p_115371_) {
        return RAY_PROJECTILE_LOCATION;
    }
}
