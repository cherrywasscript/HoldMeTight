package com.ricardthegreat.unnamedsizemod.Client.handlers;

import com.mojang.brigadier.CommandDispatcher;
import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;
import com.ricardthegreat.unnamedsizemod.Client.Keybindings;
import com.ricardthegreat.unnamedsizemod.Commands.TestCommand;
import com.ricardthegreat.unnamedsizemod.network.PacketHandler;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnnamedSizeMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    private static final Component KEY_PRESSED = Component.translatable("message." + UnnamedSizeMod.MODID + ".key_pressed");

    @SubscribeEvent
    public static void RegisterClientCommandsEvent(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        TestCommand.register(dispatcher);
    } 

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(Keybindings.INSTANCE.shoulderCarryKey.consumeClick() && minecraft.player != null) {
            minecraft.player.displayClientMessage(KEY_PRESSED, false);

            PlayerCarryExtension player = (PlayerCarryExtension) minecraft.player;

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
