package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

@Mixin(Player.class)
public abstract class PlayerCapabilityMixin {

    private float prevScale = 0.0f;

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo info){
        Player player = (Player) (Object) this;
        PehkuiEntityExtensions pEnt = (PehkuiEntityExtensions) player;
        ScaleData data = pEnt.pehkui_getScaleData(ScaleTypes.BASE);

        (player).getCapability(PlayerSizeProvider.PLAYER_SIZE).ifPresent(scale -> {
            scale.tick(player);
        });

        (player).getCapability(PlayerCarryProvider.PLAYER_CARRY).ifPresent(scale -> {
            scale.tick(player);
        });

        if (!player.level().isClientSide()) {
            if (data.getScale() != prevScale) {
                PlayerSizeUtils.setPeripheralScales(player);
            }
            prevScale = data.getScale();
        }
    }
}
