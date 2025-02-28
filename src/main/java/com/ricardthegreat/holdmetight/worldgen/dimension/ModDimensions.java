package com.ricardthegreat.holdmetight.worldgen.dimension;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import com.ricardthegreat.holdmetight.HoldMeTight;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class ModDimensions {

        
    public static final ResourceKey<LevelStem> DIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(HoldMeTight.MODID, "dim"));
    public static final ResourceKey<Level> DIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(HoldMeTight.MODID, "dim"));
    public static final ResourceKey<DimensionType> DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(HoldMeTight.MODID, "dim_type"));


    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        HolderGetter<StructureSet> structureGenSettings = context.lookup(Registries.STRUCTURE_SET);
        HolderGetter<PlacedFeature> placedFeatureGenSettings = context.lookup(Registries.PLACED_FEATURE);

        //possibly works to make flat with no structures
        //honestly might need to make my own generator if i dont want mobs spawning?
        HolderSet<StructureSet> t = HolderSet.direct(structureGenSettings.getOrThrow(BuiltinStructureSets.VILLAGES));
        FlatLevelSource flatChunkGenerator = new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.of(t), biomeRegistry.getOrThrow(Biomes.THE_VOID), FlatLevelGeneratorSettings.createLakesList(placedFeatureGenSettings)));
        
        

        //wrapped chunk gen only does 1 biome type
        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                //change biome here
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.THE_VOID)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.END));


        //while this multinoisebiomesource does multiple biomes based somehow on the params set
        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.PLAINS)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OCEAN)),
                                Pair.of(
                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.DARK_FOREST))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.DIM_TYPE), flatChunkGenerator);

        context.register(DIM_KEY, stem);
    }
    
}
