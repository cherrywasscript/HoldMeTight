package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(DragonEggBlock.class)
public abstract class DragonEggBlockMixin extends FallingBlock{

    protected DragonEggBlockMixin(Properties p_53205_) {super(p_53205_);}
    
    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(dragonEggShape());
        }
    }
     
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return dragonEggShape();
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

    private VoxelShape dragonEggShape(){
        VoxelShape layer0 = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
        VoxelShape layer1 = Block.box(2.0D, 1.0D, 2.0D, 14.0D, 3.0D, 14.0D);
        VoxelShape layer2 = Block.box(1.0D, 3.0D, 1.0D, 15.0D, 8.0D, 15.0D);
        VoxelShape layer3 = Block.box(2.0D, 8.0D, 2.0D, 14.0D, 11.0D, 14.0D);
        VoxelShape layer4 = Block.box(3.0D, 11.0D, 3.0D, 13.0D, 13.0D, 13.0D);
        VoxelShape layer5 = Block.box(5.0D, 13.0D, 5.0D, 11.0D, 15.0D, 11.0D);
        VoxelShape layer6 = Block.box(6.0D, 15.0D, 6.0D, 10.0D, 16.0D, 10.0D);

        VoxelShape l01 = Shapes.or(layer0, layer1);
        VoxelShape l23 = Shapes.or(layer2, layer3);
        VoxelShape l45 = Shapes.or(layer4, layer5);

        VoxelShape l0123 = Shapes.or(l01, l23);
        VoxelShape l456 = Shapes.or(l45, layer6);

        return Shapes.or(l0123, l456);
    }
}
