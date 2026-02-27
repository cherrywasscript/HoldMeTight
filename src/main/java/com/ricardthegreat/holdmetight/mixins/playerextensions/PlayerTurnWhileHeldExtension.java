package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

@Mixin(Player.class)
public abstract class PlayerTurnWhileHeldExtension extends LivingEntity{

    private float vehicleBodyRot = 361;

    protected PlayerTurnWhileHeldExtension(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public void tick() {
        if (PlayerCarryProvider.getPlayerCarryCapability((Player) (Object) this).getTurnWhileCarried()) {
            linkRotation();
        }
        
        super.tick();
    }

    @Override
    protected float tickHeadTurn(float y, float x) {
        /* 
        float f = Mth.wrapDegrees(y - this.yBodyRot);
        this.yBodyRot += f * 0.3F;
        float f1 = Mth.wrapDegrees(this.getYRot() - this.yBodyRot);
        this.yBodyRot -= f * 0.3F;
        if (Math.abs(f1) > 50.0F) {
            for (Entity ent : this.getPassengers()) {
                if (ent instanceof Player player) {
                    player.setYRot(player.getYRot() + (f1 - (float)(Mth.sign((double)f1) * 50)));
                    this.yRotO += f1 - (float)(Mth.sign((double)f1) * 50);
                }
            }
        }   
        */

        return super.tickHeadTurn(y, x);
    }

    private void linkRotation(){
        if (this.getVehicle() instanceof Player vehicle) {
            float wrapped = Mth.wrapDegrees(vehicle.yBodyRot);
            if (vehicleBodyRot == 600) {
                vehicleBodyRot = wrapped;
            }else if (vehicleBodyRot != wrapped) {
                System.out.println("rotated:" + vehicleBodyRot + "/" + wrapped);


                this.setYRot(this.getYRot() + (wrapped - vehicleBodyRot));
                
                vehicleBodyRot = wrapped;
            }
        }else{
            vehicleBodyRot = 600;
        }
    }

}