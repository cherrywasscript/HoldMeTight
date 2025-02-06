package com.ricardthegreat.unnamedsizemod.Client;

import com.ricardthegreat.unnamedsizemod.Client.screens.SizeItemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {
    public static void openSizeItemScreen(Player player, InteractionHand hand, MinecraftServer server) {
        Minecraft.getInstance().setScreen(new SizeItemScreen(player, hand, server));
    }
    
}
