package com.ricardthegreat.holdmetight.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit {
    // Create a Deferred Register to hold CreativeModeTabs which will all be
    // registered under the "UnnamedSizeMod" namespace
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, HoldMeTight.MODID);

    // List of all items for the tab
    public static final List<Supplier<? extends ItemLike>> TAB_ITEMS = new ArrayList<>();

    // Creates a creative tab with the id "UnnamedSizeMod:size_tab" for the 
    // item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> SIZE_TAB = TABS.register("size_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.size_tab"))
                    .icon(ItemInit.CUSTOM_SIZE_REMOTE.get()::getDefaultInstance)
                    .displayItems((parameters, output) -> TAB_ITEMS
                            .forEach(itemLike -> output.accept(itemLike.get())))
                    .build());





    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike){
        TAB_ITEMS.add(itemLike);
        return itemLike;
    }


    // place items into existing creative mode tabs
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event){  
        
    }   

}
