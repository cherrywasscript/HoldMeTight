package com.ricardthegreat.holdmetight.utils;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockHitboxHelper {
    
    /**
     * 
     * @param width The width of the box
     * @param height The height of the box
     * @param length The length of the box
     * @param x where the box starts on the x axis
     * @param y where the box starts on the y axis
     * @param z where the box starts on the z axis
     * @param rotation an int from 0-3 0 is no rotation 1 is 90 degrees 2 is 180 3 is 270
     * @return
     */
    public static VoxelShape easyBox(double width, double height, double length, double x, double y, double z, int rotation){
        rotation = rotation%4;
        double rotAngle = Math.toRadians(rotation*90);

        double x1 = x + width;
        double y1 = y + height;
        double z1 = z + length;


        // rotate the initial coords around the block centre
        double rotX = x-8;
        double rotZ = z-8;

        x = rotX*Math.cos(rotAngle) - rotZ*Math.sin(rotAngle);
        z = rotX*Math.sin(rotAngle) + rotZ*Math.cos(rotAngle);

        x = x+8;
        z = z+8;

        //rotate the ending coords around the block centre
        rotX = x1-8;
        rotZ = z1-8;

        x1 = rotX*Math.cos(rotAngle) - rotZ*Math.sin(rotAngle);
        z1 = rotX*Math.sin(rotAngle) + rotZ*Math.cos(rotAngle);

        x1 = x1+8;
        z1 = z1+8;

        //make sure the first x is smaller than the second x
        double temp = Math.max(x, x1);
        x = Math.min(x, x1);
        x1 = temp;
    
        //do the same with z
        temp = Math.max(z, z1);
        z = Math.min(z, z1);
        z1 = temp;

        return Block.box(x, y, z, x1, y1, z1);
    }

    public static VoxelShape massOr(List<VoxelShape> shapes){
        VoxelShape base = null;
        for (VoxelShape voxelShape : shapes) {
            if (voxelShape != null) {
                if (base == null) {
                    base = voxelShape;
                }else{
                    base = Shapes.or(base, voxelShape);
                }
            }
        }

        return base;
    }
}
