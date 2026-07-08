package com.ricardthegreat.holdmetight.mixins.collisions;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin extends Block{

    public ChorusFlowerBlockMixin(Properties p_49795_) {super(p_49795_);    }
    
    //adds a hitbox for those under 0.1
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        //block with the bounds of a pressure plate
        if (shouldChangeShape(state, context)) {
            return flowerBlockShape();
        }
        return super.getCollisionShape(state, getter, pos, context);
    }
    
    private boolean shouldChangeShape(BlockState state, CollisionContext context){
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && (EntitySizeUtils.getSize(ent) < 0.21)){
                return true;
            }
        }
        return false;
    }

    private VoxelShape flowerBlockShape(){
        VoxelShape core = Block.box(2, 0, 2, 14, 16, 14);
        VoxelShape side0 = Block.box(2, 2, 0, 14, 14, 2);
        VoxelShape side1 = Block.box(2, 2, 14, 14, 14, 16);
        VoxelShape side2 = Block.box(0, 2, 2, 2, 14, 14);
        VoxelShape side3 = Block.box(14, 2, 2, 16, 14, 14);

        VoxelShape s01 = Shapes.or(side0, side1);
        VoxelShape s23 = Shapes.or(side2, side3);

        VoxelShape s0123 = Shapes.or(s01, s23);

        return Shapes.or(core, s0123);
    }
}
