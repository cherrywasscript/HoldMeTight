package com.ricardthegreat.holdmetight.Client.handlers;

import com.mojang.brigadier.CommandDispatcher;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.Keybindings;
import com.ricardthegreat.holdmetight.Commands.ChatScaleCommand;
import com.ricardthegreat.holdmetight.Commands.TestCommand;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    private static final Component KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".key_pressed");

    @SubscribeEvent
    public static void RegisterClientCommandsEvent(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        TestCommand.register(dispatcher);
        //ChatScaleCommand.register(dispatcher);
    } 

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer mcPlayer = minecraft.player;
        if(Keybindings.INSTANCE.shoulderCarryKey.consumeClick() && mcPlayer != null) {
            mcPlayer.displayClientMessage(KEY_PRESSED, false);

            PlayerCarryExtension player = (PlayerCarryExtension) mcPlayer;

            player.setShoulderCarry(!player.getShoulderCarry());
            player.setCustomCarry(false);
            player.setShouldSync(true);

        }

        if(Keybindings.INSTANCE.customCarryKey.consumeClick() && minecraft.player != null) {
            minecraft.player.displayClientMessage(KEY_PRESSED, true);

            PlayerCarryExtension player = (PlayerCarryExtension) minecraft.player;

            player.setCustomCarry(!player.getCustomCarry());
            player.setShoulderCarry(false);
            player.setShouldSync(true);
            
        }
    }
}
