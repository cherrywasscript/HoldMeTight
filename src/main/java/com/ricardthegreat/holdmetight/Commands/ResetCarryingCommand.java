package com.ricardthegreat.holdmetight.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ResetCarryingCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("resetCarrying").executes((source) -> {
                return run(source.getSource(), source.getSource().getEntityOrException());
            }));
    }

    public static int run(CommandSourceStack command, Entity ent) throws CommandSyntaxException {
        if (ent instanceof Player) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) ent);

            playerCarry.setCarrying(false);
            playerCarry.setCarryPosition(false, 0);

            playerCarry.setShouldSyncSimple(true);
        }
            
        return 1;
    }
}
