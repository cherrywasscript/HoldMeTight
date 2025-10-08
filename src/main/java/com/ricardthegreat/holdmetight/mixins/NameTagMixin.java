package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.items.CollarItem;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.inventory.CurioSlot;

@Mixin(Entity.class)
public class NameTagMixin {
    //(at = @At("RETURN"), method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z")
    @Inject(at = @At("HEAD"), method = "getName()Lnet/minecraft/network/chat/Component", cancellable = true)
    public void getName(CallbackInfoReturnable<Component> info) {
        Entity ent = (Entity) (Object) this;
        if (ent instanceof Player) {
            Player player = (Player) ent;
            
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler("collar").ifPresent(stacksHandler -> {
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                        IDynamicStackHandler cosmeticStacksHandler = stacksHandler.getCosmeticStacks();

                        for (int i = 0; i < stackHandler.getSlots(); i++) {
                            ItemStack stack = cosmeticStacksHandler.getStackInSlot(i);
                            if (stack.getItem() instanceof CollarItem) {
                                System.out.println("collar item");
                            }else{
                                System.out.println("not collar item");
                            }
                        }

                    }));
        }
    }
}
