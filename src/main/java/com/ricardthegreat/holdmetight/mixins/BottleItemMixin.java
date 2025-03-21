package com.ricardthegreat.holdmetight.mixins;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(BottleItem.class)
public class BottleItemMixin extends Item{

    public BottleItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        context.getClickedFace().step();
        Vec3i vecOffset = new Vec3i((int) context.getClickedFace().step().x(),(int) context.getClickedFace().step().y(),(int) context.getClickedFace().step().z());
        BlockPos targetBlock = context.getClickedPos().offset(vecOffset);
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }

        if (level.getBlockState(targetBlock).canBeReplaced() && (!shouldFill(level, player) || player.isShiftKeyDown())) {
            level.setBlock(targetBlock, BlockInit.TINY_JAR.get().defaultBlockState(), Block.UPDATE_NEIGHBORS);
            if (!player.getAbilities().instabuild) {
                if (player.getMainHandItem().is(Items.GLASS_BOTTLE)) {
                    player.getMainHandItem().shrink(1);
                }else if (player.getOffhandItem().is(Items.GLASS_BOTTLE)) {
                    player.getOffhandItem().shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    private boolean shouldFill(Level level, Player player){
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
         if (blockhitresult.getType() == HitResult.Type.MISS) {
            return false;
         } else {
            if (blockhitresult.getType() == HitResult.Type.BLOCK) {
               BlockPos blockpos = blockhitresult.getBlockPos();
               if (!level.mayInteract(player, blockpos)) {
                  return false;
               }

               if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                  return true;
               }
            }

            return false;
         }
    }
    
}
