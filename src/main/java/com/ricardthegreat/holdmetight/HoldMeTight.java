package com.ricardthegreat.holdmetight;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import com.ricardthegreat.holdmetight.Commands.TestingCommand;
import com.ricardthegreat.holdmetight.events.ForgeModEvents;
import com.ricardthegreat.holdmetight.init.BlockEntityInit;
import com.ricardthegreat.holdmetight.init.BlockInit;
import com.ricardthegreat.holdmetight.init.CreativeTabInit;
import com.ricardthegreat.holdmetight.init.EffectsInit;
import com.ricardthegreat.holdmetight.init.EntityInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.init.PotionsInit;
import com.ricardthegreat.holdmetight.init.RecipeInit;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HoldMeTight.MODID)
public class HoldMeTight {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "holdmetight";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    
    public static boolean curiosInstalled;

    public HoldMeTight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        //this exists so i dont have to call ModList.get().isLoaded("curios") every time i wanna check
        //not sure if this is bad or not honestly
        curiosInstalled = ModList.get().isLoaded("curios");



        BlockInit.BLOCKS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        CreativeTabInit.TABS.register(modEventBus);
        EntityInit.ENTITIES.register(modEventBus);
        EffectsInit.register(modEventBus);
        PotionsInit.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the
        // config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        //i've seen mods on 1.20.1 forge use PotionBrewing.addmix() to init potions but for me its private so idk whats up with that????
        //anyway this is a bit of an amalgam because i couldnt find an actual tutorial for 1.20.1 only 1.21 and 1.18.2(https://www.youtube.com/@ModdingByKaupenjoe)
        //they're great but both were slightly wrong for what was needed
        RecipeInit.register();
            
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    /* 
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }
        */

    //TODO figure out why these work here but not in forge or common events

    @SubscribeEvent
    public void playerLoggedInEvent(PlayerLoggedInEvent event){
        Player joiner = event.getEntity();
        Level level = joiner.level();
        MinecraftServer server = level.getServer();
        
        if (server != null) {
            ServerPlayer serverJoiner = server.getPlayerList().getPlayer(joiner.getUUID());
            ForgeModEvents.syncPlayerCapabilities(serverJoiner, server);
        }
    }


    @SubscribeEvent
    public void playerChangedDimensionEvent(PlayerChangedDimensionEvent event){
        Player dimChangePlayer = event.getEntity();
        Level level = dimChangePlayer.level();
        MinecraftServer server = level.getServer();
        
        if (server != null) {
            ServerPlayer serverJoiner = server.getPlayerList().getPlayer(dimChangePlayer.getUUID());
            ForgeModEvents.syncPlayerCapabilities(serverJoiner, server);
        }
    }

    @SubscribeEvent
    public void registerServerCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        TestingCommand.register(dispatcher);
    }

    
}
