package com.ricardthegreat.holdmetight.mixins.collisions;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

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
    private static final VoxelShape COLLISION_NORTH_AABB = Block.box(5.0D, 4.0D, 13.0D, 11.0D, 12.0D, 16.0D);
    private static final VoxelShape COLLISION_SOUTH_AABB = Block.box(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 3.0D);
    private static final VoxelShape COLLISION_WEST_AABB = Block.box(13.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
    private static final VoxelShape COLLISION_EAST_AABB = Block.box(0.0D, 4.0D, 5.0D, 3.0D, 12.0D, 11.0D);
    private static final VoxelShape COLLISION_UP_AABB_Z = Block.box(5.0D, 0.0D, 4.0D, 11.0D, 3.0D, 12.0D);
    private static final VoxelShape COLLISION_UP_AABB_X = Block.box(4.0D, 0.0D, 5.0D, 12.0D, 3.0D, 11.0D);
    private static final VoxelShape COLLISION_DOWN_AABB_Z = Block.box(5.0D, 13.0D, 4.0D, 11.0D, 16.0D, 12.0D);
    private static final VoxelShape COLLISION_DOWN_AABB_X = Block.box(4.0D, 13.0D, 5.0D, 12.0D, 16.0D, 11.0D);

    public LeverBlockMixin(Properties p_53182_) {
        super(p_53182_);
    }
    
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
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
                    return COLLISION_UP_AABB_X;
                case Z:
                default:
                    return COLLISION_UP_AABB_Z;
                }
            case WALL:
                switch ((Direction)p_54665_.getValue(FACING)) {
                case EAST:
                    return COLLISION_EAST_AABB;
                case WEST:
                    return COLLISION_WEST_AABB;
                case SOUTH:
                    return COLLISION_SOUTH_AABB;
                case NORTH:
                default:
                    return COLLISION_NORTH_AABB;
                }
            case CEILING:
            default:
                switch (p_54665_.getValue(FACING).getAxis()) {
                case X:
                    return COLLISION_DOWN_AABB_X;
                case Z:
                default:
                    return COLLISION_DOWN_AABB_Z;
                }
        }
    }
}
