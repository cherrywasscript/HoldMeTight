package com.ricardthegreat.holdmetight.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.serverbound.SAddCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SEditCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SRemoveCustomCarryPosPacket;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CustomCarryCommand {
    private static final SimpleCommandExceptionType FAILED_TO_REMOVE_CUSTOM_POS = new SimpleCommandExceptionType(Component.translatable("commands." + HoldMeTight.MODID + ".remove.failed"));
    private static final SimpleCommandExceptionType FAILED_TO_EDIT_CUSTOM_POS = new SimpleCommandExceptionType(Component.translatable("commands." + HoldMeTight.MODID + ".edit.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("customCarry")

            .then(Commands.literal("add")
            .then(Commands.argument("name", StringArgumentType.string())
            .then(Commands.argument("angle", IntegerArgumentType.integer())
            .then(Commands.argument("distance", DoubleArgumentType.doubleArg())
            .then(Commands.argument("height", DoubleArgumentType.doubleArg())
            .then(Commands.argument("sideways", DoubleArgumentType.doubleArg()).executes((source) -> {
                return addCarry(source.getSource(), StringArgumentType.getString(source, "name"), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), null, source.getSource().getEntityOrException());
            })
            .then(Commands.argument("connect to head (optional)", BoolArgumentType.bool()).executes((source) -> {
                return addCarry(source.getSource(), StringArgumentType.getString(source, "name"), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), BoolArgumentType.getBool(source, "connect to head (optional)"), source.getSource().getEntityOrException());
            }))))))))

            .then(Commands.literal("remove")
            .then(Commands.argument("name", StringArgumentType.string()).suggests((context, builder) -> {
                Entity ent = context.getSource().getEntityOrException();
                ArrayList<String> posNames = new ArrayList<>();
                if (ent != null && ent instanceof Player player) {
                    PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                    ArrayList<CarryPosition> positions = playerCarry.getCustomCarryPositions();

                    for (CarryPosition carryPosition : positions) {
                        if (!carryPosition.posName.equals("default")) {
                            posNames.add(carryPosition.posName);  
                        }
                    }
                }
                return SharedSuggestionProvider.suggest(posNames, builder);
            }).executes((source) -> {
                return deleteCarry(source.getSource(), StringArgumentType.getString(source, "name"), source.getSource().getEntityOrException());
            })))

            .then(Commands.literal("edit")
            .then(Commands.argument("name", StringArgumentType.string()).suggests((context, builder) -> {
                Entity ent = context.getSource().getEntityOrException();
                ArrayList<String> posNames = new ArrayList<>();
                if (ent != null && ent instanceof Player player) {
                    PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
                    ArrayList<CarryPosition> positions = playerCarry.getCustomCarryPositions();

                    for (CarryPosition carryPosition : positions) {
                        posNames.add(carryPosition.posName);  
                    }
                }
                return SharedSuggestionProvider.suggest(posNames, builder);
            })
            .then(Commands.argument("angle", IntegerArgumentType.integer())
            .then(Commands.argument("distance", DoubleArgumentType.doubleArg())
            .then(Commands.argument("height", DoubleArgumentType.doubleArg())
            .then(Commands.argument("sideways", DoubleArgumentType.doubleArg()).executes((source) -> {
                return editCarry(source.getSource(), StringArgumentType.getString(source, "name"), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), null, source.getSource().getEntityOrException());
            })
            .then(Commands.argument("connect to head (optional)", BoolArgumentType.bool()).executes((source) -> {
                return editCarry(source.getSource(), StringArgumentType.getString(source, "name"), IntegerArgumentType.getInteger(source, "angle"), DoubleArgumentType.getDouble(source, "distance"), DoubleArgumentType.getDouble(source, "height"), DoubleArgumentType.getDouble(source, "sideways"), BoolArgumentType.getBool(source, "connect to head (optional)"), source.getSource().getEntityOrException());
            }))))))))
            );
    }

    public static int addCarry(CommandSourceStack command, String name, int rotation, double distance, double height, double sideways, @Nullable Boolean head, Entity ent) throws CommandSyntaxException {
        rotation = rotation%360;
        if(ent instanceof Player player){
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            boolean headlink = (head != null) ? head : false;

            CarryPosition pos = new CarryPosition(name, rotation, distance, height, sideways, headlink);

            playerCarry.addCustomCarryPos(pos);

            PacketHandler.sendToServer(new SAddCustomCarryPosPacket(pos));
        }
        return 1;
    }

    public static int deleteCarry(CommandSourceStack command, String carryName, Entity ent) throws CommandSyntaxException{
        if (ent instanceof Player player) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);
            
            if (!playerCarry.removeCustomCarryPos(carryName)) {
                throw FAILED_TO_REMOVE_CUSTOM_POS.create();
            }else{
                PacketHandler.sendToServer(new SRemoveCustomCarryPosPacket(carryName));
            }
        }
        return 1;
    }

    public static int editCarry(CommandSourceStack command, String name, int rotation, double distance, double height, double sideways, @Nullable Boolean head, Entity ent) throws CommandSyntaxException {
        rotation = rotation%360;
        if(ent instanceof Player player){
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            ArrayList<CarryPosition> positions = playerCarry.getCustomCarryPositions();

            int index = -1;
            for (int i = 0; i < positions.size(); i++) {
                if (positions.get(i).posName.equals(name)) {
                    index = i;
                }
            }

            if (index == -1) {
                throw FAILED_TO_EDIT_CUSTOM_POS.create();
            }else{
                boolean headlink = (head != null) ? head : false;

                CarryPosition pos = new CarryPosition(name, rotation, distance, height, sideways, headlink);

                playerCarry.editCustomCarryPos(pos, index);

                PacketHandler.sendToServer(new SEditCustomCarryPosPacket(pos, index));
            }
        }
        return 1;
    }
}
