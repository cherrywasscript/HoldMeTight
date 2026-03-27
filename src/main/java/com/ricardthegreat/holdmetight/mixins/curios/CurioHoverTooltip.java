package com.ricardthegreat.holdmetight.mixins.curios;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.utils.constants.CarryPosConstants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import top.theillusivec4.curios.api.client.ICuriosScreen;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

@Mixin(CuriosScreenV2.class)
public abstract class CurioHoverTooltip extends EffectRenderingInventoryScreen<CuriosContainer> implements RecipeUpdateListener, ICuriosScreen {
    
    public CurioHoverTooltip(CuriosContainer p_98701_, Inventory p_98702_, Component p_98703_) {
        super(p_98701_, p_98702_, p_98703_);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;II)V"), cancellable = true)
    private void renderTooltip(GuiGraphics graphics, int x, int y, float partialTicks, CallbackInfo info, @Local CurioSlot curioSlot){
        if (curioSlot.getIdentifier().equals(CarryPosConstants.CUSTOM)) {
            if (this.minecraft != null) {
                if (this.minecraft.player != null) {
                    PlayerCarry carry = PlayerCarryProvider.getPlayerCarryCapability(this.minecraft.player);
                    
                    CarryPosition pos = carry.getCarryPosition(this.minecraft.player, curioSlot.getIdentifier() + curioSlot.getContainerSlot());
                    graphics.renderTooltip(this.font, Component.literal(pos.posName + " (Custom Carry)"), x, y);
                    info.cancel();
                }
            }
        }
    }
  
}
