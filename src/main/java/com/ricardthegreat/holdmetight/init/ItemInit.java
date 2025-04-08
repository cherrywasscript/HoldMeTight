package com.ricardthegreat.holdmetight.init;

import static com.ricardthegreat.holdmetight.init.CreativeTabInit.addToTab;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.CollarItem;
import com.ricardthegreat.holdmetight.items.PaperWingsItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.items.SizeRay;
import com.ricardthegreat.holdmetight.items.SizeWand;
import com.ricardthegreat.holdmetight.items.remotes.AdvancedSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.BasicSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.MasterSizeRemoteItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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

        public static final RegistryObject<Item> BASIC_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/iron_size_remote", () -> new BasicSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> ADVANCED_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/gold_size_remote", () -> new AdvancedSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> MASTER_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/netherite_size_remote", () -> new MasterSizeRemoteItem(new Item.Properties().stacksTo(1))));

                

        public static final RegistryObject<Item> SIZE_RAY = addToTab(
                ITEMS.register("size_ray", () -> new SizeRay(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> SIZE_WAND = addToTab(
                ITEMS.register("size_wand", () -> new SizeWand(new Item.Properties().stacksTo(1))));





        public static final RegistryObject<Item> PLAYER_ITEM = addToTab(
                ITEMS.register("player_item", () -> new PlayerStandinItem(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> PAPER_WINGS_ITEM = addToTab(
                ITEMS.register("paper_wings_item", () -> new PaperWingsItem(new Item.Properties().durability(432).rarity(Rarity.COMMON))));

                
        public static final RegistryObject<BlockItem> MUSHROOM_HOUSE_ITEM = addToTab(ITEMS.register("mushroom_house", 
                () -> new BlockItem(BlockInit.MUSHROOM_HOUSE.get(), new Item.Properties())));

                /* 
        public static final RegistryObject<BlockItem> JAR_ITEM = addToTab(ITEMS.register("tiny_jar", 
                () -> new BlockItem(BlockInit.TINY_JAR.get(), new Item.Properties())));
                 */

        public static final RegistryObject<Item> COLLAR_ITEM = addToTab(
                ITEMS.register("collar_item", () -> new CollarItem(new Item.Properties().stacksTo(1))));

}
