package com.ricardthegreat.holdmetight.portal;

import java.util.function.Function;



import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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
