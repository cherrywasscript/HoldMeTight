package com.ricardthegreat.holdmetight.blocks;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class JarBlock extends Block {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public JarBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.valueOf(false)));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (SizeUtils.getSize(player) >= 0.8) {
            level.setBlock(pos, state.cycle(OPEN), UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }else{
            return InteractionResult.FAIL;
        }
    
    }

    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        /* 
        if (state.getOptionalValue(OPEN).get()) {
            return makeShape();
        }else{
            return makeClosedShape();
        }
        */
        return Block.box(4, 0, 4, 12, 11, 12);
    }

    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {   
        if (state.getOptionalValue(OPEN).get()) {
            return makeShape();
        }else{
            return makeClosedShape();
        }
    }

    private VoxelShape makeClosedShape(){
        VoxelShape s0 = Shapes.or(makeShape(), makeStopper());
        return s0;
    }

    private VoxelShape makeShape(){
        //this just combines all the shapes
        //probaby bad


        VoxelShape s0 = Shapes.or(base(), layer1());
        VoxelShape s1 = Shapes.or(mainBody(), layer5());
        VoxelShape s2 = Shapes.or(layer6(), neck());
        VoxelShape s3 = Shapes.or(s0, s1);
        VoxelShape s4 = Shapes.or(s2, s3);

        return s4;
    }

    private VoxelShape base(){
        VoxelShape block0 = Block.box(6, 0, 7, 10, 1, 9);
        VoxelShape block1 = Block.box(7, 0, 6, 9, 1, 10);
        return Shapes.or(block0, block1);
    }

    private VoxelShape layer1(){
        VoxelShape edge0 = Block.box(5, 1, 7, 6, 2, 9);
        VoxelShape edge1 = Block.box(10, 1, 7, 11, 2, 9);
        VoxelShape edge2 = Block.box(7, 1, 5, 9, 2, 6);
        VoxelShape edge3 = Block.box(7, 1, 10, 9, 2, 11);

        VoxelShape corner0 = Block.box(6, 1, 9, 7, 2, 10);
        VoxelShape corner1 = Block.box(6, 1, 6, 7, 2, 7);
        VoxelShape corner2 = Block.box(9, 1, 9, 10, 2, 10);
        VoxelShape corner3 = Block.box(9, 1, 6, 10, 2, 7);

        VoxelShape or0 = Shapes.or(edge0, edge1);
        VoxelShape or1 = Shapes.or(edge2, edge3);
        VoxelShape or2 = Shapes.or(or0, or1);
        VoxelShape or3 = Shapes.or(corner0, corner1);
        VoxelShape or4 = Shapes.or(corner2, corner3);
        VoxelShape or5 = Shapes.or(or3, or4);
        VoxelShape or6 = Shapes.or(or2, or5);

        return or6;
    }

    //layers 2,3and 4
    private VoxelShape mainBody(){
        VoxelShape edge0 = Block.box(4, 2, 7, 5, 5, 9);
        VoxelShape edge1 = Block.box(11, 2, 7, 12, 5, 9);
        VoxelShape edge2 = Block.box(7, 2, 4, 9, 5, 5);
        VoxelShape edge3 = Block.box(7, 2, 11, 9, 5, 12);

        VoxelShape corner0 = Block.box(5, 2, 6, 6, 5, 7);
        VoxelShape corner1 = Block.box(6, 2, 5, 7, 5, 6);

        VoxelShape corner2 = Block.box(5, 2, 9, 6, 5, 10);
        VoxelShape corner3 = Block.box(6, 2, 10, 7, 5, 11);

        VoxelShape corner4 = Block.box(9, 2, 5, 10, 5, 6);
        VoxelShape corner5 = Block.box(10, 2, 6, 11, 5, 7);

        VoxelShape corner6 = Block.box(9, 2, 10, 10, 5, 11);
        VoxelShape corner7 = Block.box(10, 2, 9, 11, 5, 10);

        VoxelShape or0 = Shapes.or(edge0, edge1);
        VoxelShape or1 = Shapes.or(edge2, edge3);
        VoxelShape or2 = Shapes.or(or0, or1);
        VoxelShape or3 = Shapes.or(corner0, corner1);
        VoxelShape or4 = Shapes.or(corner2, corner3);
        VoxelShape or5 = Shapes.or(or3, or4);
        VoxelShape or6 = Shapes.or(or2, or5);
        VoxelShape or7 = Shapes.or(corner4, corner5);
        VoxelShape or8 = Shapes.or(corner6, corner7);
        VoxelShape or9 = Shapes.or(or7, or8);
        VoxelShape or10 = Shapes.or(or6, or9);

        return or10;
    }

    private VoxelShape layer5(){
        VoxelShape block0 = Block.box(5, 5, 6, 6, 6, 10);
        VoxelShape block1 = Block.box(10, 5, 6, 11, 6, 10);
        VoxelShape block2 = Block.box(6, 5, 5, 10, 6, 6);
        VoxelShape block3 = Block.box(6, 5, 10, 10, 6, 11);

        VoxelShape or0 = Shapes.or(block0, block1);
        VoxelShape or1 = Shapes.or(block2, block3);
        VoxelShape or2 = Shapes.or(or0, or1);

        return or2;
    }

    private VoxelShape layer6(){
        VoxelShape block0 = Block.box(6, 6, 6, 7, 7, 10);
        VoxelShape block1 = Block.box(9, 6, 6, 10, 7, 10);

        VoxelShape block2 = Block.box(6, 6, 6, 10, 7, 7);
        VoxelShape block3 = Block.box(6, 6, 9, 10, 7, 10);

        VoxelShape or0 = Shapes.or(block0, block1);
        VoxelShape or1 = Shapes.or(block2, block3);
        VoxelShape or2 = Shapes.or(or0, or1);

        return or2;
    }

    //layer 7 and 8
    private VoxelShape neck(){
        VoxelShape block0 = Block.box(6, 7, 7, 7, 9, 9);
        VoxelShape block1 = Block.box(9, 7, 7, 10, 9, 9);
        VoxelShape block2 = Block.box(7, 7, 6, 9, 9, 7);
        VoxelShape block3 = Block.box(7, 7, 9, 9, 9, 10);

        VoxelShape or0 = Shapes.or(block0, block1);
        VoxelShape or1 = Shapes.or(block2, block3);
        VoxelShape or2 = Shapes.or(or0, or1);

        return or2;
    }

    private VoxelShape makeStopper(){
        VoxelShape block0 = Block.box(7, 8, 7, 9, 9, 9);
        VoxelShape block1 = Block.box(6, 9, 6, 10, 11, 10);

        return Shapes.or(block0, block1);
    }
}
