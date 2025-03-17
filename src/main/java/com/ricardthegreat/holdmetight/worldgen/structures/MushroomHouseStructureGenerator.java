package com.ricardthegreat.holdmetight.worldgen.structures;

import com.ricardthegreat.holdmetight.blockentities.MushroomTeleporterBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;;

public class MushroomHouseStructureGenerator {

    public static boolean generated = false;
    
    public static void generateMushroomHouseStructure(LevelAccessor level, MushroomTeleporterBlockEntity house){
        BlockPos pos = new BlockPos(house.getHousePos());

        System.out.println("does this print before the lag starts?");

        //this is very laggy
        //i feel like there's probably far far better way of doing this, like i dont imagine all structures
        //need to be made using rectangles but maybe im wrong i should look it up
        genFloor(level, pos);
        System.out.println("floor done");
        genWalls(level, pos);
        System.out.println("walls done");
        genWallCorners(level, pos);
        System.out.println("wall corners done");
        genSecondFloor(level, pos);
        System.out.println("second floor done");
        genCap(level, pos);
        System.out.println("cap done");
        genCapCorners(level, pos);
        System.out.println("cap corners done");
    }
    //generates the ground floor
    private static void genFloor(LevelAccessor level, BlockPos pos){
        //9x0x9 area at given pos
        AABB floor = new AABB(pos, pos).inflate(5,0,5);

        //generate flat platform of grass with dimensions of given aabb, not sure what the final int is for
        BlockPos.betweenClosedStream(floor).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.GRASS_BLOCK.defaultBlockState(), 0));
    }

    //does the walls for the ground floor
    private static void genWalls(LevelAccessor level, BlockPos pos){

        pos = pos.above(4);

        BlockPos posEast = pos.east(6);
        BlockPos posWest = pos.west(6);
        BlockPos posNorth = pos.north(6);
        BlockPos posSouth = pos.south(6);

        AABB eastWall = new AABB(posEast, posEast).inflate(0,3,2);
        AABB westWall = new AABB(posWest, posWest).inflate(0,3,2);
        AABB northWall = new AABB(posNorth, posNorth).inflate(2,3,0);  
        AABB southWall = new AABB(posSouth, posSouth).inflate(2,3,0);


        BlockPos.betweenClosedStream(eastWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
        BlockPos.betweenClosedStream(westWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
        BlockPos.betweenClosedStream(northWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
        BlockPos.betweenClosedStream(southWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
    }
    //does the corners for the ground floor
    private static void genWallCorners(LevelAccessor level, BlockPos pos){
        //pos = pos.above(4);


        for(int j = 3; j < 6; j++){
            BlockPos posNorthEast = pos.north(j).east(8-j);
            BlockPos posNorthWest = pos.north(j).west(8-j);
            BlockPos posSouthEast = pos.south(j).east(8-j);
            BlockPos posSouthWest = pos.south(j).west(8-j);

            
            
            AABB eastWall = new AABB(posNorthEast, posNorthEast.above(6));
            AABB westWall = new AABB(posNorthWest, posNorthWest.above(6));
            AABB northWall = new AABB(posSouthEast, posSouthEast.above(6));  
            AABB southWall = new AABB(posSouthWest, posSouthWest.above(6));

            BlockPos.betweenClosedStream(eastWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(westWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(northWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(southWall).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.MUSHROOM_STEM.defaultBlockState(), 0));
        }
    }

    //generates the second floor including the hole
    private static void genSecondFloor(LevelAccessor level, BlockPos pos){
        pos = pos.above(7);

        AABB centre = new AABB(pos, pos).inflate(2,0,10);
        AABB centreHole = new AABB(pos, pos).inflate(2,0,5);

        BlockPos.betweenClosedStream(centre).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
        BlockPos.betweenClosedStream(centreHole).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.AIR.defaultBlockState(), 0));

        BlockPos posEast = pos.east(7);
        BlockPos posWest = pos.west(7);

        AABB east = new AABB(posEast.south(10), posEast.north(10).west());
        AABB west = new AABB(posWest.south(10), posWest.north(10).east());

        BlockPos.betweenClosedStream(east).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
        BlockPos.betweenClosedStream(west).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));


        for(int j = 3; j < 6; j++){
            posEast = pos.east(j);
            posWest = pos.west(j);

            east = new AABB(posEast, posEast).inflate(0,0,10);
            west = new AABB(posWest, posWest).inflate(0,0,10);
            AABB eastHole = new AABB(posEast, posEast).inflate(0,0,7-j);
            AABB westHole = new AABB(posWest, posWest).inflate(0,0,7-j);

            BlockPos.betweenClosedStream(east).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(west).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(eastHole).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.AIR.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(westHole).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.AIR.defaultBlockState(), 0));
        }

        for(int j = 8; j < 11; j++){
            posEast = pos.east(j);
            posWest = pos.west(j);

            east = new AABB(posEast, posEast).inflate(0,0,17-j);
            west = new AABB(posWest, posWest).inflate(0,0,17-j);

            BlockPos.betweenClosedStream(east).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
            BlockPos.betweenClosedStream(west).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
        }

        
    }
    
    //generates most of the mushroom cap
    private static void genCap(LevelAccessor level, BlockPos pos){
        pos = pos.above(8);

        AABB top = new AABB(pos, pos).inflate(11,0,11);
        AABB topHole = new AABB(pos, pos).inflate(10,0,10);

        BlockPos.betweenClosedStream(top).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
        BlockPos.betweenClosedStream(topHole).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.AIR.defaultBlockState(), 0));

        pos = pos.above(7);

        //fills the rest
        int[] widths = new int[]{2,5,7,8,9,10,11};
        int prevWidth = 0;
        
        for(int j = 0; j < 7; j++){
            top = new AABB(pos.below(j), pos.below(j)).inflate(widths[j],0,widths[j]);
            topHole = new AABB(pos.below(j), pos.below(j)).inflate(prevWidth,0,prevWidth);

            BlockPos.betweenClosedStream(top).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
            if (j != 0) {
                BlockPos.betweenClosedStream(topHole).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.AIR.defaultBlockState(), 0));
            }

            prevWidth = widths[j];
        }

        
    }

    private static void genCapCorners(LevelAccessor level, BlockPos pos){
        pos = pos.above(8);

        //this is for the 2block corners
        BlockPos posNE = pos;
        BlockPos posNW = pos;
        BlockPos posSE = pos;
        BlockPos posSW = pos;
        BlockPos[] posList = new BlockPos[]{posNE,posNW,posSE,posSW};
        for(int j = 8; j < 11; j++){
            posList[0] = pos.north(j).east(18-j);
            posList[1] = pos.north(j).west(18-j);
            posList[2] = pos.south(j).east(18-j);
            posList[3] = pos.south(j).west(18-j);
            for(int k = 0; k < 4; k++){
                AABB corner = new AABB(posList[k],posList[k].above());
                BlockPos.betweenClosedStream(corner).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
            }
        }

        //this is for the 1block corners 1 block above
        pos = pos.above(2);
        for(int j = 8; j < 10; j++){
            posList[0] = pos.north(j).east(17-j);
            posList[1] = pos.north(j).west(17-j);
            posList[2] = pos.south(j).east(17-j);
            posList[3] = pos.south(j).west(17-j);

            for(int k = 0; k < 4; k++){
                AABB corner = new AABB(posList[k],posList[k]);
                BlockPos.betweenClosedStream(corner).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
            }
        }

        //this is for the final 1block corner
        pos = pos.above();
        posList[0] = pos.north(8).east(8);
        posList[1] = pos.north(8).west(8);
        posList[2] = pos.south(8).east(8);
        posList[3] = pos.south(8).west(8);
        for(int j = 0; j < 4; j++){
            AABB corner = new AABB(posList[j],posList[j]);
            BlockPos.betweenClosedStream(corner).map(BlockPos::immutable).forEach(i -> level.setBlock(i, Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), 0));
        }
    }
}
