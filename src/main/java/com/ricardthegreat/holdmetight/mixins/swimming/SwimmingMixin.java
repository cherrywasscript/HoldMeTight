package com.ricardthegreat.holdmetight.mixins.swimming;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.IBlockSwimming;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.fluids.FluidType;
import virtuoel.pehkui.mixin.step_height.IForgeEntityMixin;

@Mixin(IForgeEntity.class)
public abstract interface SwimmingMixin{

    @Overwrite(remap = false)
    default boolean canStartSwimming() {
        if (!this.getEyeInFluidType().isAir() && this.canSwimInFluidType(this.getEyeInFluidType())) {
            return true;
        }else if (this instanceof Player) {
            Entity ent = (Entity) (Object) this;

            IBlockSwimming swimming = (IBlockSwimming) ent;
            
            return swimming.getInSwimmableBlock();
        }else{
            return false;
        }
    }

    @Overwrite(remap = false)
    default boolean canSwimInFluidType(FluidType type) {
        return type.canSwim(self());
    }

    @Shadow
    abstract FluidType getEyeInFluidType();

    @Shadow
    abstract Entity self();
}

