package com.ricardthegreat.holdmetight.worldgen.structures;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ricardthegreat.holdmetight.blockentities.MushroomTeleporterBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.phys.AABB;

public class MushroomHouseStructureGenerator {

    public static boolean generated = false;
    
    public static void generateMushroomHouseStructure(LevelAccessor level, MushroomTeleporterBlockEntity house){
        BlockPos pos = new BlockPos(house.getHousePos());

        //9x0x9 area at given pos
        AABB floor = new AABB(pos, pos).inflate(5,0,5);

        BlockPos posEast = pos.east(6);
        AABB eastWall = new AABB(posEast.above(), posEast.above()).inflate(0,5,5);

        //generate flat platform of grass with dimensions of given aabb, not sure what the final int is for
        BlockPos.betweenClosedStream(floor).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.GRASS_BLOCK.defaultBlockState(), 0));

        BlockPos.betweenClosedStream(eastWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
    }
    
}
