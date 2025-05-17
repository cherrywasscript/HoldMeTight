package com.ricardthegreat.holdmetight.mixins;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    @ModifyReturnValue(method = "onClimbable()Z", at = @At("RETURN"))
    public boolean onClimbable(boolean original) {

        LivingEntity ent = (LivingEntity) (Object) this;

        if (original) {
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


    //all of this stuff is to make players not emit particles when small hopefully

    @Shadow
    private Map<MobEffect, MobEffectInstance> activeEffects;
    @Shadow
    private boolean effectsDirty;
    @Shadow
    private static EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;
    @Shadow
    private static EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;

    @Shadow
    protected void onEffectUpdated(MobEffectInstance p_147192_, boolean p_147193_, @Nullable Entity p_147194_) {}
    @Shadow
    protected void onEffectRemoved(MobEffectInstance p_21126_) {}
    @Shadow
    protected void updateInvisibilityStatus() {}
    @Shadow
    private void updateGlowingStatus() {}


    //figure out how to make this the most important so that other mods that do this still have their particles removed for small folk
    @Inject(method = "tickEffects",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),cancellable = true)
    private void holdmetight$disableParticles(CallbackInfo info){
        if (EntitySizeUtils.getSize(this) <= Config.minParticleScale) {
            info.cancel();
        }
    }

    @Shadow
    public abstract void defineSynchedData();
    @Shadow
    public abstract void readAdditionalSaveData(CompoundTag p_20052_);
    @Shadow
    public abstract void addAdditionalSaveData(CompoundTag p_20139_);
}
