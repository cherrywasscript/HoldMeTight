package com.ricardthegreat.holdmetight.Commands;

import javax.annotation.Nullable;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.coordinates.LocalCoordinates;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("rotation")
            .then(Commands.argument("angle", IntegerArgumentType.integer())
            .then(Commands.argument("distance", DoubleArgumentType.doubleArg())
            .then(Commands.argument("height", DoubleArgumentType.doubleArg())
            .then(Commands.argument("sideways", DoubleArgumentType.doubleArg()).executes((source) -> {
                return run(source.getSource(), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), source.getSource().getEntityOrException());
            })))))
            );
    }

    public static int run(CommandSourceStack command, int rotation, double distance, double height, double sideways, Entity ent) throws CommandSyntaxException {
        if(rotation >= 360){
            rotation = 0;
        }
        if(ent instanceof Player){
            PlayerCarryExtension pl = (PlayerCarryExtension) ent;
            pl.setRotationOffset(rotation);
            pl.setXYMult(distance);
            pl.setVertOffset(height);
            pl.setLeftRightMove(sideways);
            pl.setShouldSync(true);
        }
        return 1;
    }
    
}
