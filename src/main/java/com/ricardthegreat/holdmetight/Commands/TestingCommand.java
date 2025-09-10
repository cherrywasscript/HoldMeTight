package com.ricardthegreat.holdmetight.Commands;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TestingCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("test").executes((source) -> {
                return run(source.getSource(), source.getSource().getEntityOrException(), null);
            })
            .then(Commands.argument("player", EntityArgument.player()).executes((source) -> {
                return run(source.getSource(), source.getSource().getEntityOrException(), EntityArgument.getPlayer(source, "player"));
            }))
            );
    }

    //TODO make this work on both dedicated and client side
    public static int run(CommandSourceStack command, Entity ent, @Nullable ServerPlayer target) throws CommandSyntaxException {
        PlayerCarry playerCarry = null;
        if (ent != null) {
            if (target == null) {
                if (ent instanceof Player) {
                    playerCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) ent);
                }
            }else{
                playerCarry = PlayerCarryProvider.getPlayerCarryCapability(target);
            }

            if (playerCarry != null) {
                /* 
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer mcPlayer = minecraft.player;

                System.out.println("is carried: " + playerCarry.getIsCarried());
                HoldMeTight.LOGGER.atDebug().log("is carried: " + playerCarry.getIsCarried());

                System.out.println("is carrying: " + playerCarry.getIsCarrying());
                HoldMeTight.LOGGER.atDebug().log("is carrying: " + playerCarry.getIsCarrying());

                if (mcPlayer != null) {
                    mcPlayer.displayClientMessage(Component.literal("is carried: " + playerCarry.getIsCarried()), false);
                    mcPlayer.displayClientMessage(Component.literal("is carrying: " + playerCarry.getIsCarrying()), false);
                }

                playerCarry.setShouldSyncAll(true);
                */
            }
        }
        
            
        return 1;
    }
}
