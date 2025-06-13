package com.ricardthegreat.holdmetight.mixins.climbing;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

//TODO remove?
@Mixin(LivingEntity.class)
public abstract class EntityClimbingMixin2 extends Entity{
    
    public EntityClimbingMixin2(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow protected boolean jumping;
    @Shadow private Optional<BlockPos> lastClimbablePos;

    private boolean onTop = false;

    //@Inject(at = @At("HEAD"), method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", cancellable = true)
    //@ModifyVariable(at = @At("HEAD"), method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0)
    @Override
    public void move(MoverType mover, Vec3 vec3){
        LivingEntity ent = ((LivingEntity) (Object) this);
        if (onTop && vec3.y < 0) {
            this.resetFallDistance();

            if (ent instanceof Player && this.jumping) {
                Player player = (Player) ent;
                player.jumpFromGround();
            }
        }

        super.move(mover, vec3);
    }

    //@Inject(at = @At("RETURN"), method = "getBlockPosBelowThatAffectsMyMovement()Lnet/minecraft/core/BlockPos;", cancellable = true)
    @Override
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        Entity ent = ((Entity) (Object) this);
        if (ent instanceof Player) {
            Player entity = (Player) ent;
            
            List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().expandTowards(0, -1, 0));

            if (!list.isEmpty()) {
                if (onTop(entity, list.get(0))) {
                    //System.out.println(entity.level().getBlockState(list.get(0).blockPosition().below()).getBlock().getName());
                    return list.get(0).blockPosition().below();
                }
            }
        }

