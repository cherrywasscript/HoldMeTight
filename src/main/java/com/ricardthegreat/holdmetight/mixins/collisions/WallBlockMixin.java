package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(WallBlock.class)
public abstract class WallBlockMixin{

   @Shadow abstract VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_);

   //not sure if this works serverside yet but it should
   //returns only the post hitbox for fences if the player is less than 0.21
   //wanted to do <= 0.2 but i think 0.2 in game is like 0.20000000001 or something bc it didnt work
   @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
   public void getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> info) {
      if (context instanceof EntityCollisionContext) {
         EntityCollisionContext entContext = (EntityCollisionContext) context;

         Entity ent = entContext.getEntity();
         if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
            info.setReturnValue(getShape(state, level, pos, entContext));
         }
      }
   }
}
