package com.ricardthegreat.holdmetight.mixins.playerextensions;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

    @Override
    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (this.getVehicle() instanceof Player) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }
}
