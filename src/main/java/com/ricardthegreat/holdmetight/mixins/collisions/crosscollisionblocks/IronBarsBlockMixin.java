package com.ricardthegreat.holdmetight.mixins.collisions.crosscollisionblocks;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(IronBarsBlock.class)
public abstract class IronBarsBlockMixin extends CrossCollisionBlock{
    private static VoxelShape pillar = Block.box(7, 0,7, 9, 16, 9);

    private static VoxelShape north = Shapes.or(Block.box(7.95, 2,4, 8.05, 4, 7), 
                                        Shapes.or(Block.box(7.95, 0, 2, 8.05, 16, 4), Block.box(7.95, 7, 0, 8.05, 9, 2)));

    private static VoxelShape south = Shapes.or(Block.box(7.95, 12,9, 8.05, 14, 12), 
                                        Shapes.or(Block.box(7.95, 0, 12, 8.05, 16, 14), Block.box(7.95, 7, 14, 8.05, 9, 16)));

    private static VoxelShape east = Shapes.or(Block.box(9, 2,7.95, 12, 4, 8.05), 
                                        Shapes.or(Block.box(12, 0, 7.95, 14, 16, 8.05), Block.box(14, 7, 7.95, 16, 9, 8.05)));

    private static VoxelShape west = Shapes.or(Block.box(4, 12,7.95, 7, 14, 8.05), 
                                        Shapes.or(Block.box(2, 0, 7.95, 4, 16, 8.05), Block.box(0, 7, 7.95, 2, 9, 8.05)));

    public IronBarsBlockMixin(float p_52320_, float p_52321_, float p_52322_, float p_52323_, float p_52324_,Properties p_52325_) {super(p_52320_, p_52321_, p_52322_, p_52323_, p_52324_, p_52325_);}
    
    //not sure if this works serverside yet but it should
    //returns only the post hitbox for fences if the player is less than 0.21
    //wanted to do <= 0.2 but i think 0.2 in game is like 0.20000000001 or something bc it didnt work
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (shouldChangeShape(state, context)) {
            return ironBarShape(state);
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    private boolean shouldChangeShape(BlockState state, CollisionContext context){
        if (context instanceof EntityCollisionContext) {
            if (!state.getSoundType().equals(SoundType.GLASS)) {
                EntityCollisionContext entContext = (EntityCollisionContext) context;
                Entity ent = entContext.getEntity();
                if(ent != null && (EntitySizeUtils.getSize(ent) < 0.21)){
                    return true;
                }
            }
        }
        return false;
    }

    private VoxelShape ironBarShape(BlockState state){
        VoxelShape shape = pillar;

        if (state.getValue(NORTH)) {
            VoxelShape cap = Block.box(7, 15.95, 0, 9, 16, 7);
            VoxelShape base = Block.box(7, 0, 0, 9, 0.05, 7);
            shape = Shapes.or(shape, Shapes.or(cap, base));
            shape = Shapes.or(shape, north);
        }
        if (state.getValue(SOUTH)) {
            VoxelShape cap = Block.box(7, 15.95, 9, 9, 16, 16);
            VoxelShape base = Block.box(7, 0, 9, 9, 0.05, 16);
            shape = Shapes.or(shape, Shapes.or(cap, base));
            shape = Shapes.or(shape, south);
        }
        if (state.getValue(EAST)) {
            VoxelShape cap = Block.box(9, 15.95, 7, 16, 16, 9);
            VoxelShape base = Block.box(9, 0, 7, 16, 0.05, 9);
            shape = Shapes.or(shape, Shapes.or(cap, base));
            shape = Shapes.or(shape, east);
        }
        if (state.getValue(WEST)) {
            VoxelShape cap = Block.box(0, 15.95, 7, 7, 16, 9);
            VoxelShape base = Block.box(0, 0, 7, 7, 0.05, 9);
            shape = Shapes.or(shape, Shapes.or(cap, base));
            shape = Shapes.or(shape, west);
        }

        return shape;
    }
}
