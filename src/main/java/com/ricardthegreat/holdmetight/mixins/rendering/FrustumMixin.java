package com.ricardthegreat.holdmetight.mixins.rendering;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.ricardthegreat.holdmetight.utils.rendering.FrustumIntersectionCustom;
import com.ricardthegreat.holdmetight.utils.rendering.FrustumMixinInterface;

import net.minecraft.client.renderer.culling.Frustum;

@Mixin(Frustum.class)
public class FrustumMixin{

   private static int maxcount = 0;

   //@Shadow int OFFSET_STEP;
   @Shadow private final FrustumIntersection intersection = new FrustumIntersectionCustom();
   @Shadow Matrix4f matrix;
   @Shadow Vector4f viewVector;
   @Shadow double camX;
   @Shadow double camY;
   @Shadow double camZ;

   private static FrustumIntersectionCustom custom = new FrustumIntersectionCustom();
    
   @Overwrite
   public Frustum offsetToFullyIncludeCameraCube(int p_194442_) {
      // 5.217907075594587/63.00016200001119/-6.78800325630634/(-9.994E-1  3.294E-2  1.299E-2 -6.104E-5)
      //System.out.println(camX + "/" + camY + "/" + camZ + "/" + viewVector + "/" + intersection);
      
      double d0 = Math.floor(this.camX / (double)p_194442_) * (double)p_194442_;
      double d1 = Math.floor(this.camY / (double)p_194442_) * (double)p_194442_;
      double d2 = Math.floor(this.camZ / (double)p_194442_) * (double)p_194442_;
      double d3 = Math.ceil(this.camX / (double)p_194442_) * (double)p_194442_;
      double d4 = Math.ceil(this.camY / (double)p_194442_) * (double)p_194442_;
      //interesction:org.joml.FrustumIntersection@50e31126/camx:5.304954384199016/camy:63.00014041511028/camz:-3.5590285087363784
      //System.out.println("intersection:"+intersection+"/camx:"+this.camX+"/camy:"+this.camY+"/camz:"+this.camZ);

      custom.intersectAab((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)((Math.ceil(this.camZ / (double)p_194442_) * (double)p_194442_) - this.camZ));


      for(double d5 = Math.ceil(this.camZ / (double)p_194442_) * (double)p_194442_; this.intersection.intersectAab((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)(d5 - this.camZ)) != -2; this.camZ -= (double)(this.viewVector.z() * 4.0F)) {
         //System.out.println(this.intersection.intersectAab((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)(d5 - this.camZ)));
         this.camX -= (double)(this.viewVector.x() * 4.0F);
         this.camY -= (double)(this.viewVector.y() * 4.0F);
      }

      return (Frustum) (Object) this;
   }

   @Overwrite
   private void calculateFrustum(Matrix4f p_253909_, Matrix4f p_254521_) {
      p_254521_.mul(p_253909_, this.matrix);
      FrustumIntersectionCustom custom = new FrustumIntersectionCustom();
      //custom.set(this.matrix);
      ((FrustumIntersectionCustom) intersection).set(this.matrix);


      this.viewVector = this.matrix.transformTranspose(new Vector4f(0.0F, 0.0F, 1.0F, 0.0F));
   }





    public Frustum offsetToFullyIncludeCameraCubeOLD(int p_194442_) {
      double d0 = Math.floor(this.camX / (double)p_194442_) * (double)p_194442_;
      double d1 = Math.floor(this.camY / (double)p_194442_) * (double)p_194442_;
      double d2 = Math.floor(this.camZ / (double)p_194442_) * (double)p_194442_;
      double d3 = Math.ceil(this.camX / (double)p_194442_) * (double)p_194442_;
      double d4 = Math.ceil(this.camY / (double)p_194442_) * (double)p_194442_;

      for(double d5 = Math.ceil(this.camZ / (double)p_194442_) * (double)p_194442_; !this.cubeCompletelyInFrustum((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)(d5 - this.camZ)); this.camZ -= (double)(this.viewVector.z() * 4.0F)) {
         this.camX -= (double)(this.viewVector.x() * 4.0F);
         this.camY -= (double)(this.viewVector.y() * 4.0F);
      }

      return (Frustum) (Object) this;
   }

   private boolean cubeCompletelyInFrustum(float p_194444_, float p_194445_, float p_194446_, float p_194447_, float p_194448_, float p_194449_) {
      for(int i = 0; i < 6; ++i) {
         //Vector4f vector4f = this.frustumData[i];
         Vector4f vector4f = new Vector4f();
         if (vector4f.dot(new Vector4f(p_194444_, p_194445_, p_194446_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194447_, p_194445_, p_194446_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194444_, p_194448_, p_194446_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194447_, p_194448_, p_194446_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194444_, p_194445_, p_194449_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194447_, p_194445_, p_194449_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194444_, p_194448_, p_194449_, 1.0F)) <= 0.0F) {
            return false;
         }

         if (vector4f.dot(new Vector4f(p_194447_, p_194448_, p_194449_, 1.0F)) <= 0.0F) {
            return false;
         }
      }

      return true;
   }

   
}
