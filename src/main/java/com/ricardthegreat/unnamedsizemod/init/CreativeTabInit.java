package com.ricardthegreat.unnamedsizemod.init;

import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
    // registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, UnnamedSizeMod.MODID);

    // List of all items for the tab
    public static final List<Supplier<? extends ItemLike>> TAB_ITEMS = new ArrayList<>();

    // Creates a creative tab with the id "examplemod:example_tab" for the example
    // item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = TABS.register("example_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.example_tab"))
                    .icon(ItemInit.SIZE_ITEM.get()::getDefaultInstance)
                    .displayItems((parameters, output) -> TAB_ITEMS
                            .forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
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
