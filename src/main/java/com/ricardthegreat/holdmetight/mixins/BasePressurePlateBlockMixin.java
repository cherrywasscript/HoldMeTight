package com.ricardthegreat.holdmetight.mixins;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(BasePressurePlateBlock.class)
public class BasePressurePlateBlockMixin extends Block{

    public BasePressurePlateBlockMixin(Properties p_49795_) {
            super(p_49795_);

    }
    
    //disables pressureplate for those under 0.1
    @Inject(at = @At("HEAD"), method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    public void entityInside(BlockState p_49314_, Level p_49315_, BlockPos p_49316_, Entity entity, CallbackInfo info) {
        if(SizeUtils.getSize(entity) < 0.1){
            info.cancel();
        }
    }

    //adds a hitbox for those under 0.1
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState p_52357_, @Nonnull BlockGetter p_52358_, @Nonnull BlockPos p_52359_, @Nonnull CollisionContext context) {
        //block with the bounds of a pressure plate
        VoxelShape voxelshape = Block.box(1, 0.0D, 1, 15, 1, 15);

        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && SizeUtils.getSize(ent) < 0.1){
                return voxelshape;
            }
        }
        return Shapes.empty();
    }
}
