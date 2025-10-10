package com.ricardthegreat.holdmetight.mixins.swimming;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.IBlockSwimming;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.fluids.FluidType;

@Mixin(Entity.class)
public abstract class BlockSwimmingMixin implements IBlockSwimming, IForgeEntity{
    @Shadow
    protected Object2DoubleMap<net.minecraftforge.fluids.FluidType> forgeFluidTypeHeight;


    private boolean wasInBlock = false;
    private boolean isInBlock = false;

    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    public void tick(CallbackInfo info) {
        if (isInBlock) {
            isInBlock = false;
            wasInBlock = true;
        }else{
            wasInBlock = false;
        }

        
    }

    @Inject(at = @At("RETURN"), method = "isInWater()Z", cancellable = true)
    public void isInWater(CallbackInfoReturnable<Boolean> info) {
        //System.out.println(wasInBlock);
        if (wasInBlock) {
            info.setReturnValue(true);
        }
    }



    @Override
    public boolean getInSwimmableBlock() {
        return wasInBlock;
    }

    @Override
    public void setIsInSwimmableBlock(boolean block){
        this.isInBlock = block;
    }



    /* 
    @Override
    public net.minecraftforge.fluids.FluidType getMaxHeightFluidType() {
        return this.forgeFluidTypeHeight.object2DoubleEntrySet().stream().max(java.util.Comparator.comparingDouble(Object2DoubleMap.Entry::getDoubleValue)).map(Object2DoubleMap.Entry::getKey).orElseGet(net.minecraftforge.common.ForgeMod.EMPTY_TYPE);
    }
    */

    @Override
    public final double getFluidTypeHeight(FluidType type) {
        if (wasInBlock) {
            return 1d;
        }

        return this.forgeFluidTypeHeight.getDouble(type);
    }
    
}
