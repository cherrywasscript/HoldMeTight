package com.ricardthegreat.holdmetight.data;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

public class HMTCurioDataProvider extends CuriosDataProvider{

    public HMTCurioDataProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<Provider> registries) {
        super(modId, output, fileHelper, registries);
    }

    @Override
    public void generate(Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("shoulderleft").size(1);

        this.createEntities("test").addPlayer().addSlots("shoulderLeft");
    }
    
}
