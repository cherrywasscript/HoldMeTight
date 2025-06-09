package com.ricardthegreat.holdmetight.mixins.playerextensions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(LivingEntity.class)
public abstract class EntityClimbingMixin extends Entity{
    
    public EntityClimbingMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow protected boolean jumping;

    private boolean onTop = false;

    //@Inject(at = @At("HEAD"), method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", cancellable = true)
    //@ModifyVariable(at = @At("HEAD"), method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0)
    @Override
    public void move(MoverType mover, Vec3 vec3){
        LivingEntity ent = ((LivingEntity) (Object) this);
        if (onTop && vec3.y < 0) {
            this.resetFallDistance();

            if (ent instanceof Player && this.jumping) {
                Player player = (Player) ent;
                player.jumpFromGround();
            }
        }

        super.move(mover, vec3);
    }

     
    //@Inject(at = @At("RETURN"), method = "getBlockPosBelowThatAffectsMyMovement()Lnet/minecraft/core/BlockPos;", cancellable = true)
    @Override
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        Entity ent = ((Entity) (Object) this);
        if (ent instanceof Player) {
            Player entity = (Player) ent;
            
            List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().expandTowards(0, -1, 0));

            if (!list.isEmpty()) {
                if (onTop(entity, list.get(0))) {
                    System.out.println(entity.level().getBlockState(list.get(0).blockPosition().below()).getBlock().getName());
                    return list.get(0).blockPosition().below();
                }
            }
        }

        return super.getBlockPosBelowThatAffectsMyMovement();
    }
    

    //@Inject(at = @At("HEAD"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V", cancellable = true)
    @ModifyVariable(at = @At("TAIL"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0)
    public Vec3 travel(Vec3 vec3) {
        LivingEntity ent = ((LivingEntity) (Object) this);
        
        if (ent instanceof Player) {
            Player entity = (Player) ent;
            //System.out.println("before:" + entity.getBoundingBox());
            
            //List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().inflate(0.1*EntitySizeUtils.getSize(entity)));
            List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().expandTowards(0, -1, 0));

            if (!list.isEmpty()) {
                if (onTop(entity, list.get(0))) {
                    //System.out.println("list exists");
                    
                    if (entity.getDeltaMovement().y < 0) {
                        entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
                    }
                    if (entity.getPosition(0).y < list.get(0).getBoundingBox().maxY) {
                        entity.setPos(entity.getPosition(0).x, list.get(0).getBoundingBox().maxY,entity.getPosition(0).z);    
                    }
                    
                    //entity.hurtMarked = true;
                    entity.setOnGround(true);
                    //entity.verticalCollisionBelow = true;
                    
                    //entity.hurtMarked = true;
                    //entity.getAbilities().flying = false;
                                      
                    //System.out.println(entity.isControlledByLocalInstance());
                    
                    onTop = true;
                }else{
                    onTop = false;
                }
            }else{
                onTop = false;
            }
            //System.out.println("after:" + entity.getBoundingBox());
        }
        return vec3;
    }

    private boolean onTop(LivingEntity player, Entity ent){
        AABB bb = ent.getBoundingBox();
        Vec3 pos = player.getPosition(0);

        boolean inX = pos.x >= bb.minX && pos.x <= bb.maxX;
        boolean inZ = pos.z >= bb.minZ && pos.z <= bb.maxZ;

        //boolean onY = pos.y >= bb.minY && pos.y <= bb.maxY + (0.1f * EntitySizeUtils.getSize(ent));
        boolean onY = pos.y >= bb.maxY - (0.1f * EntitySizeUtils.getSize(ent)) && pos.y <= bb.maxY ;
        
        return inX && inZ && onY;
    }

    //@Shadow protected abstract void doPush(Entity p_20971_);
}
