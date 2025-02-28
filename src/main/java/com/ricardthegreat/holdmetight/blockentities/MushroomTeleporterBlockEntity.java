package com.ricardthegreat.holdmetight.blockentities;

import java.util.LinkedList;
import java.util.Queue;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MushroomTeleporterBlockEntity extends BlockEntity{

    private int houseNum = -1;
    private boolean structureGenerated = false;
    private Vec3i housePos = null;

    public MushroomTeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.MUSHROOM_HOUSE_ENTITY.get(), pos, state);
    }
    
    public int getHouseNum(){
        return houseNum;
    }

    public void setHouseNum(int houseNum){
        this.houseNum = houseNum;
    }

    public boolean hasHouseNum(){
        return  houseNum != -1 ? true : false;
    }

    public boolean getStructureGenerated(){
        return structureGenerated;
    }

    public void setStructureGenerated(boolean structureGenerated){
        this.structureGenerated = structureGenerated;
    }

    public Vec3i getHousePos() {
        if (housePos == null) {
            this.housePos = genSpiralHousePos();
        }
        return housePos;
    }

    public void setHousePos(Vec3i housePos) {
        this.housePos = housePos;
    }

    private Vec3i genSpiralHousePos(){
        //0 right, 1 down, 2 left, 3 up
        Queue<Integer> dir = new LinkedList<>();
        dir.add(0);
        dir.add(1);
        dir.add(2);
        dir.add(3);

        int dist = 1;
        int distIncrease = 1;


        int[] pos = new int[] {0,0};

        //confusing and ill probably forget but
        //basic idea is that it moves 1 step in direction provided by dir and once it has moved far enough shown by dist it turns
        //achived by moving dir to the next direction (next number)
        //the total distance to move increases by 1 after down and up (1 and 3)
        for(int i = 0; i < houseNum; i++){
            switch (dir.peek()) {
                case 0:
                    pos[0]++;
                    dist--;
                    break;
                case 1:
                    pos[1]--;
                    dist--;
                    break;
                case 2:
                    pos[0]--;
                    dist--;
                    break;
                case 3:
                    pos[1]++;
                    dist--;
                    break;
                default:
                    break;
            }
            if (dist == 0) {
                if (dir.peek() == 1 || dir.peek() == 3) {
                    dist = 1 + distIncrease;
                    distIncrease++;
                }else{
                    dist = distIncrease;
                }
                dir.add(dir.poll());
            }
        }
        

        return new Vec3i(pos[0]*25, 64, pos[1]*25);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag mushroomTeleporterData = nbt.getCompound(HoldMeTight.MODID);

        this.houseNum = mushroomTeleporterData.getInt("houseNum");
        this.structureGenerated = mushroomTeleporterData.getBoolean("structureGenerated");
        
        if (structureGenerated) {
            this.housePos = new Vec3i(mushroomTeleporterData.getInt("xpos"), mushroomTeleporterData.getInt("ypos"), mushroomTeleporterData.getInt("zpos"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        var mushroomTeleporterData = new CompoundTag();

        mushroomTeleporterData.putInt("houseNum", houseNum);
        mushroomTeleporterData.putBoolean("structureGenerated", structureGenerated);
        if (housePos != null) {
            mushroomTeleporterData.putInt("xpos", housePos.getX());
            mushroomTeleporterData.putInt("ypos", housePos.getY());
            mushroomTeleporterData.putInt("zpos", housePos.getZ());
        }
        


        nbt.put(HoldMeTight.MODID, mushroomTeleporterData);
    }
}
