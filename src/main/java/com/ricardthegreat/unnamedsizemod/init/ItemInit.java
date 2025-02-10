package com.ricardthegreat.unnamedsizemod.init;

import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;
import com.ricardthegreat.unnamedsizemod.items.PlayerStandinItem;
import com.ricardthegreat.unnamedsizemod.items.SizeRay;
import com.ricardthegreat.unnamedsizemod.items.AdvancedSizeRemote;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.ricardthegreat.unnamedsizemod.init.CreativeTabInit.addToTab;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            UnnamedSizeMod.MODID);

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and
    // saturation 2
    //not actually needed to stay here but keeping incase i want an example
    /* 
    public static final RegistryObject<Item> EXAMPLE_ITEM = addToTab(ITEMS.register("example_item",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .alwaysEat()
                            .nutrition(1)
                            .saturationMod(2f)
                            .build()))));
        */


        public static final RegistryObject<Item> ADVANCED_SIZE_REMOTE = addToTab(
                ITEMS.register("size_remote", () -> new AdvancedSizeRemote(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> SIZE_RAY = addToTab(
                ITEMS.register("size_ray", () -> new SizeRay(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> PLAYER_ITEM = addToTab(
                ITEMS.register("player_item", () -> new PlayerStandinItem(new Item.Properties().stacksTo(1))));
}
