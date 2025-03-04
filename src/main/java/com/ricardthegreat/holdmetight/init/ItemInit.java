package com.ricardthegreat.holdmetight.init;

import static com.ricardthegreat.holdmetight.init.CreativeTabInit.addToTab;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.AdvancedSizeRemote;
import com.ricardthegreat.holdmetight.items.PaperWingsItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.items.SizeRay;
import com.ricardthegreat.holdmetight.items.SizeWand;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
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

        public static final RegistryObject<Item> PAPER_WINGS_ITEM = addToTab(
                ITEMS.register("paper_wings_item", () -> new PaperWingsItem(new Item.Properties().stacksTo(1))));

                
        public static final RegistryObject<BlockItem> MUSHROOM_HOUSE_ITEM = addToTab(ITEMS.register("mushroom_house", 
                () -> new BlockItem(BlockInit.MUSHROOM_HOUSE.get(), new Item.Properties())));
}
