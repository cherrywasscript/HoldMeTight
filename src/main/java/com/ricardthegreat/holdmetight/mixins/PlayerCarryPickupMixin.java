package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@Mixin(Player.class)
public abstract class PlayerCarryPickupMixin extends Entity{
    
    public PlayerCarryPickupMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    @ModifyVariable(method = "aiStep()V", at = @At("STORE"), ordinal = 0)
    public AABB aiStep(AABB aabb) {
        if (this.isPassenger() && this.getVehicle() instanceof Player) {
            return this.getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
        }
        return aabb;
    }
}
