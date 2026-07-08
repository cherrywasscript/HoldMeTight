package com.ricardthegreat.holdmetight.mixins.collisions;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.BlockHitboxHelper;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(FenceGateBlock.class)
public class FenceGateBlockMixin {


    //treats fence gates as open for any ent under 0.21 scale
    //wanted to do <= 0.2 but i think 0.2 in game is like 0.20000000001 or something bc it didnt work
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(BlockState state, BlockGetter p_53397_, BlockPos p_53398_, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;

            Entity ent = entContext.getEntity();
            if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
                cir.setReturnValue(fenceGateShape(state));
            }
        }
    }

    /*
    @Inject(method = "getShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    public void getShape(BlockState state, BlockGetter p_53392_, BlockPos p_53393_, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;

            Entity ent = entContext.getEntity();
            if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
                cir.setReturnValue(fenceGateShape(state));
            }
        }
    } */

    private VoxelShape fenceGateShape(BlockState state){
        int rotation = 0;
        
        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
        switch (direction) {
            case EAST:
            default:
                rotation = 0;
                break;
            case WEST:
                rotation = 2;
                break;
            case SOUTH:
                rotation = 1;
                break;
            case NORTH:
                rotation = 3;
                break;
        }

        int yoffset = 0;
        if (!state.getValue(FenceGateBlock.IN_WALL)) {
            yoffset = 3;
        }

        VoxelShape edge0 = BlockHitboxHelper.easyBox(2, 11, 2, 7, 2 + yoffset, 0, rotation);
        VoxelShape edge1 = BlockHitboxHelper.easyBox(2, 11, 2, 7, 2 + yoffset, 14, rotation);

        VoxelShape ret = Shapes.or(edge0, edge1);

        if (state.getValue(FenceGateBlock.OPEN)) {
            VoxelShape openLeftTop = BlockHitboxHelper.easyBox(6, 3, 2, 9, 9 + yoffset, 0, rotation);
            VoxelShape openLeftBottom = BlockHitboxHelper.easyBox(6, 3, 2, 9, 3 + yoffset, 0, rotation);
            VoxelShape openLeftMiddle = BlockHitboxHelper.easyBox(2, 3, 2, 13, 6 + yoffset, 0, rotation);

            VoxelShape openRightTop = BlockHitboxHelper.easyBox(6, 3, 2, 9, 9 + yoffset, 14, rotation);
            VoxelShape openRightBottom = BlockHitboxHelper.easyBox(6, 3, 2, 9, 3 + yoffset, 14, rotation);
            VoxelShape openRightMiddle = BlockHitboxHelper.easyBox(2, 3, 2, 13, 6 + yoffset, 14, rotation);

            ret = BlockHitboxHelper.massOr(List.of(ret, openLeftTop, openLeftBottom, openLeftMiddle, openRightTop, openRightBottom, openRightMiddle));
        }else{
            VoxelShape closedTop = BlockHitboxHelper.easyBox(2, 3, 12, 7, 9 + yoffset, 2, rotation);
            VoxelShape closedBottom = BlockHitboxHelper.easyBox(2, 3, 12, 7, 3 + yoffset, 2, rotation);
            VoxelShape closedMiddle = BlockHitboxHelper.easyBox(2, 3, 4, 7, 6 + yoffset, 6, rotation);

            ret = BlockHitboxHelper.massOr(List.of(ret, closedTop, closedBottom, closedMiddle));
        }

        return ret;
    }
}