        return super.getBlockPosBelowThatAffectsMyMovement();
    }
    
    //TODO change this from injecting into travel to something more appropriate
    //@Inject(at = @At("HEAD"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V", cancellable = true)
    //@ModifyVariable(at = @At("TAIL"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0)
    @Inject(at = @At("HEAD"), method = "tick()V", cancellable = true)
    public void tick(CallbackInfo info) {
        LivingEntity ent = ((LivingEntity) (Object) this);
        

        //TODO go through a few steps 1- check if holding climb item  2- check if inside hitbox (if so kick out left/right/up) 3-do the actual standing/climbing
        
        if (ent instanceof Player && holdingClimbingItem((Player) ent)) {
            Player player = (Player) ent;
            //horizontal check is performed in onclimbable in livingentmixin

            
            shuntIfInsideHitbox(player);
            checkEntVertical(player);
        }
        //return vec3;
    }


    


    //block and entity climbing
    @ModifyReturnValue(method = "onClimbable()Z", at = @At("RETURN"))
    public boolean onClimbable(boolean original) {

        LivingEntity ent = (LivingEntity) (Object) this;

        if (original) {
            return original;
        }

        //checks if entity is .8 or less and is player
        if (ent.isSpectator() || EntitySizeUtils.getSize(ent) >= 0.8 || !(ent instanceof Player)) {
            return false;
        }else{

            //checks if they are next to a climbable block
            if (checkForBlock(ent) ) {
                this.lastClimbablePos = Optional.of(ent.blockPosition());
                return true;
            }else if (checkClimbableEnt(ent)) {
                return true;
            }
                
            return false;
        }

    }

    private boolean checkClimbableEnt(LivingEntity entity){
        List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().inflate(1, 0, 1));
        if (!list.isEmpty()) {
            for(Entity collison : list){
                if (touchSide(entity, collison)) {
                    System.out.println(true);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean touchSide(LivingEntity player, Entity ent){
        AABB entBB = ent.getBoundingBox();
        AABB playerBB = player.getBoundingBox();

        //System.out.println(playerBB);

        boolean minX = entBB.minX == playerBB.maxX;
        boolean maxX = entBB.maxX == playerBB.minX;

        boolean minY = entBB.minY == playerBB.maxY;
        boolean maxY = entBB.maxY == playerBB.minY;
        
        return minX || maxX || minY || maxY;
    }
    













    private void shuntIfInsideHitbox(LivingEntity player){
        List<Entity> list = player.level().getEntities(player, player.getBoundingBox());
        
        if (!list.isEmpty()) {
            // if ent similar size then ignore
            for(Entity ent : list){
                if (player.getBoundingBox().intersects(ent.getBoundingBox())) {
                    if (upperHalfHitbox(player, ent)) {
                        player.setPos(player.position().x, ent.getBoundingBox().maxY, player.position().z);
                    }else{
                        Vec3 centreDistance = player.position().vectorTo(ent.position());
                        if (Math.abs(centreDistance.x) > Math.abs(centreDistance.z)) {
                            double x = findXPos(player, ent);
                            player.setPos(x, player.position().y, player.position().z);
                        }else{
                            double z = findZPos(player, ent);
                            player.setPos(player.position().x, player.position().y, z);
                        }
                    }
                }
            }
        }
    }

    private double findXPos(LivingEntity player, Entity ent){
        double entXPos = ent.position().x();
        double playerXPos = player.position().x();
        AABB entBB = ent.getBoundingBox();
        AABB playerBB = player.getBoundingBox();

        if (playerXPos > entXPos) {
            return (entBB.maxX + playerBB.maxX - playerBB.minX)/2;
        }else{
            return entBB.minX - (playerBB.maxX - playerBB.minX)/2;
        }
    }

    private double findZPos(LivingEntity player, Entity ent){
        double entZPos = ent.position().z();
        double playerZPos = player.position().z();
        AABB entBB = ent.getBoundingBox();
        AABB playerBB = player.getBoundingBox();

        if (playerZPos > entZPos) {
            return (entBB.maxZ + playerBB.maxZ - playerBB.minZ)/2;
        }else{
            return entBB.minZ - (playerBB.maxZ - playerBB.minZ)/2;
        }
    }

    private boolean upperHalfHitbox(LivingEntity player, Entity entity){
        double half = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY)/2;
        return player.position().y() > half;
    }


    private void checkEntVertical(Player entity){
        List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().expandTowards(0, -1, 0));
        
        if (!list.isEmpty()) {
            if (onTop(entity, list.get(0))) {
                //System.out.println("list exists");
                
                if (entity.getDeltaMovement().y < 0) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
                }
                if (entity.position().y < list.get(0).getBoundingBox().maxY) {
                    entity.setPos(entity.position().x, list.get(0).getBoundingBox().maxY,entity.position().z);    
                }
                
                //entity.hurtMarked = true;
                entity.setOnGround(true);
                entity.getAbilities().flying = false;
                entity.onUpdateAbilities();
                
                //entity.verticalCollisionBelow = true;
                
                //entity.hurtMarked = true;
                //entity.getAbilities().flying = false;
                                    
                //System.out.println(entity.hasImpulse);
                
                onTop = true;
                horizontalCollision = true;
            }else{
                onTop = false;
            }
        }else{
            onTop = false;
        }
    }

    private boolean onTop(LivingEntity player, Entity ent){
        AABB bb = ent.getBoundingBox();
        Vec3 pos = player.position();

        boolean inX = pos.x >= bb.minX && pos.x <= bb.maxX;
        boolean inZ = pos.z >= bb.minZ && pos.z <= bb.maxZ;

        //boolean onY = pos.y >= bb.minY && pos.y <= bb.maxY + (0.1f * EntitySizeUtils.getSize(ent));
        boolean onY = pos.y >= bb.maxY - (0.1f * EntitySizeUtils.getSize(ent)) && pos.y <= bb.maxY ;
        
        return inX && inZ && onY;
    }

    private boolean holdingClimbingItem(Player player){
        if ((player.getMainHandItem().is(Items.SLIME_BALL) || player.getOffhandItem().is(Items.SLIME_BALL))) {
            return true;
        }

        return false;
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
