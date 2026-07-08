package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.utils.BlockHitboxHelper;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(TripWireHookBlock.class)
public class TripWireHookBlockMixin extends Block{

    public TripWireHookBlockMixin(Properties p_49795_) {super(p_49795_);}

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return tripwireShape(state);
        }
        return super.getCollisionShape(state, getter, pos, context);
    }

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

    private VoxelShape tripwireShape(BlockState state){
        int rotation = 0;
        
        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
        switch (direction) {
            case EAST:
            default:
                rotation = 1;
                break;
            case WEST:
                rotation = 3;
                break;
            case SOUTH:
                rotation = 2;
                break;
            case NORTH:
                rotation = 0;
                break;
        }
        
        VoxelShape body = BlockHitboxHelper.easyBox(4, 8, 2, 6, 1, 14, rotation);

        return body;
    }
}
