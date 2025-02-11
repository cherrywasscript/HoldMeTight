package com.ricardthegreat.unnamedsizemod;

import com.mojang.logging.LogUtils;
import com.ricardthegreat.unnamedsizemod.init.CreativeTabInit;
import com.ricardthegreat.unnamedsizemod.init.EntityInit;
import com.ricardthegreat.unnamedsizemod.init.ItemInit;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UnnamedSizeMod.MODID)
public class UnnamedSizeMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "unnamedsizemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public UnnamedSizeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        
        ItemInit.ITEMS.register(modEventBus);
        CreativeTabInit.TABS.register(modEventBus);
        EntityInit.ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the
        // config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    /* 
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }
        */

    @SubscribeEvent
    public void playerLoggedInEvent(PlayerLoggedInEvent event){
        
        //System.out.println(event.getEntity().getPersistentData().getInt(PlayerCarryUtils.ROTATION_NBT_TAG));
        
        //System.out.println("event " + event.getEntity().getUUID());
        
    }

    
}
