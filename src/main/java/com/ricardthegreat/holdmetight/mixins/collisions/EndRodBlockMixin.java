package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(EndRodBlock.class)
public class EndRodBlockMixin extends RodBlock{
    public EndRodBlockMixin(Properties p_154339_) {super(p_154339_);}

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && (EntitySizeUtils.getSize(ent) < 0.21)){
                return endRodShape(state.getValue(BlockStateProperties.FACING));
            }
        }
        return super.getShape(state, getter, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && (EntitySizeUtils.getSize(ent) < 0.21)){
                return endRodShape(state.getValue(BlockStateProperties.FACING));
            }
        }
        return super.getCollisionShape(state, getter, pos, context);
    }

    private VoxelShape endRodShape(Direction direction){
        VoxelShape rod;
        VoxelShape base;
        switch (direction) {
            case UP:
            default:
                rod = Block.box(7.0D, 1.0D, 7.0D, 9.0D, 16.0D, 9.0D);
                base = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 1.0D, 10.0D);
                return Shapes.or(rod, base);
            case DOWN:
                rod = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 15.0D, 9.0D);
                base = Block.box(6.0D, 15.0D, 6.0D, 10.0D, 16.0D, 10.0D);
                return Shapes.or(rod, base);
            case NORTH:
                rod = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 15.0D);
                base = Block.box(6.0D, 6.0D, 15.0D, 10.0D, 10.0D, 16.0D);
                return Shapes.or(rod, base);
            case SOUTH:
                rod = Block.box(7.0D, 7.0D, 1.0D, 9.0D, 9.0D, 16.0D);
                base = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 1.0D);
                return Shapes.or(rod, base);
            case EAST:
                rod = Block.box(0.0D, 7.0D, 7.0D, 15.0D, 9.0D, 9.0D);
                base = Block.box(15.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
                return Shapes.or(rod, base);
            case WEST:
                rod = Block.box(1.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
                base = Block.box(0.0D, 6.0D, 6.0D, 1.0D, 10.0D, 10.0D);
                return Shapes.or(rod, base);
        }
    }
}
