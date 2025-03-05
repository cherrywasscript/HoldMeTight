package com.ricardthegreat.holdmetight.mixins.collisions;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(LeverBlock.class)
public class LeverBlockMixin extends FaceAttachedHorizontalDirectionalBlock{

    //lever shapes adjusted so collision matches model, interaction box unchanged
    private static final VoxelShape NORTH_AABB = Block.box(5.0D, 4.0D, 10.0D, 8.0D, 12.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(5.0D, 4.0D, 0.0D, 8.0D, 12.0D, 6.0D);
    private static final VoxelShape WEST_AABB = Block.box(10.0D, 4.0D, 5.0D, 16.0D, 12.0D, 8.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 4.0D, 5.0D, 6.0D, 12.0D, 8.0D);
    private static final VoxelShape UP_AABB_Z = Block.box(5.0D, 0.0D, 4.0D, 11.0D, 3.0D, 12.0D);
    private static final VoxelShape UP_AABB_X = Block.box(4.0D, 0.0D, 5.0D, 12.0D, 3.0D, 11.0D);
    private static final VoxelShape DOWN_AABB_Z = Block.box(5.0D, 10.0D, 4.0D, 11.0D, 13.0D, 12.0D);
    private static final VoxelShape DOWN_AABB_X = Block.box(4.0D, 10.0D, 5.0D, 12.0D, 13.0D, 11.0D);

    public LeverBlockMixin(Properties p_53182_) {
        super(p_53182_);
    }
    
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && SizeUtils.getSize(ent) < 0.21){
                return getCollision(state, level, pos, context);
            }
        }
        return Shapes.empty();
    }


    private VoxelShape getCollision(BlockState p_54665_, BlockGetter p_54666_, BlockPos p_54667_, CollisionContext p_54668_) {
        switch ((AttachFace)p_54665_.getValue(FACE)) {
            case FLOOR:
                switch (p_54665_.getValue(FACING).getAxis()) {
                case X:
                    return UP_AABB_X;
                case Z:
                default:
                    return UP_AABB_Z;
                }
            case WALL:
                switch ((Direction)p_54665_.getValue(FACING)) {
                case EAST:
                    return EAST_AABB;
                case WEST:
                    return WEST_AABB;
                case SOUTH:
                    return SOUTH_AABB;
                case NORTH:
                default:
                    return NORTH_AABB;
                }
            case CEILING:
            default:
                switch (p_54665_.getValue(FACING).getAxis()) {
                case X:
                    return DOWN_AABB_X;
                case Z:
                default:
                    return DOWN_AABB_Z;
                }
        }
    }
}
