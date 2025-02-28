package com.ricardthegreat.holdmetight.blocks;

import javax.annotation.Nullable;

import com.ricardthegreat.holdmetight.blockentities.MushroomTeleporterBlockEntity;
import com.ricardthegreat.holdmetight.init.BlockEntityInit;
import com.ricardthegreat.holdmetight.portal.ModTeleporter;
import com.ricardthegreat.holdmetight.save.MushroomHouseSavedData;
import com.ricardthegreat.holdmetight.worldgen.dimension.ModDimensions;
import com.ricardthegreat.holdmetight.worldgen.structures.StructureGenerator;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

public class MushroomTeleporterBlock extends Block implements EntityBlock{

    public MushroomTeleporterBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.MUSHROOM_HOUSE_ENTITY.get().create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        BlockEntity bEntity = level.getBlockEntity(pos);

        if(!(bEntity instanceof MushroomTeleporterBlockEntity)){
            System.out.println("nope");
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        
        MushroomTeleporterBlockEntity house = (MushroomTeleporterBlockEntity) bEntity;
        
        if (player.getServer() != null) {
            MushroomHouseSavedData m = MushroomHouseSavedData.getData(player.getServer());

            //debug thing to reset the number
            if (player.isShiftKeyDown()) {
                m.setTest(0);
            }
            
            if(!house.hasHouseNum()){
                house.setHouseNum(m.getTest());
                m.setTest(m.getTest()+1);
            }
        }
        
        System.out.println(house.getHouseNum());

        if (player.canChangeDimensions()) {
            handleTeleport(player, pos, house);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }


    private void handleTeleport(Entity player, BlockPos pos, MushroomTeleporterBlockEntity house) {
        if (player.level() instanceof ServerLevel serverlevel) {
            MinecraftServer minecraftserver = serverlevel.getServer();
            ResourceKey<Level> resourcekey = player.level().dimension() == ModDimensions.DIM_LEVEL_KEY ?
                    Level.OVERWORLD : ModDimensions.DIM_LEVEL_KEY;

            ServerLevel portalDimension = minecraftserver.getLevel(resourcekey);
            ServerLevel overworld = minecraftserver.getLevel(Level.OVERWORLD);

            if (portalDimension != null && !player.isPassenger()) {
 
                //teleport to and from dimension
                if(resourcekey == ModDimensions.DIM_LEVEL_KEY) {



                    //generate structure if it doesnt already exist
                    if (!house.getStructureGenerated()) {
                        System.out.println("generated");
                        house.setStructureGenerated(true);
                        StructureGenerator.generateMushroomHouseStructure(portalDimension, house);
                    }
                    BlockPos b = new BlockPos(house.getHousePos());
                    
                    ModTeleporter t = new ModTeleporter(b, true);
                    
                    //need to get pos
                    player.changeDimension(portalDimension, t);
                    //Vec3 playerPos = player.changeDimension(portalDimension, t).position();
                    //player.moveTo(playerPos);
                    //System.out.println(playerPos);

                    //System.out.println("block: " + serverlevel.getBlockState(b).getBlock());
                } else {
                    BlockPos b = new BlockPos(house.getHousePos());
                    System.out.println("dimteleporter:53");
                    player.changeDimension(overworld, new ModTeleporter(b, false));
                }
            }
        }
    }

    
}
