package com.ricardthegreat.holdmetight.blocks.tinyjars;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.init.BlockInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
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
            
            if (player.getItemInHand(hand).is(ItemInit.PLAYER_ITEM.get())) {
                placePlayerInside(pos, player);
            }else if (player.getItemInHand(hand).is(Items.WATER_BUCKET)) {
               
            }else{
                level.setBlock(pos, state.cycle(OPEN), UPDATE_ALL);
            }

            return InteractionResult.SUCCESS;
        }else{
            return InteractionResult.FAIL;
        }
    
    }

}
