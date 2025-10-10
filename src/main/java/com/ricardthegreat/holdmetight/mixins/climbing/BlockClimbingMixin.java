package com.ricardthegreat.holdmetight.mixins.climbing;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(LivingEntity.class)
public class BlockClimbingMixin {
    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    @ModifyReturnValue(method = "onClimbable()Z", at = @At("RETURN"))
    public boolean onClimbable(boolean original) {
        
        LivingEntity ent = (LivingEntity) (Object) this;
        
        if (original) {
            if ((LivingEntity) (Object) this instanceof Player) {
                if(PlayerCarryProvider.getPlayerCarryCapability((Player) (Object) this).getIsCarried()){
                    return false;
                }
            }
            return original;
        }

        //checks if entity is .5 or less and is player
        if (ent.isSpectator() || EntitySizeUtils.getSize(ent) >= 0.8 || !(ent instanceof Player)) {
            return false;
        }else{

            //checks if they are next to a climbable block
            if (checkForBlock(ent) ) {
                this.lastClimbablePos = Optional.of(ent.blockPosition());
                return true;
            }

            return false;
        }

    }


    //this is almost certainly extremely inefficient 
    private boolean checkForBlock(LivingEntity ent){

        //the radius of the entitys hitbox
        double bbradius = ent.getBbWidth()/2;

        //their current x and z pos
        double xpos = ent.position().x;
        double zpos = ent.position().z;

        //the x y and z coords of their current block position
        BlockPos playerBlockPos = ent.blockPosition();
        int bpx = playerBlockPos.getX();
        int bpy = playerBlockPos.getY();
        int bpz = playerBlockPos.getZ();

        
        //because blocks positions are at the most north west point, e.g. a block with centre 0.5, 0.5 counts as at 0,0 these two are simple
        int bpsouth = (int) Math.floor(zpos+bbradius);
        int bpeast = (int) Math.floor(xpos+bbradius);
        //but these two are slightly more complicated as being right on the edge
        //im making the hitbox count as being 1% larger so that it rolls over to be rounded down to the correct value and hopefully isnt noticeable
        int bpnorth = (int) Math.floor(zpos-bbradius*1.01);
        int bpwest = (int) Math.floor(xpos-bbradius*1.01);

        


        //add the 4 potential blocks to the array
        Vec3i[] vecs = new Vec3i[4];
        vecs[0] = new Vec3i(bpeast, bpy, bpz); //east
        vecs[1] = new Vec3i(bpwest, bpy, bpz); //west
        vecs[2] = new Vec3i(bpx, bpy, bpsouth); //south
        vecs[3] = new Vec3i(bpx, bpy, bpnorth); //north

        Level level = ent.level();

        //iterate through the array and check if any of the blocks are valid climbing blocks 
        //if they are exit early with true
        for(int i = 0; i < 4; i++){
            BlockState state = level.getBlockState(new BlockPos(vecs[i]));

            
            if (tinyCanClimb(state, ent)) {
                return true;
            }
            
        }

        
        return false;
    }

    private boolean tinyCanClimb(BlockState state, LivingEntity ent){

        if (!ent.horizontalCollision) {
            return false;
        }

        if ((ent.getMainHandItem().is(Items.SLIME_BALL) || ent.getOffhandItem().is(Items.SLIME_BALL))) {
            return true;
        }

        if(state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.WOOL) || state.is(BlockTags.WOOL_CARPETS)){
            return true;
        }

        return false;
    }
}
