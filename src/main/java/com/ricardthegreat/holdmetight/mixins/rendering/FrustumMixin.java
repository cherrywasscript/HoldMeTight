package com.ricardthegreat.holdmetight.mixins.rendering;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.ricardthegreat.holdmetight.utils.rendering.FrustumIntersectionCustom;

import net.minecraft.client.renderer.culling.Frustum;

@Mixin(Frustum.class)
public class FrustumMixin{
   
   //@Shadow int OFFSET_STEP;
   @Shadow private final FrustumIntersection intersection = new FrustumIntersectionCustom();
   @Shadow Matrix4f matrix;
   @Shadow Vector4f viewVector;

   @Overwrite
   private void calculateFrustum(Matrix4f p_253909_, Matrix4f p_254521_) {
      p_254521_.mul(p_253909_, this.matrix);

      ((FrustumIntersectionCustom) intersection).set(this.matrix);

      this.viewVector = this.matrix.transformTranspose(new Vector4f(0.0F, 0.0F, 1.0F, 0.0F));
   }
}
