package com.ricardthegreat.holdmetight.mixins.collisions;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(TorchBlock.class)
public class TorchBlockMixin extends Block{
    
    public TorchBlockMixin(Properties p_49795_) {
            super(p_49795_);
    }
    
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        //block with the bounds of a torch
        VoxelShape voxelshape = Block.box(7, 0.0D, 7, 9, 10, 9);

        if (context instanceof EntityCollisionContext) {
            EntityCollisionContext entContext = (EntityCollisionContext) context;
            Entity ent = entContext.getEntity();
            if(ent != null && EntitySizeUtils.getSize(ent) < 0.21){
                return voxelshape;
            }
        }
        return Shapes.empty();
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        
        if (entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity) && entOnTop(entity, pos) && EntitySizeUtils.getSize(entity) < 0.21) {
            if (state.is(Blocks.TORCH)) {
                entity.hurt(level.damageSources().inFire(), 1);
            }else if (state.is(Blocks.SOUL_TORCH)) {
                entity.hurt(level.damageSources().inFire(), 2);
            }
            
        }
    }

    private boolean entOnTop(Entity entity, BlockPos pos){
        Vec3 entPos = entity.position();
        //System.out.println("line 64/"+ pos);
        if (entPos.y() == pos.getY()+0.625 && correctX(entPos.x(), pos.getX()) && correctZ(entPos.z(), pos.getZ())) {
            return true;
        }
        return false;
    }

    private boolean correctX(double entX, double blockX){
        if (entX > blockX + 0.4375 && entX < blockX + 0.5625) {
            return true;
        }
        return false;
    }

    private boolean correctZ(double entZ, double blockZ){
        if (entZ > blockZ + 0.4375 &&entZ < blockZ + 0.5625) {
            return true;
        }
        return false;
    }
}
