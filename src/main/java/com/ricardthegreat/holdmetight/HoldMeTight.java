package com.ricardthegreat.holdmetight;

import com.mojang.logging.LogUtils;
import com.ricardthegreat.holdmetight.init.BlockEntityInit;
import com.ricardthegreat.holdmetight.init.BlockInit;
import com.ricardthegreat.holdmetight.init.CreativeTabInit;
import com.ricardthegreat.holdmetight.init.EffectsInit;
import com.ricardthegreat.holdmetight.init.EntityInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.init.PotionsInit;
import com.ricardthegreat.holdmetight.utils.BetterBrewingRecipe;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
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
@Mod(HoldMeTight.MODID)
public class HoldMeTight {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "holdmetight";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public HoldMeTight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

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
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));


        //not touching the stuff above, should probably comment it out tho

        //i've seen mods on 1.20.1 forge use PotionBrewing.addmix() to init potions but for me its private so idk whats up with that????
        //anyway this is a bit of an amalgam because i couldnt find an actual tutorial for 1.20.1 only 1.21 and 1.18.2(https://www.youtube.com/@ModdingByKaupenjoe)
        //they're great but both were slightly wrong for what was needed

        //shrinking pots
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.FLOWERING_AZALEA, PotionsInit.SHRINK_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_1.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_2.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_3.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_4.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_4_LONG.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4_LONG.get()));

        //growth pots
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD, PotionsInit.GROW_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION.get(), Items.REDSTONE, PotionsInit.GROW_POTION_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_1.get(), Items.REDSTONE, PotionsInit.GROW_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_2.get(), Items.REDSTONE, PotionsInit.GROW_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_3.get(), Items.REDSTONE, PotionsInit.GROW_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_4.get(), Items.REDSTONE, PotionsInit.GROW_POTION_4_LONG.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4_LONG.get()));

        
        //super shrink pots
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_4.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_4_LONG.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION_LONG.get()));
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
