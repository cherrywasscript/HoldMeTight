package com.ricardthegreat.holdmetight.mixins.menu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.items.EntityStandinItem;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

@Mixin(AbstractContainerMenu.class)
public class PreventPlayerItemInteractionMixin {
    @Shadow NonNullList<Slot> slots;
    
    @Inject(at = @At("HEAD"), method = "clicked(IILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V", cancellable = true)
    public void clicked(int slot, int mouse, ClickType click, Player player, CallbackInfo info) {
        if (!(slot < 0 || slot >= slots.size())) {
            ItemStack stack = slots.get(slot).getItem();

            if (stack.getItem() instanceof EntityStandinItem) {
                AbstractContainerMenu rep = (AbstractContainerMenu) (Object) this;
                if (!(rep instanceof InventoryMenu || rep instanceof CuriosContainer || rep instanceof CuriosContainerV2)) {
                    info.cancel();
                }
            }
        }
    }
}
