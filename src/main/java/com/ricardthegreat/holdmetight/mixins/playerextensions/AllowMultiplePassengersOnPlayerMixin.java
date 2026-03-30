package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class AllowMultiplePassengersOnPlayerMixin extends Entity{

    public AllowMultiplePassengersOnPlayerMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);    
    }

    @Override
    protected boolean canAddPassenger(Entity p_20354_) {
        return true;
    }
}
