package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.world.entity.player.Player;

import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

@Mixin(Player.class)
public abstract class PlayerCapabilityMixin {

    private ScaleData data = ((PehkuiEntityExtensions) (Player) (Object) this).pehkui_getScaleData(ScaleTypes.BASE);

    boolean shouldSync = false;

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo info){
        ((Player) (Object) this).getCapability(PlayerSizeProvider.PLAYER_SIZE).ifPresent(scale -> {
            scale.tick((Player) (Object) this);
        });

        ((Player) (Object) this).getCapability(PlayerCarryProvider.PLAYER_CARRY).ifPresent(scale -> {
            scale.tick((Player) (Object) this);
        });
    }
}
