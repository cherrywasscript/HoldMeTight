package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin extends DiodeBlock{

    protected ComparatorBlockMixin(Properties p_53205_) {super(p_53205_);}
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return comparitorShape(state.getValue(FACING));
        }
        return super.getShape(state, getter, pos, context);
    }
     
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(context)) {
            return comparitorShape(state.getValue(FACING));
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

    private VoxelShape comparitorShape(Direction direction){
        VoxelShape base = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

        VoxelShape torch0 = null;
        VoxelShape torch1 = null;
        VoxelShape torch2 = null;

        switch (direction) {
            case SOUTH:
            default:
                torch0 = Block.box(7.0D, 2.0D, 2.0D, 9.0D, 4.0D, 4.0D);
                torch1 = Block.box(4.0D, 2.0D, 11.0D, 6.0D, 7.0D, 13.0D);
                torch2 = Block.box(10.0D, 2.0D, 11.0D, 12.0D, 7.0D, 13.0D);
                break;
            case NORTH:
                torch0 = Block.box(7.0D, 2.0D, 12.0D, 9.0D, 4.0D, 14.0D);
                torch1 = Block.box(4.0D, 2.0D, 3.0D, 6.0D, 7.0D, 5.0D);
                torch2 = Block.box(10.0D, 2.0D, 3.0D, 12.0D, 7.0D, 5.0D);
                break;
            case EAST:
                torch0 = Block.box(2.0D, 2.0D, 7.0D, 4.0D, 4.0D, 9.0D);
                torch1 = Block.box(11.0D, 2.0D, 4.0D, 13.0D, 7.0D, 6.0D);
                torch2 = Block.box(11.0D, 2.0D, 10.0D, 13.0D, 7.0D, 12.0D);
                break;
            case WEST:
                torch0 = Block.box(12.0D, 2.0D, 7.0D, 14.0D, 4.0D, 9.0D);
                torch1 = Block.box(3.0D, 2.0D, 4.0D, 5.0D, 7.0D, 6.0D);
                torch2 = Block.box(3.0D, 2.0D, 10.0D, 5.0D, 7.0D, 12.0D);
                break;
        }

        return Shapes.or(Shapes.or(base, torch0), Shapes.or(torch1, torch2));
    }
}
