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
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(LecternBlock.class)
public abstract class LecternBlockMixin extends BaseEntityBlock {

    protected LecternBlockMixin(Properties p_49224_) {super(p_49224_);}

    @Shadow abstract public VoxelShape getShape(BlockState p_51973_, BlockGetter p_51974_, BlockPos p_51975_, CollisionContext p_51976_);
    
    //adds a hitbox for those under 0.1
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
        if (shouldChangeShape(context)) {
            info.setReturnValue(getShape(state, getter, pos, context));
        }
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        if (EntitySizeUtils.getSize(collidingEntity) < 0.21) {
            return true;
        }
        return super.collisionExtendsVertically(state, level, pos, collidingEntity);
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
}
