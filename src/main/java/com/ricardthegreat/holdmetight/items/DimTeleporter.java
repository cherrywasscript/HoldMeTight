package com.ricardthegreat.holdmetight.items;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.datagen.DataGenerators;
import com.ricardthegreat.holdmetight.portal.ModTeleporter;
import com.ricardthegreat.holdmetight.save.MushroomHouseSavedData;
import com.ricardthegreat.holdmetight.worldgen.dimension.ModDimensions;

import net.minecraft.client.gui.font.providers.UnihexProvider.Dimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class DimTeleporter extends Item{

    

    public DimTeleporter(Properties p_41383_) {
        super(p_41383_);
        //TODO Auto-generated constructor stub
    }

    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {

        if (player.canChangeDimensions()) {
            handleTeleport(player);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        } else {
            return super.use(level, player, hand);
        }
        
    }

    private void handleTeleport(Entity player) {
        if (player.level() instanceof ServerLevel serverlevel) {
            MinecraftServer minecraftserver = serverlevel.getServer();
            ResourceKey<Level> resourcekey = player.level().dimension() == ModDimensions.DIM_LEVEL_KEY ?
                    Level.OVERWORLD : ModDimensions.DIM_LEVEL_KEY;
            System.out.println("dimteleporter:44" + resourcekey);
            ServerLevel portalDimension = minecraftserver.getLevel(resourcekey);
            System.out.println("dimteleporter:46" + portalDimension);
            

            ServerLevel overworld = minecraftserver.getLevel(Level.OVERWORLD);
            if (portalDimension != null && !player.isPassenger()) {
                if(resourcekey == ModDimensions.DIM_LEVEL_KEY) {
                    System.out.println("dimteleporter:50");
                    player.changeDimension(portalDimension, new ModTeleporter(player.blockPosition(), true));
                } else {
                    System.out.println("dimteleporter:53");
                    player.changeDimension(overworld, new ModTeleporter(player.blockPosition(), false));
                }
            }
        }
    }
    
}
