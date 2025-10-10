package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    //all of this stuff is to make players not emit particles when small hopefully
    //figure out how to make this the most important so that other mods that do this still have their particles removed for small folk
    @Inject(method = "tickEffects",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),cancellable = true)
    private void holdmetight$disableParticles(CallbackInfo info){
        if (EntitySizeUtils.getSize(this) <= Config.minParticleScale) {
            info.cancel();
        }
    }
    
    @Inject(at = @At("RETURN"), method = "isInWall()Z", cancellable = true)
    private void holdmetight$isInWall(CallbackInfoReturnable<Boolean> info){
        if (info.getReturnValue()) {
            if ((Entity) (Object) this instanceof Player) {
                Player player = (Player) (Object) this;
                if (PlayerCarryProvider.getPlayerCarryCapability(player).getIsCarried()) {
                    info.setReturnValue(false);
                }         
            }
        }
    }
}
