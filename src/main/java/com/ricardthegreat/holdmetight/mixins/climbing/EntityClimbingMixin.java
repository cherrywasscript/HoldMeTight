package com.ricardthegreat.holdmetight.mixins.climbing;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import oshi.driver.mac.net.NetStat.IFdata;

@Mixin(Entity.class)
public class EntityClimbingMixin {

    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    public void tick(CallbackInfo info) {
        Entity ent = (Entity) (Object) this;
        if (ent instanceof Player && holdingClimbingItem((Player) ent)) {
            Player player = (Player) ent;
            double sizeDif = PlayerSizeUtils.getSize(player) / EntitySizeUtils.getSize(ent);
            if (sizeDif <= 0.5) {
                shuntIfInsideHitbox(player);
            }
        }
    }
    
    @Inject(at = @At("RETURN"), method = "canBeCollidedWith()Z", cancellable = true)
    public void canBeCollidedWith(CallbackInfoReturnable<Boolean> info) {
        //return true;
    }

    @Inject(at = @At("RETURN"), method = "canCollideWith(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    public void canCollideWith(Entity entity, CallbackInfoReturnable<Boolean> info) {
        Entity thisEnt = (Entity) (Object) this;
        
        if (thisEnt instanceof Player) {
            Player player = (Player) thisEnt;
            double sizeDif = PlayerSizeUtils.getSize(player) / EntitySizeUtils.getSize(entity);
            if (holdingClimbingItem(player) && sizeDif <= 0.5) {
                info.setReturnValue(true);
            }
        }
    }


    private boolean holdingClimbingItem(Player player){
        if ((player.getMainHandItem().is(Items.SLIME_BALL) || player.getOffhandItem().is(Items.SLIME_BALL))) {
            return true;
        }

        return false;
    }

    private void shuntIfInsideHitbox(LivingEntity player){
        List<Entity> list = player.level().getEntities(player, player.getBoundingBox());
        
        if (!list.isEmpty()) {
            // if ent similar size then ignore
            for(Entity ent : list){
                if (player.getBoundingBox().intersects(ent.getBoundingBox())) {
                    if (upperHalfHitbox(player, ent)) {
                        player.setPos(player.position().x, ent.getBoundingBox().maxY, player.position().z);
                    }else{
                        Vec3 centreDistance = player.position().vectorTo(ent.position());
                        if (Math.abs(centreDistance.x) > Math.abs(centreDistance.z)) {
                            double x = findXPos(player, ent);
                            player.setPos(x, player.position().y, player.position().z);
                        }else{
                            double z = findZPos(player, ent);
                            player.setPos(player.position().x, player.position().y, z);
                        }
                    }
                }
            }
        }
    }

    private double findXPos(LivingEntity player, Entity ent){
        double entXPos = ent.position().x();
        double playerXPos = player.position().x();
        AABB entBB = ent.getBoundingBox();
        AABB playerBB = player.getBoundingBox();

        if (playerXPos > entXPos) {
            return (entBB.maxX + playerBB.maxX - playerBB.minX)/2;
        }else{
            return entBB.minX - (playerBB.maxX - playerBB.minX)/2;
        }
    }

    private double findZPos(LivingEntity player, Entity ent){
        double entZPos = ent.position().z();
        double playerZPos = player.position().z();
        AABB entBB = ent.getBoundingBox();
        AABB playerBB = player.getBoundingBox();

        if (playerZPos > entZPos) {
            return (entBB.maxZ + playerBB.maxZ - playerBB.minZ)/2;
        }else{
            return entBB.minZ - (playerBB.maxZ - playerBB.minZ)/2;
        }
    }

    private boolean upperHalfHitbox(LivingEntity player, Entity entity){
        double half = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY)/2;
        return player.position().y() > half;
    }
}

