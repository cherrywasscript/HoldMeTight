package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.entities.projectile.RayGunProjectile;
import com.ricardthegreat.holdmetight.entities.projectile.WandProjectile;
import com.ricardthegreat.holdmetight.inventory.HeldEntityInventoryMenu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, HoldMeTight.MODID);

    public static final RegistryObject<MenuType<HeldEntityInventoryMenu>> HELD_PLAYER_MENU =
    MENUS.register("held_player_menu", () -> new MenuType<HeldEntityInventoryMenu>(HeldEntityInventoryMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus){
        MENUS.register(bus);
    }
}
