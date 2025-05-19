package com.ricardthegreat.holdmetight.mixins;

import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.culling.Frustum;

@Mixin(Frustum.class)
public class FrustumMixin {

    private static int maxcount = 0;

    //@Shadow int OFFSET_STEP;
    @Shadow FrustumIntersection intersection;
    @Shadow Matrix4f matrix;
    @Shadow Vector4f viewVector;
    @Shadow double camX;
    @Shadow double camY;
    @Shadow double camZ;
    
    @Overwrite
    public Frustum offsetToFullyIncludeCameraCube(int p_194442_) {
        double d0 = Math.floor(this.camX / (double)p_194442_) * (double)p_194442_;
        double d1 = Math.floor(this.camY / (double)p_194442_) * (double)p_194442_;
        double d2 = Math.floor(this.camZ / (double)p_194442_) * (double)p_194442_;
        double d3 = Math.ceil(this.camX / (double)p_194442_) * (double)p_194442_;
        double d4 = Math.ceil(this.camY / (double)p_194442_) * (double)p_194442_;
        //interesction:org.joml.FrustumIntersection@50e31126/camx:5.304954384199016/camy:63.00014041511028/camz:-3.5590285087363784
        //System.out.println("intersection:"+intersection+"/camx:"+this.camX+"/camy:"+this.camY+"/camz:"+this.camZ);

        for(double d5 = Math.ceil(this.camZ / (double)p_194442_) * (double)p_194442_; this.intersection.intersectAab((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)(d5 - this.camZ)) != -2; this.camZ -= (double)(this.viewVector.z() * 4.0F)) {
            //System.out.println(this.intersection.intersectAab((float)(d0 - this.camX), (float)(d1 - this.camY), (float)(d2 - this.camZ), (float)(d3 - this.camX), (float)(d4 - this.camY), (float)(d5 - this.camZ)));
            this.camX -= (double)(this.viewVector.x() * 4.0F);
            this.camY -= (double)(this.viewVector.y() * 4.0F);
        }

        return (Frustum) (Object) this;
    }
}
