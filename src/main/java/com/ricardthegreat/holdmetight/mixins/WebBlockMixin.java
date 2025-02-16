package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(WebBlock.class)
public class WebBlockMixin {
    @Overwrite
    public void entityInside(BlockState p_58180_, Level p_58181_, BlockPos p_58182_, Entity entity) {
        if(SizeUtils.getSize(entity) < 2 && SizeUtils.getSize(entity) > 1/16){
            entity.makeStuckInBlock(p_58180_, new Vec3(0.25D, (double)0.05F, 0.25D));
        }
    }
}
