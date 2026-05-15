package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(LivingEntity.class)
public class TinySuperJumpMixin {
    //TODO actually enable this class

    @ModifyReturnValue( at = @At("RETURN"), method = "getJumpPower()F")
	private float getJumpPower(float original) {
        if (((LivingEntity) (Object) this) instanceof Player player) {
            //TODO return to this later, i think just having it activate while shifting should be fine but might want to use
            //PlayerPreferences "enableVaulting" and have it be fully disableable
            if (player.isShiftKeyDown()) {
                if (EntitySizeUtils.getSize(player) < 0.8) {
                    if (checkForBlock(player)) {
                        return original+0.2f;
                    }
                }
            }
        }
        return original;
	}

    //TODO this is duplicate code from BlockClimbingMixin so probably fix that
    private boolean checkForBlock(Player ent){

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
        int i = -1;
        switch (ent.getDirection()) {
            case EAST:
                i = 0;
                break;
            case WEST:
                i = 1;
                break;
            case SOUTH:
                i = 2;
                break;
            case NORTH:
                i = 3;
                break;
            default:
                break;
        }

        if (i != -1) {
            BlockPos blockPos = new BlockPos(vecs[i]);
            BlockState state = level.getBlockState(blockPos);

            boolean preCheck = true;
            if (state.equals(level.getBlockState(ent.blockPosition()))) {
                if (state.isAir()) {
                    preCheck = false;
                }
            }

            BlockPos aboveBlockPos = blockPos.above();
            BlockState above = level.getBlockState(aboveBlockPos);

            //TODO make this better im kinda sleepy and am just making it functionable
            //also future me if you're reading this and not understanding it just know i could have explained it here but choose
            //not to because im tired *middle finger emoji*
            if (preCheck && ent.horizontalCollision) {
                if (above.getCollisionShape(level, aboveBlockPos).isEmpty() ) {
                    double height = state.getCollisionShape(level, blockPos).max(Direction.Axis.Y) + blockPos.getY();
                    if (height-ent.getY() <= 1 && height-ent.getY() <= 2.5*PlayerSizeUtils.getSize(ent)) {
                        return true;
                    }
                }else{
                    double height = above.getCollisionShape(level, aboveBlockPos).max(Direction.Axis.Y) + aboveBlockPos.getY();
                    if (height-ent.getY() <= 1.5*PlayerSizeUtils.getSize(ent)) {
                        return true;
                    }
                }
            }
        }
  
        return false;
    }
}
