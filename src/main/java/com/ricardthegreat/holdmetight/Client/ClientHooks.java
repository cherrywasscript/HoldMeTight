package com.ricardthegreat.holdmetight.Client;



import com.ricardthegreat.holdmetight.Client.screens.CarryPositionScreen;
import com.ricardthegreat.holdmetight.Client.screens.SizeRayScreen;
import com.ricardthegreat.holdmetight.Client.screens.remotes.setmult.CustomSizeRemoteScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {
    public static void openSizeRemoteScreen(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new CustomSizeRemoteScreen(player, hand));
    }

    public static void openSizeRayScreen(Player player, InteractionHand hand){
        Minecraft.getInstance().setScreen(new SizeRayScreen(player, hand));
    }

    public static void openCarryPositionScreen(Player player){
        Minecraft.getInstance().setScreen(new CarryPositionScreen(player));
    }
    
}
