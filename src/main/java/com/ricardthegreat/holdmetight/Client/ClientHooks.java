package com.ricardthegreat.holdmetight.Client;



import com.ricardthegreat.holdmetight.Client.screens.CarryPositionScreen;
import com.ricardthegreat.holdmetight.Client.screens.CarryPositionWheel;
import com.ricardthegreat.holdmetight.Client.screens.SizePrefsScreen;
import com.ricardthegreat.holdmetight.Client.screens.SizeRayScreen;
import com.ricardthegreat.holdmetight.Client.screens.remotes.AdvancedSizeRemoteScreen;
import com.ricardthegreat.holdmetight.Client.screens.remotes.BasicSizeRemoteScreen;
import com.ricardthegreat.holdmetight.Client.screens.remotes.MasterSizeRemoteScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

//i think theres a way to be like "@dist only in client" or seomthing i should look into that so that this class only runs clientside
public class ClientHooks {

    //remote screens
    public static void openBasicSizeRemoteScreen(Player player, InteractionHand hand){
        Minecraft.getInstance().setScreen(new BasicSizeRemoteScreen(player, hand));
    }
    public static void openAdvancedSizeRemoteScreen(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new AdvancedSizeRemoteScreen(player, hand));
    }
    public static void openMasterRemoteScreen(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new MasterSizeRemoteScreen(player, hand));
    }


    //ray screens
    public static void openSizeRayScreen(Player player, InteractionHand hand){
        Minecraft.getInstance().setScreen(new SizeRayScreen(player, hand));
    }


    public static void openCarryPositionWheel(Player player){
        Minecraft.getInstance().setScreen(new CarryPositionWheel(player));
    }
    

    //carry pos screen
    public static void openCarryPositionScreen(Player player){
        Minecraft.getInstance().setScreen(new CarryPositionScreen(player));
    }

    public static void openSizePrefsScreen(Player player){
        Minecraft.getInstance().setScreen(new SizePrefsScreen(player));
    }
    
}
