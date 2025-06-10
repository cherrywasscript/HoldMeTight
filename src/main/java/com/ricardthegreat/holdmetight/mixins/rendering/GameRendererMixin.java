package com.ricardthegreat.holdmetight.mixins.rendering;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import virtuoel.pehkui.util.ScaleRenderUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    
    //private static int numcalls =0;

    @Shadow @Final Minecraft minecraft;

    private static boolean hasbeenzero = false;

    @Inject(at = @At("RETURN"), method = "getProjectionMatrix(D)Lorg/joml/Matrix4f;", cancellable = true)
    public void getProjectionMatrix(double p_254507_, CallbackInfoReturnable<Matrix4f> info){
        Matrix4f mat = (new Matrix4f()).setPerspective((float)(p_254507_ * (double)((float)Math.PI / 180F)), (float)this.minecraft.getWindow().getWidth() / (float)this.minecraft.getWindow().getHeight(), ScaleRenderUtils.modifyProjectionMatrixDepth((float)p_254507_, minecraft.getCameraEntity(), minecraft.getFrameTime()), this.getDepthFar());
        //numcalls++;
        //System.out.println(mat + "end");

        if (p_254507_ == 0) {
            hasbeenzero = true;
        }

        System.out.println(hasbeenzero + "end");
        //System.out.println((p_254507_ * (double)((float)Math.PI / 180F)) + "/" + ((float)this.minecraft.getWindow().getWidth() / (float)this.minecraft.getWindow().getHeight()) + "end");
        
    }

    @Shadow
    public float getDepthFar(){
        return 0;
    }
}
