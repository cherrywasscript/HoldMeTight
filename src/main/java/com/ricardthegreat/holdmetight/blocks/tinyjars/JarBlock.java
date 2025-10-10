package com.ricardthegreat.holdmetight.blocks.tinyjars;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.init.BlockInit;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

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
            }else if (player.getItemInHand(hand).is(Items.POTION)) {
                ItemStack item = player.getItemInHand(hand);
                Potion potion = PotionUtils.getPotion(item);

                if (potion.getName("").equals("water")) {
                    level.setBlockAndUpdate(pos, BlockInit.TINY_JAR_FULL.get().defaultBlockState().setValue(LiquidJarBlock.OPEN, state.getOptionalValue(OPEN).get()));
                }  
            }else{
                level.setBlock(pos, state.cycle(OPEN), UPDATE_ALL);
            }

            return InteractionResult.SUCCESS;
        }else{
            return InteractionResult.FAIL;
        }
    
    }

}
