package com.ricardthegreat.holdmetight.mixins;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class FluidPushEntMixin {
    
    @Shadow @Final
    protected abstract void setFluidTypeHeight(net.minecraftforge.fluids.FluidType type, double height);

    @Inject(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At("HEAD"), remap = false, cancellable = true)
    public void updateFluidHeightAndDoFluidPushing(Predicate<FluidState> shouldUpdate, CallbackInfo info) {
        Entity rep = (Entity) (Object) this;
        if (EntitySizeUtils.getSize(rep) != 1) {
            doFluidStuffBecauseOfInjectsBeingStupid(shouldUpdate, rep);
            info.cancel();
        }
    }

    //this is an exact copy of the updateFluidHeightAndDoFluidPushing code because for some fucking reason directly injecting into where the vec3 is set causes crashed with sinytra
    private void doFluidStuffBecauseOfInjectsBeingStupid(Predicate<FluidState> shouldUpdate, Entity entRep){
        AABB aabb = entRep.getBoundingBox().deflate(0.001D);
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.minY);
        int l = Mth.ceil(aabb.maxY);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        double d0 = 0.0D;
        boolean flag = entRep.isPushedByFluid();
        boolean flag1 = false;
        Vec3 vec3 = Vec3.ZERO;
        int k1 = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        it.unimi.dsi.fastutil.objects.Object2ObjectMap<net.minecraftforge.fluids.FluidType, org.apache.commons.lang3.tuple.MutableTriple<Double, Vec3, Integer>> interimCalcs = new it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap<>(net.minecraftforge.fluids.FluidType.SIZE.get() - 1);

        for(int l1 = i; l1 < j; ++l1) {
        for(int i2 = k; i2 < l; ++i2) {
            for(int j2 = i1; j2 < j1; ++j2) {
                blockpos$mutableblockpos.set(l1, i2, j2);
                FluidState fluidstate = entRep.level().getFluidState(blockpos$mutableblockpos);
                net.minecraftforge.fluids.FluidType fluidType = fluidstate.getFluidType();
                if (!fluidType.isAir() && shouldUpdate.test(fluidstate)) {
                    double d1 = (double)((float)i2 + fluidstate.getHeight(entRep.level(), blockpos$mutableblockpos));
                    if (d1 >= aabb.minY) {
                    flag1 = true;
                    org.apache.commons.lang3.tuple.MutableTriple<Double, Vec3, Integer> interim = interimCalcs.computeIfAbsent(fluidType, t -> org.apache.commons.lang3.tuple.MutableTriple.of(0.0D, Vec3.ZERO, 0));
                    interim.setLeft(Math.max(d1 - aabb.minY, interim.getLeft()));
                    if (entRep.isPushedByFluid(fluidType)) {
                        Vec3 vec31 = fluidstate.getFlow(entRep.level(), blockpos$mutableblockpos);
                        vec31 = setTheVecCorrectly(vec31, entRep);
                        if (interim.getLeft() < 0.4D) {
                            vec31 = vec31.scale(interim.getLeft());
                        }

                        interim.setMiddle(interim.getMiddle().add(vec31));
                        interim.setRight(interim.getRight() + 1);
                    }
                    }
                }
            }
        }
        }

        interimCalcs.forEach((fluidType, interim) -> {
        if (interim.getMiddle().length() > 0.0D) {
        if (interim.getRight() > 0) {
            interim.setMiddle(interim.getMiddle().scale(1.0D / (double)interim.getRight()));
        }

        if (!(entRep instanceof Player)) {
            interim.setMiddle(interim.getMiddle().normalize());
        }

        Vec3 vec32 = entRep.getDeltaMovement();
        interim.setMiddle(interim.getMiddle().scale(entRep.getFluidMotionScale(fluidType)));
        double d2 = 0.003D;
        if (Math.abs(vec32.x) < 0.003D && Math.abs(vec32.z) < 0.003D && interim.getMiddle().length() < 0.0045000000000000005D) {
            interim.setMiddle(interim.getMiddle().normalize().scale(0.0045000000000000005D));
        }

        entRep.setDeltaMovement(entRep.getDeltaMovement().add(interim.getMiddle()));
        }

        this.setFluidTypeHeight(fluidType, interim.getLeft());
        });
    }

    private Vec3 setTheVecCorrectly(Vec3 vec, Entity rep){
        float size = EntitySizeUtils.getSize(rep);

        if (size >= 4) {
            vec = Vec3.ZERO;
        }else{
            vec = vec.scale(Math.min(1/EntitySizeUtils.getSize(rep), 100)); //i've capped this at 100x just to stop it getting silly
        }
        return vec;
    }

    //TODO This would be way better but for whatever reason this just does not function with synatra connector and i have no idea why, honestly really fucking annoying
    /*
    @ModifyVariable(method = "updateFluidHeightAndDoFluidPushing(Ljava/util/function/Predicate;)V", at = @At("STORE"), ordinal = 1)
    public Vec3 updateFluidHeightAndDoFluidPushing(Vec3 vec){
        Entity rep = (Entity) (Object) this;
        if ( rep instanceof Player) {
            System.out.println(vec);
        }

        float size = EntitySizeUtils.getSize(rep);

        if (size >= 4) {
            vec = Vec3.ZERO;
        }else{
            vec = vec.scale(Math.min(1/EntitySizeUtils.getSize(rep), 100)); //i've capped this at 100x just to stop it getting silly
        }
        return vec;
    }
        */
}
