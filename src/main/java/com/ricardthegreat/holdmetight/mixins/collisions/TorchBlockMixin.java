package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(TorchBlock.class)
public class TorchBlockMixin extends Block{
    
    public TorchBlockMixin(Properties p_49795_) {
            super(p_49795_);
    }
    
    @SuppressWarnings("null")
    @Override
    public VoxelShape getCollisionShape(BlockState p_52357_, BlockGetter p_52358_, BlockPos p_52359_, CollisionContext context) {
        //block with the bounds of a torch
        VoxelShape voxelshape = Block.box(7, 0.0D, 7, 9, 10, 9);

        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && SizeUtils.getSize(ent) < 0.21){
                System.out.println(voxelshape);
                return voxelshape;
            }
        }
        
        return Shapes.empty();
    }
}
