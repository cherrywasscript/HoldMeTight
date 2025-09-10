package com.ricardthegreat.holdmetight.mixins.collisions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(CrossCollisionBlock.class)
public class CrossCollisionBlockMixin {
    
   @Shadow
   VoxelShape[] collisionShapeByIndex;

   @Shadow
   protected int getAABBIndex(BlockState p_52364_) {return 0;}


   //not sure if this works serverside yet but it should
   //returns only the post hitbox for fences if the player is less than 0.21
   //wanted to do <= 0.2 but i think 0.2 in game is like 0.20000000001 or something bc it didnt work
   @Overwrite
   public VoxelShape getCollisionShape(BlockState p_52357_, BlockGetter p_52358_, BlockPos p_52359_, CollisionContext context) {
      if (context instanceof EntityCollisionContext) {
         EntityCollisionContext entContext = (EntityCollisionContext) context;

         Entity ent = entContext.getEntity();
         if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
            if ((Object) this instanceof FenceBlock) {
               return Block.box(6, 0.0D, 6, 10, 16, 10);
            }
            //TODO make this not work for glass panes
            return this.collisionShapeByIndex[0];
         }
      }

      return this.collisionShapeByIndex[this.getAABBIndex(p_52357_)];
   }
}
