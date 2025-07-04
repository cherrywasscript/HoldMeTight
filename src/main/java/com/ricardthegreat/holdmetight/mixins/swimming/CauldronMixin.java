package com.ricardthegreat.holdmetight.mixins.swimming;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.utils.IBlockSwimming;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(LayeredCauldronBlock.class)
public abstract class CauldronMixin extends Block{

    public CauldronMixin(Properties p_49795_) {super(p_49795_);}

    //TODO figure out if this needs to be server side only or not
    @Inject(at = @At("HEAD"), method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity, CallbackInfo info) {
        if (entity instanceof Player) {
            if (entInWater(state, entity, pos)) {
                IBlockSwimming pl = (IBlockSwimming) entity; //IBlockSwimming is blockswimmingmixin
                pl.setIsInSwimmableBlock(true);
            }
        }
    }

    private boolean entInWater(BlockState state, Entity entity, BlockPos pos){
        Vec3 entPos = entity.position();
        
        if (entPos.y() <= pos.getY()+ getContentHeight(state) && entPos.y() >= pos.getY()+0.25) {
            return true;
        }
        return false;
    }

    @Shadow
    abstract protected double getContentHeight(BlockState state);
    
}
