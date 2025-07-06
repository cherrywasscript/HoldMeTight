package com.ricardthegreat.holdmetight.blocks.tinyjars;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.effects.ShrinkEffect;
import com.ricardthegreat.holdmetight.utils.IBlockSwimming;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LiquidJarBlock extends AbstractJarBlock {

    public LiquidJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Player) {
            if (entInWater(state, entity, pos)) {
                IBlockSwimming pl = (IBlockSwimming) entity; //IBlockSwimming is blockswimmingmixin
                pl.setIsInSwimmableBlock(true);

                //TODO move this to specific shrink potion jar file
                double mult = 1-((0.00034)*(0+1));
                float size = EntitySizeUtils.getSize(entity);
                double target = size*mult;

                EntitySizeUtils.setSize(entity, (float) target, 0);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (EntitySizeUtils.getSize(player) >= 0.8) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            if (playerCarry.getIsCarrying() && playerCarry.getCarryPosition().posName == "hand") {
                Vec3 center = pos.getCenter();
                center = center.add(0, -0.4375, 0);

                Entity passenger = player.getFirstPassenger();
                passenger.stopRiding();
                passenger.dismountTo(center.x(), center.y(), center.z());
            }else if (player.getItemInHand(hand).is(Items.WATER_BUCKET)) {
               //TODO allow depth to increase and decrease as well as completely empty
            }else{
                level.setBlock(pos, state.cycle(OPEN), UPDATE_ALL);
            }

            

            return InteractionResult.SUCCESS;
        }else{
            return InteractionResult.FAIL;
        }
    
    }

    private boolean entInWater(BlockState state, Entity entity, BlockPos pos){
        Vec3 entPos = entity.position();

        if (makeWaterCross().bounds().move(pos).contains(entPos)) {
            return true;
        }
        return false;
    }

    private VoxelShape makeWaterCross(){
        VoxelShape block0 = Block.box(6, 1, 7, 10, 2, 9);
        VoxelShape block1 = Block.box(7, 1, 6, 9, 2, 10);
        return Shapes.or(block0, block1);
    }
}
