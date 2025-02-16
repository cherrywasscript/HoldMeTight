package com.ricardthegreat.unnamedsizemod.Client;



import com.ricardthegreat.unnamedsizemod.Client.screens.AdvancedSizeRemoteScreen;
import com.ricardthegreat.unnamedsizemod.Client.screens.SizeRayScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {
    public static void openSizeRemoteScreen(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new AdvancedSizeRemoteScreen(player, hand));
    }

    public static void openSizeRayScreen(Player player, InteractionHand hand){
        Minecraft.getInstance().setScreen(new SizeRayScreen(player, hand));
    }
    
}
