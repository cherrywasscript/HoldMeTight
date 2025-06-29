package com.ricardthegreat.holdmetight.mixins.swimming;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.fluids.FluidType;
import virtuoel.pehkui.mixin.step_height.IForgeEntityMixin;

@Mixin(IForgeEntity.class)
public abstract interface SwimmingMixin{

    @Overwrite(remap = false)
    default boolean canStartSwimming(){
        System.out.println("test");

        return !this.getEyeInFluidType().isAir() && this.canSwimInFluidType(this.getEyeInFluidType());
    }



    @Shadow
    abstract FluidType getEyeInFluidType();

    @Shadow
    abstract boolean canSwimInFluidType(FluidType type);
}

