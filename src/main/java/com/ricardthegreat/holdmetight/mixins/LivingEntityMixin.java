package com.ricardthegreat.holdmetight.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.WorldData;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    @ModifyReturnValue(method = "onClimbable()Z", at = @At("RETURN"))
    public boolean onClimbable(boolean original) {

        LivingEntity ent = (LivingEntity) (Object) this;

        if (original) {
            System.out.println(ent.getDeltaMovement());
            return original;
        }

        //checks if entity is .5 or less and is player
        if (ent.isSpectator() || SizeUtils.getSize(ent) > 0.5 || !(ent instanceof Player)) {
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
    public boolean checkForBlock(LivingEntity ent){

        //the radius of the entitys hitbox
        double bbradius = ent.getBbWidth()/2;

        //their current x and z pos
        double xpos = ent.position().x;
        double zpos = ent.position().z;

        //the x y and z coords of their current block position, floored if x or z are negative so it gives the correct block
        int bpx = (xpos > 0) ?  (int) Math.ceil(xpos) : (int) Math.floor(xpos);
        int bpy = (int) ent.position().y;
        int bpz = (zpos > 0) ?  (int) Math.ceil(zpos) : (int) Math.floor(zpos);

        //finding the block positions of what is next to their hitbox on the positive x and z
        int bpxpos = (xpos > 0) ?  (int) Math.ceil(xpos+bbradius) : (int) Math.floor(xpos+bbradius);
        int bpzpos = (zpos > 0) ?  (int) Math.ceil(zpos+bbradius) : (int) Math.floor(zpos+bbradius);

        //does the same for the negative x and z but needs an extra step as for an example a player on x47.5 with a hitbox radius of .5 would return
        //x47, this gives the block they are standing on not the one next to them at x46.
        int bpxneg = (xpos > 0) ?  (int) Math.ceil(xpos-bbradius) : (int) Math.floor(xpos-bbradius);
        int bpzneg = (zpos > 0) ?  (int) Math.ceil(zpos-bbradius) : (int) Math.floor(zpos-bbradius);
        bpxneg = (bpxneg == xpos-bbradius) ? bpxneg-1 : bpxneg;
        bpzneg = (bpzneg == zpos-bbradius) ? bpzneg-1 : bpzneg;


        //add the 4 potential blocks to the array
        List<Vec3i> vecs = new ArrayList<>();
        vecs.add(new Vec3i(bpxpos, bpy, bpz));
        vecs.add(new Vec3i(bpxneg, bpy, bpz));
        vecs.add(new Vec3i(bpx, bpy, bpzpos));
        vecs.add(new Vec3i(bpx, bpy, bpzneg));

        boolean dirt = false;

        //iterate through the array and check if any of the blocks are valid climbing blocks
        for(int i = 0; i < 4; i++){
            if(ent.level().getBlockState(new BlockPos(vecs.get(i))).is(BlockTags.DIRT)){
                dirt = true;
            }
        }

        return dirt;
        
    }

    
}
