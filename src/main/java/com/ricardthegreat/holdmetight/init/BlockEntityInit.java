package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.blockentities.MushroomTeleporterBlockEntity;
import com.ricardthegreat.holdmetight.blocks.MushroomTeleporterBlock;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HoldMeTight.MODID);

    public static final RegistryObject<BlockEntityType<MushroomTeleporterBlockEntity>> MUSHROOM_HOUSE_ENTITY = BLOCK_ENTITIES
            .register("example_menu_block",
                    () -> BlockEntityType.Builder.of(MushroomTeleporterBlockEntity::new, BlockInit.MUSHROOM_HOUSE.get())
                            .build(null));

}
