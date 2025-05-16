package com.ricardthegreat.holdmetight.Commands;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("rotation")
            .then(Commands.argument("angle", IntegerArgumentType.integer())
            .then(Commands.argument("distance", DoubleArgumentType.doubleArg())
            .then(Commands.argument("height", DoubleArgumentType.doubleArg())
            .then(Commands.argument("sideways", DoubleArgumentType.doubleArg()).executes((source) -> {
                return run(source.getSource(), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), null, source.getSource().getEntityOrException());
            })
            .then(Commands.argument("connect to head", BoolArgumentType.bool()).executes((source) -> {
                return run(source.getSource(), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), BoolArgumentType.getBool(source, "connect to head"), source.getSource().getEntityOrException());
            }))))))
            );
    }

    public static int run(CommandSourceStack command, int rotation, double distance, double height, double sideways, @Nullable Boolean head, Entity ent) throws CommandSyntaxException {
        rotation = rotation%360;
        if(ent instanceof Player){
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) ent);

            boolean headlink = (head != null) ? head : false;

            CarryPosition pos = new CarryPosition("custom", rotation, distance, sideways, height, headlink);

            playerCarry.addCustomCarryPos(pos);
            playerCarry.setShouldSyncAll(true);
        }
        return 1;
    }
    
}
