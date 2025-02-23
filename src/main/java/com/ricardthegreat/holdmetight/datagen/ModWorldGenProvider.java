package com.ricardthegreat.holdmetight.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.worldgen.dimension.ModDimensions;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider{

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
        .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(HoldMeTight.MODID));
    }
    
}
