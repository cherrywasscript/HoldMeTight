package com.ricardthegreat.unnamedsizemod.Client.handlers;

import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;
import com.ricardthegreat.unnamedsizemod.Client.Keybindings;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UnnamedSizeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.INSTANCE.shoulderCarryKey);
        event.register(Keybindings.INSTANCE.customCarryKey);
    } 

    
}
