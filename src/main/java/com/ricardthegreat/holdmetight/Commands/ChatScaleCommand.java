package com.ricardthegreat.holdmetight.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ricardthegreat.holdmetight.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.minecraft.world.entity.Entity;

public class ChatScaleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("chatscale")
            .then(Commands.argument("enable/disable", BoolArgumentType.bool()).executes((source) -> {
                return run(source.getSource(), BoolArgumentType.getBool(source, "enable/disable"), source.getSource().getEntityOrException());
            })));
    }

    public static int run(CommandSourceStack command, boolean enableDisable, Entity ent) throws CommandSyntaxException {
        if(enableDisable){
            Config.playerChatScale = true;
        }else{
            Config.playerChatScale = false;
        }

        return 1;
    }
    
}
