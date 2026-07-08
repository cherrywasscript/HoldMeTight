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
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(BrewingStandBlock.class)
public abstract class BrewingStandBlockMixin extends BaseEntityBlock {

    protected BrewingStandBlockMixin(Properties p_49224_) {super(p_49224_);}

    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(brewingStandShape());
        }
    }
     
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return brewingStandShape();
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

    private VoxelShape brewingStandShape(){
        VoxelShape base0 = Block.box(1.0D, 0.0D, 1.0D, 7.0D, 2.0D, 7.0D);
        VoxelShape base1 = Block.box(1.0D, 0.0D, 9.0D, 7.0D, 2.0D, 15.0D);
        VoxelShape base2 = Block.box(9.0D, 0.0D, 5.0D, 15.0D, 2.0D, 11.0D);
        VoxelShape stem = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D);

        return Shapes.or(Shapes.or(base0, base1), Shapes.or(base2, stem));
    }
}
