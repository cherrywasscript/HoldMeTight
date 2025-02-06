package com.ricardthegreat.unnamedsizemod.events;

import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;
import com.ricardthegreat.unnamedsizemod.network.PacketHandler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = UnnamedSizeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
                PacketHandler.register();
        });
    }

}
