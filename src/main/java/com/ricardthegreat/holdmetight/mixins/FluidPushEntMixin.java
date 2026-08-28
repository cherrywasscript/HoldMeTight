package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public class FluidPushEntMixin {
    
    //@ModifyArg(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3"), index = 0)
    @ModifyVariable(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At("STORE"), ordinal = 1, remap = false)
    public Vec3 updateFluidHeightAndDoFluidPushing(Vec3 vec){
        Entity rep = (Entity) (Object) this;
        if ( rep instanceof Player) {
            System.out.println(vec);
        }

        float size = EntitySizeUtils.getSize(rep);

        if (size >= 4) {
            vec = Vec3.ZERO;
        }else{
            vec = vec.scale(Math.min(1/EntitySizeUtils.getSize(rep), 100)); //i've capped this at 100x just to stop it getting silly
        }
        return vec;
    }
}
