package com.ricardthegreat.holdmetight.mixins.rendering;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.ricardthegreat.holdmetight.init.ItemInit;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/*
 * this class exists to allow me to render a "frozen" tooltip for player items so that it is easier to view the inventory
 * may or may not keep this around in the future
 */
@Mixin(AbstractContainerScreen.class)
public abstract class CustomTooltip extends Screen{

    protected CustomTooltip(Component p_96550_) {super(p_96550_);}

    @Shadow
    Slot hoveredSlot;
    @Shadow
    int imageWidth;
    @Shadow
    int imageHeight;

    @Shadow
    protected abstract List<Component> getTooltipFromContainerItem(ItemStack p_283689_);

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;Lnet/minecraft/world/item/ItemStack;II)V"), cancellable = true)
    private void renderTooltip(GuiGraphics graphics, int x, int y, CallbackInfo info, @Local ItemStack itemstack){
        if (itemstack.is(ItemInit.PLAYER_ITEM.get())) {
            info.cancel();

            int imageLeft = (width/2) - (imageWidth/2);
            int imageTop = (height/2)- (imageHeight/2);

            int tooltipX = imageLeft+hoveredSlot.x+12;
            int tooltipY = imageTop+hoveredSlot.y+4;

            graphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, tooltipX, tooltipY);
        }
    }
}
