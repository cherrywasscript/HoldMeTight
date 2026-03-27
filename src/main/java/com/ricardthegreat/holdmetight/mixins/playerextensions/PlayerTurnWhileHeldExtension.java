package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerTurnWhileHeldExtension extends LivingEntity{
    private float vehicleBodyRot = 361;

    protected PlayerTurnWhileHeldExtension(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        if (PlayerCarryProvider.getPlayerCarryCapability((Player) (Object) this).getTurnWhileCarried()) {
            linkRotation();
        }
    }

    private void linkRotation(){
        Player p = (Player) (Object) this;
        if (getVehicle() instanceof Player vehicle) {
            float wrapped = Mth.wrapDegrees(vehicle.yBodyRot);
            if (vehicleBodyRot == 600) {
                vehicleBodyRot = wrapped;
            }else if (vehicleBodyRot != wrapped) {
                p.setYRot(getYRot() + (wrapped - vehicleBodyRot));
                
                vehicleBodyRot = wrapped;
            }
        }else{
            vehicleBodyRot = 600;
        }
    }

}