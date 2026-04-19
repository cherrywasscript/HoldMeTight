package com.ricardthegreat.holdmetight.mixins.pehkui;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin {

    @Shadow @Final protected ContainerLevelAccess access;


    //hijacking the method here because the pehkui thing for itemcombiner menu doesnt just work with a few things swapped around and i have no idea what
    //the mixin things with like method = "one, two, three" is there is no documentation for putting multiple methods into a single mixin method box
    //there's actually like very little mixin documentation atall unless im just a fucking idiot who cant google search which is only an 85% probability
    @Inject(method = "stillValid(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("HEAD"), cancellable = true)
    private void stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        Boolean bool = this.access.evaluate((p_39785_, p_39786_) -> {
            // Vanilla uses 64 which is 8*8. Default reach is 4.5 so add 3.5 padding
            // magic number so required Mixins can still target here
            double reachOld = 64.0;
            double reach = player.getAttributeValue(net.minecraftforge.common.ForgeMod.BLOCK_REACH.get()) + 3.5;
            float scale = PlayerSizeUtils.getSize(player);
            reach *= scale;
            double cons = 0.5 * scale;
            return !this.isValidBlock(p_39785_.getBlockState(p_39786_)) ? false : player.distanceToSqr((double)p_39786_.getX() + cons, (double)p_39786_.getY() + cons, (double)p_39786_.getZ() + cons) <= reach * reach;
        }, true);

        if (bool) {
            cir.setReturnValue(bool);
        }
    }

    @Shadow
    protected abstract boolean isValidBlock(BlockState p_39788_);
}
