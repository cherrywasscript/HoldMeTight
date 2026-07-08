package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BaseEntityBlock {

    protected DecoratedPotBlockMixin(Properties p_49224_) {super(p_49224_);    }

    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(decoratedPotShape());
        }
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        if (EntitySizeUtils.getSize(collidingEntity) < 0.21) {
            return true;
        }
        return super.collisionExtendsVertically(state, level, pos, collidingEntity);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return decoratedPotShape();
        }else{
            return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.001D, 15.0D);
        }
    }
        
    private boolean shouldChangeShape(CollisionContext context){
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();

            if(ent != null && (EntitySizeUtils.getSize(ent) < 0.21)){
                return true;
            }
        }
        return false;
    }

    private VoxelShape decoratedPotShape(){
        VoxelShape base = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
        VoxelShape top0 = Block.box(4.8d, 16.0d, 4.8d, 11.2d, 17.1d, 11.2d);
        VoxelShape top1 = Block.box(4.1d, 17.1d, 4.1d, 11.9d, 19.9d, 11.9d);
        return Shapes.or(base, Shapes.or(top0, top1));
    }
}

