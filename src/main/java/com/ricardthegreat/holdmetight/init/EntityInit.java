package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.entities.projectile.RayGunProjectile;
import com.ricardthegreat.holdmetight.entities.projectile.WandProjectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HoldMeTight.MODID);

    public static final RegistryObject<EntityType<RayGunProjectile>> RAY_GUN_PROJECTILE =
    ENTITIES.register("ray_gun_projectile", () -> EntityType.Builder.<RayGunProjectile>of(RayGunProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build(new ResourceLocation(HoldMeTight.MODID, "ray_gun_projectile").toString()));

    public static final RegistryObject<EntityType<WandProjectile>> WAND_PROJECTILE =
    ENTITIES.register("wand_projectile", () -> EntityType.Builder.<WandProjectile>of(WandProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build(new ResourceLocation(HoldMeTight.MODID, "ray_gun_projectile").toString()));
    
}
