package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class InvulnerableWhileCarried extends LivingEntity{
    protected InvulnerableWhileCarried(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public boolean isInvulnerable() {
        if (this.getVehicle() instanceof Player) {
            return true;
        }
        return super.isInvulnerable();
    }

    @Inject(at = @At("HEAD"), method = "isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z", cancellable = true)
    public void isInvulnerableTo(CallbackInfoReturnable<Boolean> info) {
        if (this.getVehicle() instanceof Player) {
            info.setReturnValue(true);
        }
    }
}
