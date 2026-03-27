package com.ricardthegreat.holdmetight.mixins.pehkui;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AbstractContainerMenu.class)
public class ScreenHandlerMixinReplacement {
    
	//basically the entire issue with this class was that the Player and Block classes needed to be swapped

    @ModifyExpressionValue(method = { "method_17696", "func_216960_a", "lambda$stillValid$0" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
	private static double pehkui$canUse$xOffset(double value, Player player, Block block,  Level world, BlockPos pos, @Local double reach) {
		System.out.println("blockreach: "+ player.getBlockReach() + "/localblockreach:" + reach);
		return ScaleUtils.getBlockXOffset(pos, player);
	}
	
	@ModifyExpressionValue(method = { "method_17696", "func_216960_a", "lambda$stillValid$0" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
	private static double pehkui$canUse$yOffset(double value, Player player, Block block, Level world, BlockPos pos) {
		return ScaleUtils.getBlockYOffset(pos, player);
	}
	
	@ModifyExpressionValue(method = { "method_17696", "func_216960_a", "lambda$stillValid$0" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
	private static double pehkui$canUse$zOffset(double value, Player player, Block block, Level world, BlockPos pos) {
		return ScaleUtils.getBlockZOffset(pos, player);
	}

	//this was in a seperate class by the same name just in reach.compat instead of just compat
	//changed it from targeting the constant "64" to targeting the reach value set by the game
    @Dynamic
	@ModifyExpressionValue(method = { "method_17696", "func_216960_a", "lambda$stillValid$0" }, require = 0, expect = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"))
	private static double pehkui$canUse$distance(double original, @Local Player player) {
		//not sure how to mixin to a variable assignment when modifying a lambda expression so this will have to do 
		//once this value is generated normally it has 3.5 added to it so that at default it will become 8
		//then when it is actually utilised it is squared so the basic value is 64
		//i want to multiply that 64 by scale squared - e.g. if the scale is 2 then i want to do 2*2*64 and end up with 256
		/*
		 * F = final value
		 * S = scale
		 * R = reach value (original)
		 * C = constant (3.5)
		 * 
		 * (R+C)^2=F
		 * (4.5+3.5)^2=64
		 * 
		 * i can only edit R
		 * 
		 * if i F to equal ((R+C)^2)*(S^2)
		 * 
		 * if i ignore C temporarily by making R = R+C
		 * then R = R*S
		 * then R = R-C
		 * should work
		 * 
		 */
		final float scale = ScaleUtils.getBlockReachScale(player);

		if (scale > 1.0F) {
			original = original + 3.5;
			original = original*scale;
			original = original-3.5;
		}

		return original;
	}
}