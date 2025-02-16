package com.ricardthegreat.holdmetight.events;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.network.PacketHandler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
                PacketHandler.register();
        });
    }

}
