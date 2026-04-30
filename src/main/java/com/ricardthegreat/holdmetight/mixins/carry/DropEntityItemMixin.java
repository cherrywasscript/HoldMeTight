package com.ricardthegreat.holdmetight.mixins.carry;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class DropEntityItemMixin {
    
    @Inject(at = @At("HEAD"), method = ("drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;"), cancellable = true)
    public void drop(ItemStack stack, boolean bool, CallbackInfoReturnable<ItemEntity> info) {
        if (stack.getItem() instanceof EntityStandinItem) {
            stack.getItem().onDroppedByPlayer(stack, (Player) (Object) this);
        }
    }

    @Inject(at = @At("HEAD"), method = ("drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"), cancellable = true)
    public void drop(ItemStack stack, boolean bool0, boolean bool1, CallbackInfoReturnable<ItemEntity> info) {
        if (stack.getItem() instanceof EntityStandinItem) {
            stack.getItem().onDroppedByPlayer(stack, (Player) (Object) this);
        }
    }
}
