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
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block {

    protected FlowerPotBlockMixin(Properties p_49224_) {super(p_49224_);}

    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(flowerPotShape());
        }
    }
     
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return flowerPotShape();
        }
        return super.getCollisionShape(state, getter, pos, context);
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

    private VoxelShape flowerPotShape(){
        VoxelShape base = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
        VoxelShape wall0 = Block.box(5.0D, 4.0D, 5.0D, 6.0D, 6.0D, 11.0D);
        VoxelShape wall1 = Block.box(10.0D, 4.0D, 5.0D, 11.0D, 6.0D, 11.0D);
        VoxelShape wall2 = Block.box(5.0D, 4.0D, 5.0D, 11.0D, 6.0D, 6.0D);
        VoxelShape wall3 = Block.box(5.0D, 4.0D, 10.0D, 11.0D, 6.0D, 11.0D);
        
        return Shapes.or(base, Shapes.or(Shapes.or(wall0, wall1), Shapes.or(wall2, wall3)));
    }
}

