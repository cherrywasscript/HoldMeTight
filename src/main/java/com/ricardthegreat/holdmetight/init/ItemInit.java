package com.ricardthegreat.holdmetight.init;

import static com.ricardthegreat.holdmetight.init.CreativeTabInit.addToTab;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.AdvancedSizeRemote;
import com.ricardthegreat.holdmetight.items.DimTeleporter;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.items.SizeRay;
import com.ricardthegreat.holdmetight.items.SizeWand;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            HoldMeTight.MODID);

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

        public static final RegistryObject<Item> SIZE_WAND = addToTab(
                ITEMS.register("size_wand", () -> new SizeWand(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> PLAYER_ITEM = addToTab(
                ITEMS.register("player_item", () -> new PlayerStandinItem(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> DIM_TELEPORTER = addToTab(
                ITEMS.register("dim_teleporter", () -> new DimTeleporter(new Item.Properties().stacksTo(1))));
}
