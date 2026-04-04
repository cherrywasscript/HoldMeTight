package com.ricardthegreat.holdmetight.mixins.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.utils.CheckNonInvSlotUtil;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(LivingEntityRenderer.class)
public class DontRenderInvisibleCarriedEntities<T extends LivingEntity, M extends EntityModel<T>> {
    

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", cancellable = true)
    public void render(T ent, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo info) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T, M>(ent, (LivingEntityRenderer) (Object) this, p_115310_, p_115311_, p_115312_, p_115313_))) return;

        
        if (ent.getVehicle() instanceof Player vehicle) {
            PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability(vehicle);

            CarryPosition carryPos = vehicleCarry.getCarryPosition(ent, CheckNonInvSlotUtil.checkIfNonInvSlot(vehicle, ent));

            if (carryPos.posName.equals("torso")) {
                info.cancel();
            }
        } 
        
    }
}
