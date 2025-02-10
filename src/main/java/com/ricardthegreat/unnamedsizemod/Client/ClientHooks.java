package com.ricardthegreat.unnamedsizemod.Client;



import com.ricardthegreat.unnamedsizemod.Client.screens.SizeItemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {
    public static void openSizeRemoteScreen(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SizeItemScreen(player, hand));
    }

    public static void openSizeRayScreen(Player player, InteractionHand hand){
        System.out.println("size ray");
        //Minecraft.getInstance().setScreen(new SizeRemoteScreen(player, hand));
    }
    
}
