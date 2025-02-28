package com.ricardthegreat.holdmetight.init;

import static com.ricardthegreat.holdmetight.init.CreativeTabInit.addToTab;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.blocks.MushroomTeleporterBlock;
import com.ricardthegreat.holdmetight.items.AdvancedSizeRemote;

import net.minecraft.world.item.Item;
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

}
