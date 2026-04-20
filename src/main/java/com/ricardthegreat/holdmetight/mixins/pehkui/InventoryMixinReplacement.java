package com.ricardthegreat.holdmetight.mixins.pehkui;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = Container.class, priority = 990)
public interface InventoryMixinReplacement {
	@Overwrite(remap = false)
	public static boolean stillValidBlockEntity(BlockEntity blockEntity, Player player, double range){
		Level level = blockEntity.getLevel();
		BlockPos pos = blockEntity.getBlockPos();
		if (level == null) {
			return false;
		}
		else if (level.getBlockEntity(pos) != blockEntity) {
			return false;
		}
		else {
			double x = ((double) pos.getX()) + 0.5D;
			double y = ((double) pos.getY()) + 0.5D;
			double z = ((double) pos.getZ()) + 0.5D;
			final Vec3 eyePos = player.getEyePosition();
			x = (x - 0.5D) + ScaleUtils.getBlockXOffset(pos, player) - (eyePos.x() - player.getX());
			y = (y - 0.5D) + ScaleUtils.getBlockYOffset(pos, player) - (eyePos.y() - player.getY());
			z = (z - 0.5D) + ScaleUtils.getBlockZOffset(pos, player) - (eyePos.z() - player.getZ());
			final double reach = ScaleUtils.getBlockReachScale(player) * (double) range;

			return player.distanceToSqr(x, y, z) <= reach * reach;
		}
	}
		
}
