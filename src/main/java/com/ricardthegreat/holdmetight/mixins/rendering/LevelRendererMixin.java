package com.ricardthegreat.holdmetight.mixins.rendering;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.Vec3;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    
    @Shadow private Frustum cullingFrustum;

   public void prepareCullFrustum(PoseStack p_253986_, Vec3 p_253766_, Matrix4f p_254341_) {
      Matrix4f matrix4f = p_253986_.last().pose();
      double d0 = p_253766_.x();
      double d1 = p_253766_.y();
      double d2 = p_253766_.z();
      this.cullingFrustum = new Frustum(matrix4f, p_254341_);
      this.cullingFrustum.prepare(d0, d1, d2);
   }
}
