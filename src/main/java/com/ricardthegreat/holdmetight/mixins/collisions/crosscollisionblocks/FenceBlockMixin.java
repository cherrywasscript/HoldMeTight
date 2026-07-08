package com.ricardthegreat.holdmetight.mixins.collisions.crosscollisionblocks;

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
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin extends CrossCollisionBlock{
   private static VoxelShape pillar = Block.box(6, 0.0D, 6, 10, 16, 10);
   private static VoxelShape north = Shapes.or(Block.box(7, 6, 0, 9, 9, 6), Block.box(7, 12, 0, 9, 15, 6));
   private static VoxelShape south = Shapes.or(Block.box(7, 6, 10, 9, 9, 16), Block.box(7, 12, 10, 9, 15, 16));
   private static VoxelShape east = Shapes.or(Block.box(10, 6, 7, 16, 9, 9), Block.box(10, 12, 7, 16, 15, 9));
   private static VoxelShape west = Shapes.or(Block.box(0, 6, 7, 6, 9, 9), Block.box(0, 12, 7, 6, 15, 9));

   public FenceBlockMixin(float p_52320_, float p_52321_, float p_52322_, float p_52323_, float p_52324_,Properties p_52325_) {super(p_52320_, p_52321_, p_52322_, p_52323_, p_52324_, p_52325_);}

   //not sure if this works serverside yet but it should
   //returns only the post hitbox for fences if the player is less than 0.21
   //wanted to do <= 0.2 but i think 0.2 in game is like 0.20000000001 or something bc it didnt work
   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      if (context instanceof EntityCollisionContext) {
         EntityCollisionContext entContext = (EntityCollisionContext) context;

         Entity ent = entContext.getEntity();
         if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
            return fenceShape(state);
         }
      }

      return super.getCollisionShape(state, level, pos, context);
   }

   private VoxelShape fenceShape(BlockState state){
      VoxelShape shape = pillar;

      if (state.getValue(NORTH)) {
         shape = Shapes.or(shape, north);
      }
      if (state.getValue(SOUTH)) {
         shape = Shapes.or(shape, south);
      }
      if (state.getValue(EAST)) {
         shape = Shapes.or(shape, east);
      }
      if (state.getValue(WEST)) {
         shape = Shapes.or(shape, west);
      }

      return shape;
   }
}
