package com.ricardthegreat.holdmetight.save;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class MushroomHouseSavedData extends SavedData{

    private int test = 0;   

    public static MushroomHouseSavedData create(){
        return new MushroomHouseSavedData();
    }

    public static MushroomHouseSavedData load(CompoundTag tag) {
        MushroomHouseSavedData data = create();
        int testInt = tag.getInt("test");
        data.test = testInt;
        return data;  
    }

    public static MushroomHouseSavedData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(MushroomHouseSavedData::load, MushroomHouseSavedData::create, "houses");
    }

    @Override
    public CompoundTag save(@Nonnull CompoundTag tag) {
        tag.putInt("test", test);
        return tag;
    }
      
    public int getTest(){
        return test;
    }
    
    public void setTest(int test){
        this.test = test;
        this.setDirty();
    }
}
