package com.ricardthegreat.holdmetight.mixins.collisions;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(SignBlock.class)
public abstract class SignBlockMixin extends BaseEntityBlock {
    @Shadow abstract public VoxelShape getShape(BlockState p_51973_, BlockGetter p_51974_, BlockPos p_51975_, CollisionContext p_51976_);

    protected SignBlockMixin(Properties p_49224_) {
        super(p_49224_);
    }
    
    public BlockEntity newBlockEntity(@Nonnull BlockPos p_153215_, @Nonnull BlockState p_153216_) {
        return new SignBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        if (EntitySizeUtils.getSize(collidingEntity) < 0.21) {
            return true;
        }
        return super.collisionExtendsVertically(state, level, pos, collidingEntity);
    }

    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(signShape(state));
        }
    }

    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        if (shouldChangeShape(context)) {
            return signShape(state);
        }
        return Shapes.empty();
    }

    //inverted this from what i normally do so by default it has collision which is disabled if you are above a certain size
    //have this so that the extended vertical collision works properly without needing to add collision for larger people
    private boolean shouldChangeShape(CollisionContext context){
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && (EntitySizeUtils.getSize(ent) >= 0.21)){
                return false;
            }
        }
        return true;
    }

    private VoxelShape signShape(BlockState state){
        if (((SignBlock) (Object) this) instanceof StandingSignBlock) {
            return freestand(state);
        }else if (((SignBlock) (Object) this) instanceof WallSignBlock) {
            return wall();
        }

        return Shapes.empty();
    }

    private VoxelShape freestand(BlockState state){
        int rotation = state.getValue(BlockStateProperties.ROTATION_16);
        VoxelShape pole = Block.box(8-0.6666667F, 0, 8-0.6666667F, 8+0.6666667F, 16 + (0.6666667F*2), 8+0.6666667F);   
        VoxelShape sign = Shapes.empty();
        switch (rotation) {
            default: 
                return Shapes.or(pole, sign);
            case 0:
            case 8:  
                sign =  Block.box(0, 10-0.6666667F, 8-0.6666667F, 16, 16 + (0.6666667F*2), 8+0.6666667F);
                return Shapes.or(pole, sign);
            case 4:
            case 12:  
                sign =  Block.box(8-0.6666667F, 10-0.6666667F, 0, 8+0.6666667F, 16 + (0.6666667F*2), 16);
                return Shapes.or(pole, sign);
        }
    }
    private VoxelShape wall(){
        return Shapes.empty();
    }
}
