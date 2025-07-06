package com.ricardthegreat.holdmetight.blocks.tinyjars;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.init.BlockInit;
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

public class JarBlock extends AbstractJarBlock {

    public JarBlock(Properties properties) {
        super(properties);
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
               level.setBlockAndUpdate(pos, BlockInit.TINY_JAR_FULL.get().defaultBlockState().setValue(LiquidJarBlock.OPEN, state.getOptionalValue(OPEN).get()));
            }else{
                level.setBlock(pos, state.cycle(OPEN), UPDATE_ALL);
            }

            return InteractionResult.SUCCESS;
        }else{
            return InteractionResult.FAIL;
        }
    
    }

}
