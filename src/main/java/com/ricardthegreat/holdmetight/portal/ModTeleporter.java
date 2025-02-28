package com.ricardthegreat.holdmetight.portal;

import java.util.function.Function;



import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class ModTeleporter implements ITeleporter{
    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideDimension = true;

    public ModTeleporter(BlockPos pos, boolean insideDim) {
        thisPos = pos;
        insideDimension = insideDim;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        /* 
        entity = repositionEntity.apply(false);
        int y = 61;

        if (!insideDimension) {
            y = thisPos.getY();
        }

        BlockPos destinationPos = new BlockPos(thisPos.getX(), y, thisPos.getZ());

        int tries = 0;
        while ((destinationWorld.getBlockState(destinationPos).getBlock() != Blocks.AIR) &&
                !destinationWorld.getBlockState(destinationPos).canBeReplaced(Fluids.WATER) &&
                (destinationWorld.getBlockState(destinationPos.above()).getBlock()  != Blocks.AIR) &&
                !destinationWorld.getBlockState(destinationPos.above()).canBeReplaced(Fluids.WATER) && (tries < 25)) {
            destinationPos = destinationPos.above(2);
            tries++;
        }

        entity.setPos(thisPos.getX(), thisPos.getY(), thisPos.getZ());
        */

        return repositionEntity.apply(false);
    }

    //this is what actually positions the player when they tp
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        System.out.println("thispos: " + thisPos);
        
        return new PortalInfo(thisPos.getCenter(), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }
    
    @Override
    public boolean isVanilla() {
        return false;
    }
}
