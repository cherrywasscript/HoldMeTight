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

public class StructureGenerator {

    public static boolean generated = false;
    
    public static void generateMushroomHouseStructure(LevelAccessor level, MushroomTeleporterBlockEntity house){
        BlockPos pos = new BlockPos(house.getHousePos());

        //9x0x9 area at given pos
        AABB aabb = new AABB(pos, pos).inflate(5,0,5);

        //generate flat platform of grass with dimensions of given aabb, not sure what the final int is for
        BlockPos.betweenClosedStream(aabb).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.GRASS_BLOCK.defaultBlockState(), 0));;
    }
    
}
