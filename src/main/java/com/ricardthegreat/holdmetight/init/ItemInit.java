package com.ricardthegreat.holdmetight.init;

import static com.ricardthegreat.holdmetight.init.CreativeTabInit.addToTab;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.CarryPositonerItem;
import com.ricardthegreat.holdmetight.items.PaperWingsItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.items.SizeRay;
import com.ricardthegreat.holdmetight.items.SizeWand;
import com.ricardthegreat.holdmetight.items.remotes.overtime.OtherTimedSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.overtime.TimedSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.overtime.UserTimedSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.random.OtherRandomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.random.RandomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.random.UserRandomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.setmult.CustomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.setmult.OtherCustomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.setmult.UserCustomSizeRemoteItem;

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
        

        public static final RegistryObject<Item> CUSTOM_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/gold_size_remote", () -> new CustomSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> OTHER_CUSTOM_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/iron_size_remote", () -> new OtherCustomSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> USER_CUSTOM_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/copper_size_remote", () -> new UserCustomSizeRemoteItem(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> RANDOM_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/amethyst_size_remote", () -> new RandomSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> OTHER_RANDOM_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/glowstone_size_remote", () -> new OtherRandomSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> USER_RANDOM_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/lapis_size_remote", () -> new UserRandomSizeRemoteItem(new Item.Properties().stacksTo(1))));
                
        public static final RegistryObject<Item> TIMED_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/emerald_size_remote", () -> new TimedSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> OTHER_TIMED_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/diamond_size_remote", () -> new OtherTimedSizeRemoteItem(new Item.Properties().stacksTo(1))));
        public static final RegistryObject<Item> USER_TIMED_SIZE_REMOTE = addToTab(
                ITEMS.register("remotes/quartz_size_remote", () -> new UserTimedSizeRemoteItem(new Item.Properties().stacksTo(1))));

        

        public static final RegistryObject<Item> SIZE_RAY = addToTab(
                ITEMS.register("size_ray", () -> new SizeRay(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> SIZE_WAND = addToTab(
                ITEMS.register("size_wand", () -> new SizeWand(new Item.Properties().stacksTo(1))));


        public static final RegistryObject<Item> CARRY_POSITION_ITEM = addToTab(
                ITEMS.register("carry_position_item", () -> new CarryPositonerItem(new Item.Properties().stacksTo(1))));




        public static final RegistryObject<Item> PLAYER_ITEM = addToTab(
                ITEMS.register("player_item", () -> new PlayerStandinItem(new Item.Properties().stacksTo(1))));

        public static final RegistryObject<Item> PAPER_WINGS_ITEM = addToTab(
                ITEMS.register("paper_wings_item", () -> new PaperWingsItem(new Item.Properties().durability(432).rarity(Rarity.COMMON))));

                
        public static final RegistryObject<BlockItem> MUSHROOM_HOUSE_ITEM = addToTab(ITEMS.register("mushroom_house", 
                () -> new BlockItem(BlockInit.MUSHROOM_HOUSE.get(), new Item.Properties())));

        public static final RegistryObject<BlockItem> JAR_ITEM = addToTab(ITEMS.register("tiny_jar", 
                () -> new BlockItem(BlockInit.TINY_JAR.get(), new Item.Properties())));
}
