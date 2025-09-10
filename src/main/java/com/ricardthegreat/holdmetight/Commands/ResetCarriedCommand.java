package com.ricardthegreat.holdmetight.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ResetCarriedCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("resetBeingCarried").requires((permission) -> {
                return permission.hasPermission(2);
            }).executes((source) -> {
                return run(source.getSource(), source.getSource().getEntityOrException());
            }));
    }

    public static int run(CommandSourceStack command, Entity ent) throws CommandSyntaxException {
        if (ent instanceof Player) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) ent);

            playerCarry.setCarried(false);
            
            playerCarry.setShouldSyncSimple(true);
        }
            
        return 1;
    }
}
