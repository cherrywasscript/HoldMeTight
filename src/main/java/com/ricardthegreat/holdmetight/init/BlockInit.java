package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.blocks.JarBlock;
import com.ricardthegreat.holdmetight.blocks.MushroomTeleporterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HoldMeTight.MODID);

    public static final RegistryObject<MushroomTeleporterBlock> MUSHROOM_HOUSE = BLOCKS.register("mushroom_house", 
        () -> new MushroomTeleporterBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM)));

    public static final RegistryObject<JarBlock> TINY_JAR = BLOCKS.register("tiny_jar", 
        () -> new JarBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
}
