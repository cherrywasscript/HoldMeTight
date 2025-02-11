package com.ricardthegreat.unnamedsizemod.entities.projectile;

import com.ricardthegreat.unnamedsizemod.init.EntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RayGunProjectile extends Projectile {

    public RayGunProjectile(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    public RayGunProjectile(LivingEntity entity, Level level, float scale, boolean isMult) {
        this(EntityInit.RAY_GUN_PROJECTILE.get(), level);
        this.setOwner(entity);
        // currently originates the same as an ender pearl, need to do maths to get it
        // to come out of the raygun
        this.setPos(entity.getX(), entity.getEyeY() - (double) 0.1F, entity.getZ());
    }

    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS
                && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f = 0.99F;
        float f1 = 0.06F;
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale((double) 0.99F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double) -0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    protected void onHitEntity(EntityHitResult p_37241_) {
        super.onHitEntity(p_37241_);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity livingentity) {
            p_37241_.getEntity().hurt(this.damageSources().mobProjectile(this, livingentity), 1.0F);
        }

    }

    protected void onHitBlock(BlockHitResult p_37239_) {
        super.onHitBlock(p_37239_);
        if (!this.level().isClientSide) {
            this.discard();
        }

    }

    protected void defineSynchedData() {
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_150162_) {
        super.recreateFromPacket(p_150162_);
        double d0 = p_150162_.getXa();
        double d1 = p_150162_.getYa();
        double d2 = p_150162_.getZa();

        for (int i = 0; i < 7; ++i) {
            double d3 = 0.4D + 0.1D * (double) i;
            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
        }

        this.setDeltaMovement(d0, d1, d2);
    }
}
