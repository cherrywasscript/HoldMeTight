package com.ricardthegreat.holdmetight.mixins.playerextensions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(Entity.class)
public abstract class EntityCollideMixin{
    

    @Inject(at = @At("HEAD"), method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", cancellable = true)
    private void collide(Vec3 p_20273_, CallbackInfoReturnable<Vec3> info) {
        Entity ent = ((Entity) (Object) this);
        if (ent instanceof Player) {
            //System.out.print(p_20273_ + "/");
            AABB aabb = ent.getBoundingBox();
            List<VoxelShape> list = ent.level().getEntityCollisions(ent, aabb.expandTowards(p_20273_));
            
            boolean check = p_20273_.lengthSqr() == 0.0D;
            //System.out.print(check + "/");
            Vec3 vec3 = p_20273_;
            if (!check) {
                vec3 = Entity.collideBoundingBox(ent, p_20273_, aabb, ent.level(), list);
            }

            //System.out.println(vec3);
        }
    }
}
