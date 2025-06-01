package com.ricardthegreat.holdmetight.mixins;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Tilt;

@Mixin(BigDripleafBlock.class)
public class BigDripleafBlockMixin {

    @Shadow private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;

    @Inject(at = @At("HEAD"), method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo info) {
        if (!level.isClientSide) {
            if (state.getValue(TILT) == Tilt.NONE && canEntityTilt(pos, entity) && !level.hasNeighborSignal(pos)) {
                if (EntitySizeUtils.getSize(entity) <= 0.0625f) {
                    info.cancel();
                }else if (EntitySizeUtils.getSize(entity) >= 2) {
                    this.setTiltAndScheduleTick(state, level, pos, Tilt.PARTIAL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
                }
            }
        }
    }

    @Shadow 
    private void setTiltAndScheduleTick(BlockState p_152283_, Level p_152284_, BlockPos p_152285_, Tilt p_152286_, @Nullable SoundEvent p_152287_){
    }

    @Shadow
    private static boolean canEntityTilt(BlockPos p_152302_, Entity p_152303_) {
        return false;
    }
}
