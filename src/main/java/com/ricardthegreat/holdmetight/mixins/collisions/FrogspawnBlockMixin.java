package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FrogspawnBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(FrogspawnBlock.class)
public abstract class FrogspawnBlockMixin extends Block{

    protected FrogspawnBlockMixin(Properties p_53205_) {super(p_53205_);}
    
    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(frogspawnShape());
        }
    }
     
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return frogspawnShape();
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

    private VoxelShape frogspawnShape(){
        VoxelShape egg0 = Block.box(0.0D, 0.2D, 3.0D, 3.0D, 0.25D, 6.0D);
        VoxelShape egg1 = Block.box(1.0D, 0.2D, 12.0D, 4.0D, 0.25D, 15.0D);
        VoxelShape egg2 = Block.box(3.0D, 0.2D, 7.0D, 6.0D, 0.25D, 10.0D);
        VoxelShape egg3 = Block.box(5.0D, 0.2D, 0.0D, 8.0D, 0.25D, 3.0D);
        VoxelShape egg4 = Block.box(6.0D, 0.2D, 11.0D, 9.0D, 0.25D, 14.0D);
        VoxelShape egg5 = Block.box(9.0D, 0.2D, 6.0D, 12.0D, 0.25D, 9.0D);
        VoxelShape egg6 = Block.box(10.0D, 0.2D, 13.0D, 13.0D, 0.25D, 16.0D);
        VoxelShape egg7 = Block.box(11.0D, 0.2D, 2.0D, 14.0D, 0.25D, 5.0D);
        VoxelShape egg8 = Block.box(13.0D, 0.2D, 9.0D, 16.0D, 0.25D, 12.0D);

        VoxelShape e01 = Shapes.or(egg0, egg1);
        VoxelShape e23 = Shapes.or(egg2, egg3);
        VoxelShape e45 = Shapes.or(egg4, egg5);
        VoxelShape e67 = Shapes.or(egg6, egg7);

        VoxelShape e0123 = Shapes.or(e01, e23);
        VoxelShape e4567 = Shapes.or(e45, e67);

        VoxelShape e01234567 = Shapes.or(e0123, e4567);
        return Shapes.or(e01234567, egg8);
    }
}
