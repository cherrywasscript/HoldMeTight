package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleRenderUtils;

@Mixin(ScaleRenderUtils.class)
public class ScaleRendererUtilsMixin {
    

    @Inject(at = @At("RETURN"), method = "modifyProjectionMatrixDepth(FFLnet/minecraft/world/entity/Entity;F)F", remap = false)
    private static void modifyProjectionMatrixDepth(float scale, float depth, Entity entity, float tickDelta, CallbackInfoReturnable<Float> info){
        //System.out.println(Math.max(depth * scale, (float) PehkuiConfig.CLIENT.minimumCameraDepth.get().doubleValue()));
    }

}
