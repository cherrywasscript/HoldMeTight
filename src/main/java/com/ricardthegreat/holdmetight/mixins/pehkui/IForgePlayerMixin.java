package com.ricardthegreat.holdmetight.mixins.pehkui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgePlayer;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(IForgePlayer.class)
public interface IForgePlayerMixin {
    /*
     * while this class doesnt replace the pehkui IForgePlayerMixin it does extend what it does to a new method so im putting it in here
     */

    @Overwrite(remap = false)
	default boolean canReachRaw(BlockPos pos, double padding) {
		final Player self = ((Player) this);

        return self.canReach(pos, padding);
	}

    @Overwrite(remap = false)
	default boolean canReachRaw(Entity entity, double padding) {
		final Player self = ((Player) this);

        //not using this though maybe i should?
        /* 
        final double range = self.getAttributeValue(ForgeMod.ENTITY_REACH.get());
		final double ret = range == 0.0 ? 0.0 : range + (double) (self.isCreative() ? 0.5 : 0);

        System.out.println("canreachraw");

		final float scale = ScaleUtils.getBlockReachScale(self);

        if (scale != 1.0f) {
            return self.isCloseEnough(self, self.getBlockReach()+padding);
        }
            */

        return self.canReach(entity, padding);
	}
}

