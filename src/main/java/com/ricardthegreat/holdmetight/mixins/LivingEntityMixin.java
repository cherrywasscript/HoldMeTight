package com.ricardthegreat.holdmetight.mixins;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    


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
